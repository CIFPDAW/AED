package dev.danoglez.gamehubdaniel.views;

/** Contrato comun de las vistas de la aplicacion. */
public interface Vista extends AutoCloseable {

    String pedirTexto(String mensaje);

    int pedirNumero(String mensaje);

    double pedirDouble(String mensaje);

    boolean pedirBooleano(String mensaje);

    boolean pedirConfirmacion(String mensaje);

    void mostrarTexto(String texto);

    void mostrarError(String mensaje);

    int pedirOpcion();

    @Override
    void close();
}
