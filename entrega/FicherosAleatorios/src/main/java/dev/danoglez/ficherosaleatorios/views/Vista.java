package dev.danoglez.ficherosaleatorios.views;

/** Contrato comun de las vistas de la aplicacion. */
public interface Vista extends AutoCloseable {

    // Devuelve el texto, o null si se cancela o termina la entrada.
    String pedirTexto(String mensaje);

    void mostrarTexto(String texto);

    void mostrarError(String mensaje);

    @Override
    void close();
}
