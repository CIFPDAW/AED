package dev.danoglez.ac18.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.8. El tercer argumento true de FileWriter abre el fichero en modo añadir,
 * para seguir escribiendo al volver a ejecutar el programa.
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

        try (FileWriter writer = new FileWriter(fichero, StandardCharsets.UTF_8, true)) {
            writer.write(nombre);
            writer.write(System.lineSeparator());
        }
    }
}
