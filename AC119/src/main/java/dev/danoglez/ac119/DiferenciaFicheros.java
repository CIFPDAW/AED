package dev.danoglez.ac119;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * AC1.19. Guarda en output.txt las líneas de input.txt que no están en borrar.txt.
 */
public class DiferenciaFicheros {

    // Variables
    private final File entrada;
    private final File borrar;
    private final File salida;

    // Constructores
    public DiferenciaFicheros(String rutaEntrada, String rutaBorrar, String rutaSalida) {
        this.entrada = new File(rutaEntrada);
        this.borrar = new File(rutaBorrar);
        this.salida = new File(rutaSalida);
    }

    // Metodos
    public void guardarDiferencia() throws IOException {
        ArrayList<String> descartadas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(borrar, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                descartadas.add(linea);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(entrada, StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new FileWriter(salida, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!descartadas.contains(linea)) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
        }
    }
}
