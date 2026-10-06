package com.mycompany.objectinputstreamandobjectoutputstream.views;

public interface Vista extends AutoCloseable {
    public String pedirTexto(String texto);
    public int pedirNumero(String texto);
    public double pedirDouble(String texto);
    public void mostrarTexto(String texto);
    public void mostrarMenu();
    public int pedirOpcion();
    public void mostrarError(String mensaje);
    @Override
    public void close();
}
