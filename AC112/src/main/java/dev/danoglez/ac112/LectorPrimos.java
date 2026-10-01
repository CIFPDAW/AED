package dev.danoglez.ac112;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.12. Lee primos.txt, el fichero creado en el ejercicio anterior, línea a línea.
 */
public class LectorPrimos {

    // Variables
    private final File fichero;

    // Constructores
    public LectorPrimos(String ruta) {
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
