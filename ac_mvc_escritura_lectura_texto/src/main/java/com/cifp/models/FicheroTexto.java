package com.cifp.models;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Modelo: se encarga de escribir y leer el fichero, sin interactuar con el usuario.
 */
public class FicheroTexto {
    private final File fichero;

    public FicheroTexto(String ruta) {
        assert ruta != null;
        assert !ruta.isBlank();

        fichero = new File(ruta);
    }

    public void escribir(String texto) throws IOException {
        try (FileWriter escritor = new FileWriter(fichero)) {
            escritor.write(texto);
        }
    }

    public String leer() throws IOException {
        StringBuilder contenido = new StringBuilder();
        
        try (FileReader lector = new FileReader(fichero)) {
            int caracter;
            while ((caracter = lector.read()) != -1) {
                contenido.append((char) caracter);
            }
        }
        return contenido.toString();
    }

}
