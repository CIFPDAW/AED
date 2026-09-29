package com.cifp.controllers;

import com.cifp.models.FicheroTexto;
import com.cifp.views.Vista;
import java.io.IOException;

/** Controlador: coordina las operaciones del modelo y la interacción con la vista. */
public class Controlador {
    private final FicheroTexto modelo;
    private final Vista vista;

    public Controlador(FicheroTexto modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void ejecutar() {
        String texto = vista.pedirTexto();
        if (texto == null) {
            return;
        }

        try {
            vista.mostrarTexto("\nEscribiendo fichero");
            modelo.escribir(texto);

            vista.mostrarTexto("\nLeyendo fichero");
            String contenido = modelo.leer();
            vista.mostrarTexto(contenido);
        } catch (IOException e) {
            vista.mostrarError("No se ha podido escribir o leer el fichero: " + e.getMessage());
        }

        vista.mostrarTexto("----- Fin del Programa -----");
    }
}
