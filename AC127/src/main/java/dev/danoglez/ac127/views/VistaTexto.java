package dev.danoglez.ac127.views;

import java.util.Scanner;

/** Vista: solicita datos y muestra mensajes por consola. */
public class VistaTexto implements Vista {
    private final Scanner entrada;

    public VistaTexto() {
        this.entrada = new Scanner(System.in);
    }

    @Override
    public String pedirTexto(String mensaje) {
        System.out.println(mensaje);
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

    @Override
    public void close() {
        entrada.close();
    }
}
