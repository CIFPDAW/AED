package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * REPOSITORIO XML: la ÚNICA clase del programa que toca el fichero XML.
 *
 * ---------------------------------------------------------------------------
 * IDEA BÁSICA DE DOM (Document Object Model)
 * ---------------------------------------------------------------------------
 * DOM convierte el fichero XML en un ÁRBOL de objetos en memoria:
 *
 *   Document                      -> el fichero entero
 *    └── <gamehub>                -> elemento raíz (doc.getDocumentElement())
 *         └── <videojuegos>       -> contenedor
 *              ├── <videojuego id="1">   -> un Element; "id" es un ATRIBUTO
 *              │     ├── <titulo>Hollow Knight</titulo>  -> Element hijo con TEXTO
 *              │     ├── <plataforma>PC</plataforma>
 *              │     └── ...
 *              └── <videojuego id="2"> ...
 *
 * Clases que se usan:
 *  - DocumentBuilderFactory / DocumentBuilder -> LEEN el XML y crean el Document.
 *  - Document  -> el árbol completo. También sirve para CREAR elementos nuevos.
 *  - Element   -> una etiqueta (<videojuego>, <titulo>...).
 *  - NodeList  -> una lista de nodos (lo que devuelve getElementsByTagName).
 *  - Transformer -> ESCRIBE el árbol Document en el fichero.
 *  - XPath     -> permite buscar dentro del árbol con una "ruta", como en carpetas.
 *
 * ---------------------------------------------------------------------------
 * ¿EL XML CAMBIA? -> Mira las CONSTANTES de abajo y el fichero GUIA_XML.md
 * ---------------------------------------------------------------------------
 */
public class XmlGameHubRepository implements IGameHubRepository {

    // =======================================================================
    // NOMBRES DE LAS ETIQUETAS DEL XML
    // Si el profesor te pide otro XML (por ejemplo <juegos> en vez de
    // <videojuegos>), normalmente basta con cambiar el texto de aquí.
    // =======================================================================
    private static final String ETIQUETA_RAIZ = "gamehub";           // <gamehub>
    private static final String ETIQUETA_CONTENEDOR = "videojuegos"; // <videojuegos>
    private static final String ETIQUETA_VIDEOJUEGO = "videojuego";  // <videojuego id="...">
    private static final String ATRIBUTO_ID = "id";                  // id="1"
    private static final String ETIQUETA_TITULO = "titulo";
    private static final String ETIQUETA_PLATAFORMA = "plataforma";
    private static final String ETIQUETA_GENERO = "genero";
    private static final String ETIQUETA_HORAS = "horasJugadas";
    private static final String ETIQUETA_FINALIZADO = "finalizado";

    // Ruta del fichero. Es relativa: se busca desde la carpeta del proyecto.
    private final Path ruta;

    // Constructor
    public XmlGameHubRepository() {
        this.ruta = Path.of("data", "videojuegos.xml");
    }

    // =======================================================================
    // 1. CARGAR: XML -> List<Game>
    // =======================================================================
    @Override
    public List<Game> cargar() throws GameHubXmlException {

        // Paso 1 y 2: si el fichero no existe, se crea con la estructura base
        crearXmlSiNoExiste();

        // Paso 3: leer el fichero y convertirlo en un árbol DOM
        Document doc = cargarDocumento();

        // Paso 4: recorrer los <videojuego> y convertir cada uno en un objeto Game
        List<Game> videojuegos = new ArrayList<>();

        // getElementsByTagName busca TODAS las etiquetas <videojuego> del documento,
        // da igual a qué profundidad estén.
        NodeList nodos = doc.getElementsByTagName(ETIQUETA_VIDEOJUEGO);

        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);

