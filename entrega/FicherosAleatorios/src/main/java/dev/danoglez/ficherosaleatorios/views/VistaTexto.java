package dev.danoglez.ficherosaleatorios.views;

import java.util.Scanner;

/** Vista: solicita datos y muestra mensajes por consola. */
public class VistaTexto implements Vista {

    // Variables
    private final Scanner entrada;

    // Constructores
    public VistaTexto() {
        this.entrada = new Scanner(System.in);
    }

    // Metodos
    @Override
    public String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.hasNextLine() ? entrada.nextLine() : null;
    }

    @Override
    public void mostrarTexto(String texto) {
        System.out.println(texto);
    }

    @Override
    public void mostrarError(String mensaje) {
        System.err.println("Error: " + mensaje);
    }

    // Cierra el Scanner y la entrada estandar al finalizar el uso de la vista.
    @Override
    public void close() {
        entrada.close();
    }
}
