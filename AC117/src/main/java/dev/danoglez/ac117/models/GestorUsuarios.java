package dev.danoglez.ac117.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * AC1.17. La opción 2 sobrescribe el fichero con el ArrayList.
 * La opción 1 vuelve a cargar ese fichero en el ArrayList.
 */
public class GestorUsuarios {

    // Variables
    private final File fichero;
    private final ArrayList<Usuario> usuarios = new ArrayList<>();

    // Constructores
    public GestorUsuarios(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public void cargar() throws IOException {
        usuarios.clear();
        if (!fichero.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.isBlank()) {
                    usuarios.add(Usuario.desdeLinea(linea));
                }
            }
        }
    }

    public void guardar() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fichero, StandardCharsets.UTF_8))) {
            for (Usuario usuario : usuarios) {
                writer.write(usuario.aLinea());
                writer.newLine();
            }
        }
    }

    public void anadir(Usuario usuario) {
        usuarios.add(usuario);
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
