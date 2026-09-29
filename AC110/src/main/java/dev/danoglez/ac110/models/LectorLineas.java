package dev.danoglez.ac110.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.10. Lectura línea a línea con BufferedReader.readLine(), apartado 5.2.
 * readLine() devuelve null al llegar al final del fichero.
 */
public class LectorLineas {

    // Variables
    private final File fichero;

    // Constructores
    public LectorLineas(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public String leer() throws IOException {
        StringBuilder contenido = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append(System.lineSeparator());
            }
        }

        return contenido.toString();
    }
}
