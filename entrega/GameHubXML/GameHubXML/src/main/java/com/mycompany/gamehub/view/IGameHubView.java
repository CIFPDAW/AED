package com.mycompany.gamehub.view;

/**
 * Lo que tiene que saber hacer la vista. El controlador solo usa esta interfaz.
 */
public interface IGameHubView extends AutoCloseable {

    String pedirTexto(String texto);

    int pedirNumero(String texto);

    double pedirDouble(String texto);

    boolean pedirBooleano(String texto);

    boolean pedirConfirmacion(String texto);

    void mostrarTexto(String texto);

    void mostrarMenu();

    int pedirOpcion();

    void mostrarError(String mensaje);

    @Override
    void close();
}