            // Un NodeList puede contener nodos de varios tipos (texto, comentarios...).
            // Solo nos interesan los ELEMENTOS (etiquetas).
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) nodo;
                videojuegos.add(leerVideojuego(elemento));
            }
        }

        return videojuegos;
    }

    // =======================================================================
    // 2. GUARDAR: List<Game> -> XML
    // Se construye un árbol DOM nuevo desde cero con los datos de la lista
    // y se escribe en el fichero (sobrescribiéndolo).
    // =======================================================================
    @Override
    public void guardar(List<Game> videojuegos) throws GameHubXmlException {

        try {
            // Crear un documento VACÍO (todavía no hay ninguna etiqueta)
            DocumentBuilder builder = crearFactorySegura().newDocumentBuilder();
            Document doc = builder.newDocument();

            // <gamehub>  -> se crea y se pone como raíz del documento
            Element raiz = doc.createElement(ETIQUETA_RAIZ);
            doc.appendChild(raiz);

            // <videojuegos> -> se crea y se mete DENTRO de <gamehub>
            Element contenedor = doc.createElement(ETIQUETA_CONTENEDOR);
            raiz.appendChild(contenedor);

            // Un <videojuego> por cada objeto Game, metido dentro de <videojuegos>
            for (Game videojuego : videojuegos) {
                contenedor.appendChild(crearElementoVideojuego(doc, videojuego));
            }

            // Escribir el árbol en el fichero
            guardarDocumento(doc);

        } catch (ParserConfigurationException e) {
            throw new GameHubXmlException("No se pudo crear el documento XML.", e);
        }
    }

    // =======================================================================
    // 3. BUSCAR CON XPATH directamente en el fichero
    // XPath es como una ruta de carpetas pero dentro del XML:
    //   /gamehub/videojuegos/videojuego[@id='2']
    //   = "dentro de gamehub, dentro de videojuegos, el videojuego cuyo
    //      atributo id vale 2". La @ significa "atributo".
    // =======================================================================
    @Override
    public Game buscarPorId(int id) throws GameHubXmlException {

        crearXmlSiNoExiste();
        Document doc = cargarDocumento();

        // Como id es un int ya sabemos que es un número, así que es seguro
        // meterlo en la expresión (no puede "romper" el XPath).
        String expresion = "/" + ETIQUETA_RAIZ + "/" + ETIQUETA_CONTENEDOR + "/"
                + ETIQUETA_VIDEOJUEGO + "[@" + ATRIBUTO_ID + "='" + id + "']";

        try {
            XPath xpath = XPathFactory.newInstance().newXPath();

            // XPathConstants.NODE -> devuelve UN nodo (o null si no hay ninguno)
            // XPathConstants.NODESET devolvería una lista de nodos
            Node nodo = (Node) xpath.evaluate(expresion, doc, XPathConstants.NODE);

            if (nodo instanceof Element elemento) {
                return leerVideojuego(elemento);
            }
            return null;

        } catch (XPathExpressionException e) {
            throw new GameHubXmlException("Error en la búsqueda XPath: " + expresion, e);
        }
    }

    // =======================================================================
    // MÉTODOS AUXILIARES (privados)
    // =======================================================================

    /**
     * Crea el fichero con la estructura mínima si todavía no existe:
     *   <gamehub>
     *       <videojuegos/>
     *   </gamehub>
     */
    private void crearXmlSiNoExiste() throws GameHubXmlException {

        try {
            // Si existe y tiene contenido, no se hace nada
            if (Files.exists(ruta) && Files.size(ruta) > 0) {
                return;
            }

            // Crea la carpeta "data" si no existe
            if (ruta.getParent() != null) {
                Files.createDirectories(ruta.getParent());
            }

            String base = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <%s>
                        <%s/>
                    </%s>
                    """.formatted(ETIQUETA_RAIZ, ETIQUETA_CONTENEDOR, ETIQUETA_RAIZ);

            Files.writeString(ruta, base, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new GameHubXmlException("No se pudo crear el fichero XML: " + ruta, e);
        }
    }

    /**
     * Crea el DocumentBuilderFactory de forma SEGURA (como en los apuntes, 9.2.3).
     * Se desactivan DTD y entidades externas para evitar ataques XXE.
     * Nuestro XML no usa nada de eso, así que no perdemos nada.
     */
    private DocumentBuilderFactory crearFactorySegura() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        return dbf;
    }

    /**
     * Lee el fichero XML y devuelve el árbol DOM (Document).
     */
    private Document cargarDocumento() throws GameHubXmlException {

        try {
            DocumentBuilder builder = crearFactorySegura().newDocumentBuilder();

            // Sin esto, Java imprime "[Fatal Error] ..." por su cuenta cuando el XML
            // está mal. DefaultHandler no imprime nada y sigue lanzando la excepción,
            // que capturamos abajo y mostramos nosotros.
            builder.setErrorHandler(new DefaultHandler());

            // try-with-resources: el InputStream se cierra solo al terminar
            try (InputStream in = Files.newInputStream(ruta)) {
                Document doc = builder.parse(in);      // XML -> árbol DOM
                doc.getDocumentElement().normalize();  // limpia nodos de texto partidos

                // Comprobar que la raíz es la que esperamos
                String raiz = doc.getDocumentElement().getTagName();
                if (!raiz.equals(ETIQUETA_RAIZ)) {
                    throw new GameHubXmlException("La raíz del XML debería ser <"
                            + ETIQUETA_RAIZ + "> pero es <" + raiz + ">.");
                }
                return doc;
            }

        } catch (ParserConfigurationException e) {
            throw new GameHubXmlException("No se pudo configurar el lector XML.", e);
        } catch (SAXException e) {
            // SAXException = el XML está mal escrito (etiqueta sin cerrar, etc.)
            throw new GameHubXmlException("El fichero XML está mal formado: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new GameHubXmlException("No se pudo leer el fichero XML: " + ruta, e);
        }
    }

    /**
     * Escribe el árbol DOM en el fichero con Transformer (apuntes 9.2.10).
     * Queda indentado con 4 espacios y en UTF-8.
     */
    private void guardarDocumento(Document doc) throws GameHubXmlException {

        try {
            // Quita el standalone="no" de la cabecera <?xml ...?>
            doc.setXmlStandalone(true);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            // Propiedad propia del Java de Oracle/OpenJDK: pone un salto de línea
            // después de la cabecera <?xml ...?> (si no, <gamehub> sale pegado a ella)
            transformer.setOutputProperty("http://www.oracle.com/xml/is-standalone", "yes");

            if (ruta.getParent() != null) {
                Files.createDirectories(ruta.getParent());
            }

            // DOMSource = de dónde salen los datos (el árbol)
            // StreamResult = a dónde van (el fichero)
            try (OutputStream out = Files.newOutputStream(ruta)) {
                transformer.transform(new DOMSource(doc), new StreamResult(out));
            }

        } catch (TransformerException e) {
            throw new GameHubXmlException("No se pudo transformar el árbol DOM a XML.", e);
        } catch (IOException e) {
            throw new GameHubXmlException("No se pudo escribir el fichero XML: " + ruta, e);
        }
    }

    /**
     * Convierte UN elemento <videojuego> en un objeto Game.
     * Si algún dato está mal (no es un número, falta, etc.) lanza una excepción
     * diciendo qué videojuego falla.
     */
    private Game leerVideojuego(Element e) throws GameHubXmlException {

        // El id es un ATRIBUTO -> getAttribute
        String textoId = e.getAttribute(ATRIBUTO_ID);

        try {
            int id = Integer.parseInt(textoId.trim());

            // El resto son ELEMENTOS HIJOS -> textoHijo
            String titulo = textoHijo(e, ETIQUETA_TITULO);
            String plataforma = textoHijo(e, ETIQUETA_PLATAFORMA);
            String genero = textoHijo(e, ETIQUETA_GENERO);
            double horas = Double.parseDouble(textoHijo(e, ETIQUETA_HORAS));
            boolean finalizado = leerBooleano(textoHijo(e, ETIQUETA_FINALIZADO));

            // El constructor de Game valida (título vacío, horas negativas...)
            return new Game(id, titulo, plataforma, genero, horas, finalizado);

        } catch (IllegalArgumentException ex) {
            // NumberFormatException también es IllegalArgumentException
            throw new GameHubXmlException("Datos incorrectos en el videojuego con id='"
                    + textoId + "': " + ex.getMessage(), ex);
        }
    }

    /**
     * Devuelve el texto de la PRIMERA etiqueta hija con ese nombre.
     * Ejemplo: textoHijo(e, "titulo") con <titulo>Hollow Knight</titulo>
     * devuelve "Hollow Knight". Si no existe la etiqueta devuelve "".
     */
    private String textoHijo(Element padre, String etiqueta) {
        NodeList hijos = padre.getElementsByTagName(etiqueta);
        return hijos.getLength() == 0 ? "" : hijos.item(0).getTextContent().trim();
    }

    // Boolean.parseBoolean("hola") daría false sin avisar; así solo vale true/false
    private boolean leerBooleano(String texto) {
        if (texto.equalsIgnoreCase("true")) {
            return true;
        }
        if (texto.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException("<" + ETIQUETA_FINALIZADO
                + "> debe ser true o false y vale '" + texto + "'.");
    }

    /**
     * Convierte UN objeto Game en un elemento <videojuego> con sus hijos
     * (apuntes 9.2.6):
     *   <videojuego id="1">
     *       <titulo>...</titulo>
     *       ...
     *   </videojuego>
     */
    private Element crearElementoVideojuego(Document doc, Game videojuego) {

        Element eJuego = doc.createElement(ETIQUETA_VIDEOJUEGO);

        // El id va como ATRIBUTO
        eJuego.setAttribute(ATRIBUTO_ID, String.valueOf(videojuego.getId()));

        // El resto como ELEMENTOS HIJOS
        eJuego.appendChild(elementoTexto(doc, ETIQUETA_TITULO, videojuego.getTitulo()));
        eJuego.appendChild(elementoTexto(doc, ETIQUETA_PLATAFORMA, videojuego.getPlataforma()));
        eJuego.appendChild(elementoTexto(doc, ETIQUETA_GENERO, videojuego.getGenero()));
        eJuego.appendChild(elementoTexto(doc, ETIQUETA_HORAS, String.valueOf(videojuego.getHorasJugadas())));
        eJuego.appendChild(elementoTexto(doc, ETIQUETA_FINALIZADO, String.valueOf(videojuego.isFinalizado())));

        return eJuego;
    }

    /**
     * Crea una etiqueta con texto dentro: <nombre>valor</nombre>
     * setTextContent escapa solo los caracteres especiales (&, <, >),
     * así que un título como "Ratchet & Clank" se guarda bien.
     */
    private Element elementoTexto(Document doc, String nombre, String valor) {
        Element e = doc.createElement(nombre);
        e.setTextContent(valor);
        return e;
    }
}
