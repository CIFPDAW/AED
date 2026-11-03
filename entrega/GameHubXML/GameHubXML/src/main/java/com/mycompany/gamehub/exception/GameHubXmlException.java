package com.mycompany.gamehub.exception;

/**
 * Excepción propia para los errores al leer, crear o guardar el fichero XML.
 *
 * ¿Por qué una excepción propia? Al trabajar con XML pueden saltar muchas
 * excepciones distintas de Java (IOException, SAXException,
 * ParserConfigurationException, TransformerException, XPathExpressionException...).
 * El repositorio las "envuelve" todas en esta, así el controlador solo tiene
 * que capturar UNA excepción y mostrar el mensaje al usuario.
 */
public class GameHubXmlException extends Exception {

    // Constructores
    public GameHubXmlException(String mensaje) {
        super(mensaje);
    }

    // "causa" es la excepción original de Java, se guarda por si hay que depurar
    public GameHubXmlException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
