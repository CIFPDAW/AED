package dev.danoglez.gamehubdaniel.exception;

/**
 * Excepcion propia para encapsular errores de lectura/escritura de ficheros.
 */
public class GameHubFileException extends Exception {

    // Constructores
    public GameHubFileException(String mensaje) {
        super(mensaje);
    }

    public GameHubFileException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
