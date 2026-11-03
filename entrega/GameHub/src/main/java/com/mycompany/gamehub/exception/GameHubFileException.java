package com.mycompany.gamehub.exception;

public class GameHubFileException extends Exception {

    // Constructores
    public GameHubFileException(String mensaje) {
        super(mensaje);
    }

    public GameHubFileException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
