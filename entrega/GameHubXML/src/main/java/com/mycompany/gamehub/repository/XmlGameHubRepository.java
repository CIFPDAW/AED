package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

    /*
    const lista = document.getElementById("lista");

    // Crear un nuevo elemento <li>
    const nuevoNodo = document.createElement("li");

    // Añadir texto al elemento
    nuevoNodo.textContent = "Nuevo elemento";

    // Añadirlo al final de la lista
    lista.appendChild(nuevoNodo);
    */

public class XmlGameHubRepository implements IGameHubRepository {

    private Path ruta = Path.of("data", "videojuegos.xml");

    @Override
    public List<Game> cargar() throws GameHubXmlException {

        List<Game> videojuegos = new ArrayList<>();

        try {
            crearXmlSiNoExiste();

            Document doc = leerDocumento();

            NodeList nodos = doc.getElementsByTagName("videojuego");

            for (int i = 0; i < nodos.getLength(); i++) {
                if (nodos.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nodos.item(i);
                    videojuegos.add(leerVideojuego(e));
                }
            }

        } catch (Exception e) {
            throw new GameHubXmlException("Error al cargar el XML: " + e.getMessage(), e);
        }

        return videojuegos;
    }

    @Override
    public void guardar(List<Game> videojuegos) throws GameHubXmlException {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element raiz = doc.createElement("gamehub");
            doc.appendChild(raiz);

            Element contenedor = doc.createElement("videojuegos");
            raiz.appendChild(contenedor);

            for (Game juego : videojuegos) {
                Element eJuego = doc.createElement("videojuego");
                eJuego.setAttribute("id", String.valueOf(juego.getId()));

                eJuego.appendChild(crearElemento(doc, "titulo", juego.getTitulo()));
                eJuego.appendChild(crearElemento(doc, "plataforma", juego.getPlataforma()));
                /*
                // Crear el contenedor <plataformas>
                Element plataformas = doc.createElement("plataformas");

                // Recorrer todas las plataformas del videojuego
                for (String plataforma : juego.getPlataformas()) {

                    // Crear una etiqueta <plataforma> por cada plataforma
                    Element ePlataforma = crearElemento(doc, "plataforma", plataforma);

                    // Añadir la plataforma al contenedor
                    plataformas.appendChild(ePlataforma);
                }

                // Añadir el contenedor de plataformas al videojuego
                eJuego.appendChild(plataformas);
                */
                eJuego.appendChild(crearElemento(doc, "genero", juego.getGenero()));
                eJuego.appendChild(crearElemento(doc, "horasJugadas", String.valueOf(juego.getHorasJugadas())));
                eJuego.appendChild(crearElemento(doc, "finalizado", String.valueOf(juego.isFinalizado())));

                contenedor.appendChild(eJuego);
            }

            escribirDocumento(doc);

        } catch (Exception e) {
            throw new GameHubXmlException("Error al guardar el XML: " + e.getMessage(), e);
        }
    }

    @Override
    public Game buscarPorId(int id) throws GameHubXmlException {

        try {
            crearXmlSiNoExiste();
            Document doc = leerDocumento();

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expresion = "/gamehub/videojuegos/videojuego[@id='" + id + "']";

            Node nodo = (Node) xpath.evaluate(expresion, doc, XPathConstants.NODE);

            if (nodo == null) {
                return null;
            }
            return leerVideojuego((Element) nodo);

        } catch (Exception e) {
            throw new GameHubXmlException("Error al buscar en el XML: " + e.getMessage(), e);
        }
    }

    private void crearXmlSiNoExiste() throws Exception {

        if (Files.exists(ruta)) {
            return;
        }

        Files.createDirectories(ruta.getParent());

        String base = """
                <?xml version="1.0" encoding="UTF-8"?>
                <gamehub>
                    <videojuegos/>
                </gamehub>
                """;

        Files.writeString(ruta, base);
    }

    private Document leerDocumento() throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);

        DocumentBuilder builder = dbf.newDocumentBuilder();

        try (InputStream in = Files.newInputStream(ruta)) {
            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();
            return doc;
        }
    }

    private void escribirDocumento(Document doc) throws Exception {

        doc.setXmlStandalone(true);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        try (OutputStream out = Files.newOutputStream(ruta)) {
            transformer.transform(new DOMSource(doc), new StreamResult(out));
        }
    }

    private Game leerVideojuego(Element e) {

        int id = Integer.parseInt(e.getAttribute("id"));
        String titulo = textoHijo(e, "titulo");
        String plataforma = textoHijo(e, "plataforma");
        // Si tuviera varias plataformas
        /*
        // Crear el contenedor de plataformas
        Element plataformas = doc.createElement("plataformas");

        // Recorrer todas las plataformas del videojuego
        for (String plataforma : juego.getPlataformas()) {

            // Crear una etiqueta <plataforma> por cada plataforma
            Element ePlataforma = crearElemento(doc, "plataforma", plataforma);

            // Añadir la plataforma al contenedor
            plataformas.appendChild(ePlataforma);
        }

        // Añadir el contenedor al videojuego
        eJuego.appendChild(plataformas);
        */
        String genero = textoHijo(e, "genero");
        double horas = Double.parseDouble(textoHijo(e, "horasJugadas"));
        boolean finalizado = Boolean.parseBoolean(textoHijo(e, "finalizado"));

        return new Game(id, titulo, plataforma, genero, horas, finalizado);
    }

    private String textoHijo(Element padre, String etiqueta) {
        NodeList hijos = padre.getElementsByTagName(etiqueta);
        if (hijos.getLength() == 0) {
            return "";
        }
        return hijos.item(0).getTextContent().trim();
    }

    private Element crearElemento(Document doc, String nombre, String valor) {
        Element e = doc.createElement(nombre);
        e.setTextContent(valor);
        return e;
    }
    
    // Expresiones XPATH
    /*
    // 1. Seleccionar todos los videojuegos
    String expresion = "/gamehub/videojuegos/videojuego";

    // 2. Seleccionar un videojuego por su ID
    String expresion = "/gamehub/videojuegos/videojuego[@id='1']";

    // 3. Seleccionar un videojuego usando una variable ID
    String expresion = "/gamehub/videojuegos/videojuego[@id='" + id + "']";

    // 4. Seleccionar los títulos de todos los videojuegos
    String expresion = "/gamehub/videojuegos/videojuego/titulo";

    // 5. Seleccionar el título del videojuego con ID 2
    String expresion = "/gamehub/videojuegos/videojuego[@id='2']/titulo";

    // 6. Seleccionar videojuegos de género RPG
    String expresion = "/gamehub/videojuegos/videojuego[genero='RPG']";

    // 7. Seleccionar videojuegos con precio superior a 60
    String expresion = "/gamehub/videojuegos/videojuego[precio > 60]";

    // 8. Seleccionar videojuegos cuyo título sea Cyberpunk 2077
    String expresion = "/gamehub/videojuegos/videojuego[titulo='Cyberpunk 2077']";

    // 9. Seleccionar el primer videojuego
    String expresion = "/gamehub/videojuegos/videojuego[1]";

    // 10. Seleccionar videojuegos con ID 1 o ID 2
    String expresion = "/gamehub/videojuegos/videojuego[@id='1' or @id='2']";

    // 11. Seleccionar todos los atributos ID
    String expresion = "/gamehub/videojuegos/videojuego/@id";

    // 12. Seleccionar cualquier videojuego del documento
    String expresion = "//videojuego";
    */
}