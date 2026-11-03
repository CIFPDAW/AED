package com.mycompany.gamehub.exception;

public class GameHubXmlException extends Exception {

    public GameHubXmlException(String mensaje) {
        super(mensaje);
    }

    public GameHubXmlException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
