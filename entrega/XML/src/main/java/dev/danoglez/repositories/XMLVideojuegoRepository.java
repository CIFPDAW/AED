package dev.danoglez.repositories;

import dev.danoglez.exception.VideojuegoFileException;
import dev.danoglez.models.Videojuego;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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

/**
 * Repositorio XML con DOM.
 * El id va en el atributo de videojuego. El resto son elementos hijos.
 * Esta clase no muestra menu ni pide datos por teclado.
 */
public class XMLVideojuegoRepository implements IVideojuegoRepository {

    // Variables
    private final File fichero;

    // Constructores
    public XMLVideojuegoRepository(String ruta) {
        assert ruta != null : "La ruta del fichero no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    @Override
    public List<Videojuego> cargar() throws VideojuegoFileException {
        asegurarXml();

        try {
            Document doc = leerDocumento();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodos = (NodeList) xpath.evaluate(
                    "/gamehub/videojuegos/videojuego", doc, XPathConstants.NODESET);

            List<Videojuego> videojuegos = new ArrayList<>();
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    videojuegos.add(leerVideojuego((Element) nodo));
                }
            }
            return videojuegos;
        } catch (VideojuegoFileException e) {
            throw e;
        } catch (Exception e) {
            throw new VideojuegoFileException("No se pudo leer el fichero XML.", e);
        }
    }

    @Override
    public void guardar(List<Videojuego> videojuegos) throws VideojuegoFileException {
        assert videojuegos != null : "La lista de videojuegos no puede ser nula";

        File carpeta = fichero.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new VideojuegoFileException("No se pudo crear la carpeta del fichero XML.");
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element raiz = doc.createElement("gamehub");
            doc.appendChild(raiz);

            Element contenedor = doc.createElement("videojuegos");
            raiz.appendChild(contenedor);

            for (Videojuego videojuego : videojuegos) {
                Element elemento = doc.createElement("videojuego");
                elemento.setAttribute("id", String.valueOf(videojuego.getId()));
                elemento.appendChild(crearElemento(doc, "titulo", videojuego.getTitulo()));
                elemento.appendChild(crearElemento(doc, "plataforma", videojuego.getPlataforma()));
                elemento.appendChild(crearElemento(doc, "genero", videojuego.getGenero()));
                elemento.appendChild(crearElemento(doc, "horasJugadas",
                        String.valueOf(videojuego.getHorasJugadas())));
                elemento.appendChild(crearElemento(doc, "finalizado",
                        String.valueOf(videojuego.isFinalizado())));
                contenedor.appendChild(elemento);
            }

            escribirDocumento(doc);
        } catch (VideojuegoFileException e) {
            throw e;
        } catch (Exception e) {
            throw new VideojuegoFileException("No se pudo guardar el fichero XML.", e);
        }
    }

    @Override
    public Videojuego buscarPorId(int id) throws VideojuegoFileException {
        asegurarXml();

        try {
            Document doc = leerDocumento();
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expresion = "/gamehub/videojuegos/videojuego[@id='" + id + "']";
            Node nodo = (Node) xpath.evaluate(expresion, doc, XPathConstants.NODE);

            if (nodo == null || nodo.getNodeType() != Node.ELEMENT_NODE) {
                return null;
            }
            return leerVideojuego((Element) nodo);
        } catch (VideojuegoFileException e) {
            throw e;
        } catch (Exception e) {
            throw new VideojuegoFileException("No se pudo buscar en el fichero XML.", e);
        }
    }

    // Si el fichero no existe, crea gamehub/videojuegos vacio
    private void asegurarXml() throws VideojuegoFileException {
        if (fichero.exists()) {
            return;
        }

        File carpeta = fichero.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new VideojuegoFileException("No se pudo crear la carpeta del fichero XML.");
        }

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element raiz = doc.createElement("gamehub");
            doc.appendChild(raiz);
            raiz.appendChild(doc.createElement("videojuegos"));
            escribirDocumento(doc);
        } catch (Exception e) {
            throw new VideojuegoFileException("No se pudo crear el fichero XML.", e);
        }
    }

    private Document leerDocumento() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        try (InputStream entrada = Files.newInputStream(fichero.toPath())) {
            Document doc = builder.parse(entrada);
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

        try (OutputStream salida = Files.newOutputStream(fichero.toPath())) {
            transformer.transform(new DOMSource(doc), new StreamResult(salida));
        }
    }

    private Videojuego leerVideojuego(Element elemento) throws VideojuegoFileException {
        String idTexto = elemento.getAttribute("id").trim();
        if (idTexto.isBlank()) {
            throw new VideojuegoFileException("Un videojuego del XML no tiene id.");
        }

        try {
            int id = Integer.parseInt(idTexto);
            String titulo = textoHijo(elemento, "titulo");
            String plataforma = textoHijo(elemento, "plataforma");
            String genero = textoHijo(elemento, "genero");
            double horas = Double.parseDouble(textoHijo(elemento, "horasJugadas"));
            boolean finalizado = Boolean.parseBoolean(textoHijo(elemento, "finalizado"));

            if (id <= 0 || titulo.isBlank() || plataforma.isBlank() || genero.isBlank() || horas < 0) {
                throw new VideojuegoFileException(
                        "Datos incorrectos en el videojuego con id " + idTexto + ".");
            }

            return new Videojuego(id, titulo, plataforma, genero, horas, finalizado);
        } catch (VideojuegoFileException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new VideojuegoFileException(
                    "Datos incorrectos en el videojuego con id " + idTexto + ".", e);
        }
    }

    private String textoHijo(Element padre, String etiqueta) throws VideojuegoFileException {
        NodeList hijos = padre.getElementsByTagName(etiqueta);
        if (hijos.getLength() == 0 || hijos.item(0).getTextContent() == null) {
            throw new VideojuegoFileException("Falta el campo " + etiqueta + " en el XML.");
        }
        return hijos.item(0).getTextContent().trim();
    }

    private Element crearElemento(Document doc, String nombre, String valor) {
        Element elemento = doc.createElement(nombre);
        elemento.setTextContent(valor);
        return elemento;
    }
}
