package dev.danoglez.ficherosaleatorios;

import dev.danoglez.ficherosaleatorios.controllers.Controlador;
import dev.danoglez.ficherosaleatorios.repositories.EmpleadoRepositorio;
import dev.danoglez.ficherosaleatorios.views.Vista;
import dev.danoglez.ficherosaleatorios.views.VistaTexto;

public class FicherosAleatorios {

    public static void main(String[] args) {
        // El fichero se crea en el directorio del proyecto
        EmpleadoRepositorio modelo = new EmpleadoRepositorio("empleados.dat");

        // Vista vista = new VistaTexto();
        // Vista vista = new VistaGrafica();

        try (Vista vista = new VistaTexto()) {
            Controlador controlador = new Controlador(modelo, vista);
            controlador.ejecutar();
        }
    }
}
