package dev.danoglez.ac115.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AC1.15. Ordena alfabéticamente las palabras de un fichero (una por línea).
 * El resultado se llama como el original más la coletilla sort: palabras_sort.txt.
 */
public class OrdenadorPalabras {

    // Variables
    private final File origen;

    // Constructores
    public OrdenadorPalabras(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.origen = new File(ruta);
    }

    // Metodos
    public File ordenar() throws IOException {
        List<String> palabras = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(origen, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.isBlank()) {
                    palabras.add(linea);
                }
            }
        }

        Collections.sort(palabras);

        File destino = new File(origen.getParentFile(), nombreOrdenado(origen.getName()));
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(destino, StandardCharsets.UTF_8))) {
            for (String palabra : palabras) {
                writer.write(palabra);
                writer.newLine();
            }
        }

        return destino;
    }

    private String nombreOrdenado(String nombre) {
        int punto = nombre.lastIndexOf('.');
        if (punto > 0) {
            return nombre.substring(0, punto) + "_sort" + nombre.substring(punto);
        }
        return nombre + "_sort.txt";
    }
}
