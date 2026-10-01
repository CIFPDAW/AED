package dev.danoglez.ac127;

import dev.danoglez.ac127.controllers.Controlador;
import dev.danoglez.ac127.models.EmpleadoAccesoAleatorioRepositorio;
import dev.danoglez.ac127.views.Vista;
import dev.danoglez.ac127.views.VistaTexto;

public class Main {
    public static void main(String[] args) {
        EmpleadoAccesoAleatorioRepositorio modelo = new EmpleadoAccesoAleatorioRepositorio("empleados.dat");

        // Vista vista = new VistaTexto();
        // Vista vista = new VistaGrafica();

        try (Vista vista = new VistaTexto()) {
            Controlador controlador = new Controlador(modelo, vista);
            controlador.ejecutar();
        }
    }
}
