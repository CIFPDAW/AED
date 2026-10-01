package dev.danoglez.ac17;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * AC1.7. FileWriter sin append sobrescribe el fichero, como indica el apartado 5.1.
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

        try (FileWriter writer = new FileWriter(fichero)) {
            writer.write(texto);
        }
    }
}
