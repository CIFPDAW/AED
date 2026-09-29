package com.cifp.views;

import java.util.Scanner;

/** Vista: solicita datos y muestra mensajes por consola. */
public class VistaTexto implements Vista {
    private final Scanner entrada;

    public VistaTexto() {
        this.entrada = new Scanner(System.in);
    }

    @Override
    public String pedirTexto() {
        System.out.println("Escribe el texto que quieres guardar y pulsa Intro:");
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

    // Cierra el Scanner y la entrada estándar al finalizar el uso de la vista. 
    @Override
    public void close() {
        entrada.close();
    }
}
