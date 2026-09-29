package dev.danoglez.ac17.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.7. FileWriter sin el segundo argumento sobrescribe el fichero si ya existe.
 */
public class EscritorTexto {

    // Variables
    private final File fichero;

    // Constructores
    public EscritorTexto(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public void escribir(String texto) throws IOException {
        assert texto != null : "El texto no puede ser nulo";

        try (FileWriter writer = new FileWriter(fichero, StandardCharsets.UTF_8)) {
            writer.write(texto);
        }
    }
}
