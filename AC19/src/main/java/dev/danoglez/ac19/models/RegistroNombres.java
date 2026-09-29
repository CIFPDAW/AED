package dev.danoglez.ac19.models;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.9. Misma idea que el ejercicio anterior, pero solo con FileReader y FileWriter.
 * Antes de añadir, se lee el fichero carácter a carácter y se comprueba la línea.
 */
public class RegistroNombres {

    // Variables
    private final File fichero;

    // Constructores
    public RegistroNombres(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public boolean existeNombre(String nombre) throws IOException {
        if (!fichero.exists() || fichero.length() == 0) {
            return false;
        }

        StringBuilder contenido = new StringBuilder();
        try (FileReader reader = new FileReader(fichero, StandardCharsets.UTF_8)) {
            int dato;
            while ((dato = reader.read()) != -1) {
                contenido.append((char) dato);
            }
        }

        for (String linea : contenido.toString().split("\\R")) {
            if (linea.trim().equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }

    public void anadirNombre(String nombre) throws IOException {
        assert nombre != null : "El nombre no puede ser nulo";

        try (FileWriter writer = new FileWriter(fichero, StandardCharsets.UTF_8, true)) {
            writer.write(nombre.trim());
            writer.write(System.lineSeparator());
        }
    }
}
