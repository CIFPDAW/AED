package dev.danoglez.gamehubxml.exception;

/**
 * Errores propios de leer, crear o guardar el XML.
 * Envuelve la causa (SAXException, IOException, etc.) para no dejar catch vacios.
 */
public class GameHubXmlException extends Exception {

    public GameHubXmlException(String mensaje) {
        super(mensaje);
    }

    public GameHubXmlException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
