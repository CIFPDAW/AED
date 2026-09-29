package dev.danoglez.ac116.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * AC1.16. Cada usuario es una línea nombre:edad:videojuego.
 * La primera línea del fichero es la cabecera nombre:edad:videojuego.
 */
public class GestorUsuarios {

    private static final String CABECERA = "nombre:edad:videojuego";

    // Variables
    private final File fichero;

    // Constructores
    public GestorUsuarios(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public void prepararFichero() throws IOException {
        if (fichero.exists() && fichero.length() > 0) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fichero, StandardCharsets.UTF_8))) {
            writer.write(CABECERA);
            writer.newLine();
        }
    }

    public void anadir(String nombre, int edad, String videojuego) throws IOException {
        prepararFichero();

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fichero, StandardCharsets.UTF_8, true))) {
            writer.write(nombre + ":" + edad + ":" + videojuego);
            writer.newLine();
        }
    }

    public List<String> listarDatos() throws IOException {
        List<String> usuarios = new ArrayList<>();
        if (!fichero.exists()) {
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = reader.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }
                if (!linea.isBlank()) {
                    usuarios.add(linea);
                }
            }
        }

        return usuarios;
    }
}
