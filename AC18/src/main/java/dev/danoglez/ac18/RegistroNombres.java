package dev.danoglez.ac18;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * AC1.8. new FileWriter(fichero, true) añade al final, como indica el apartado 5.1.
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
    public void anadirNombre(String nombre) throws IOException {
        assert nombre != null : "El nombre no puede ser nulo";

        try (FileWriter writer = new FileWriter(fichero, true)) {
            writer.write(nombre);
            writer.write(System.lineSeparator());
        }
    }
}
