package dev.danoglez.gamehubxml.views;

/** Contrato de la vista. El controlador habla con esta interfaz, no con System.out. */
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
