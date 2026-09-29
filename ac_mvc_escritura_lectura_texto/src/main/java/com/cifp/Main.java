package com.cifp;

import com.cifp.controllers.Controlador;
import com.cifp.models.FicheroTexto;
import com.cifp.views.Vista;
import com.cifp.views.VistaGrafica;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path rutaFichero = Path.of("src", "main", "resources", "fichero.txt");
        
        FicheroTexto modelo = new FicheroTexto(rutaFichero.toString());

        // Vista vista = new VistaTexto();
        // Vista vista = new VistaGrafica();

        try (Vista vista = new VistaGrafica()) {
            Controlador controlador = new Controlador(modelo, vista);
            controlador.ejecutar();
        }
    }
}
