package dev.danoglez.ac127.views;

/** Contrato común de las vistas de la aplicación. */
public interface Vista extends AutoCloseable {

    String pedirTexto(String mensaje);

    void mostrarTexto(String texto);

    void mostrarError(String mensaje);

    @Override
    void close();
}
