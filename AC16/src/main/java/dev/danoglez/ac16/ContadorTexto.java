package dev.danoglez.ac16;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.6. Cuenta líneas y espacios recorriendo el fichero carácter a carácter.
 */
public class ContadorTexto {

    // Variables
    private final File fichero;

    // Constructores
    public ContadorTexto(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public String contar() throws IOException {
        int lineas = 0;
        int espacios = 0;
        boolean hayContenido = false;
        boolean ultimaEsSalto = false;

        try (FileReader reader = new FileReader(fichero, StandardCharsets.UTF_8)) {
            int dato;
            while ((dato = reader.read()) != -1) {
                hayContenido = true;
                char caracter = (char) dato;

                if (caracter == ' ') {
                    espacios++;
                }

                if (caracter == '\n') {
                    lineas++;
                    ultimaEsSalto = true;
                } else if (caracter != '\r') {
                    ultimaEsSalto = false;
                }
            }
        }

        if (hayContenido && !ultimaEsSalto) {
            lineas++;
        }

        return new StringBuilder()
                .append("Fichero: ").append(fichero.getName()).append("\n")
                .append("Lineas: ").append(lineas).append("\n")
                .append("Espacios en blanco: ").append(espacios).append("\n")
                .toString();
    }
}
