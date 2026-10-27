package dev.danoglez.exception;

/**
 * Errores propios de leer, crear o guardar el XML.
 * Envuelve la causa para no dejar catch vacios.
 */
public class VideojuegoFileException extends Exception {

    // Constructores
    public VideojuegoFileException(String mensaje) {
        super(mensaje);
    }

    public VideojuegoFileException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
