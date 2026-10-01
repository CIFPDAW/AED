package dev.danoglez.ac15;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.5. Lectura carácter a carácter con FileReader, como el ejemplo del apartado 5.1.
 * read() devuelve int para poder representar el -1 de fin de flujo.
 */
public class LectorCaracteres {

    // Variables
    private final File fichero;

    // Constructores
    public LectorCaracteres(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public String leer() throws IOException {
        StringBuilder contenido = new StringBuilder();

        try (FileReader reader = new FileReader(fichero, StandardCharsets.UTF_8)) {
            int dato;
            while ((dato = reader.read()) != -1) {
                contenido.append((char) dato);
            }
        }

        return contenido.toString();
    }
}
