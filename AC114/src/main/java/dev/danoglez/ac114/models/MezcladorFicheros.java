package dev.danoglez.ac114.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.14. Mezcla las líneas de dos ficheros: una del primero, una del segundo,
 * y así sucesivamente. Si uno se acaba antes, se copian las líneas que queden del otro.
 */
public class MezcladorFicheros {

    // Variables
    private final File primero;
    private final File segundo;
    private final File destino;

    // Constructores
    public MezcladorFicheros(String rutaPrimero, String rutaSegundo, String rutaDestino) {
        assert rutaPrimero != null && rutaSegundo != null && rutaDestino != null;
        this.primero = new File(rutaPrimero);
        this.segundo = new File(rutaSegundo);
        this.destino = new File(rutaDestino);
    }

    // Metodos
    public void mezclar() throws IOException {
        try (BufferedReader lectorPrimero = new BufferedReader(new FileReader(primero, StandardCharsets.UTF_8));
                BufferedReader lectorSegundo = new BufferedReader(new FileReader(segundo, StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new FileWriter(destino, StandardCharsets.UTF_8))) {

            String lineaPrimero = lectorPrimero.readLine();
            String lineaSegundo = lectorSegundo.readLine();

            while (lineaPrimero != null || lineaSegundo != null) {
                if (lineaPrimero != null) {
                    writer.write(lineaPrimero);
                    writer.newLine();
                    lineaPrimero = lectorPrimero.readLine();
                }
                if (lineaSegundo != null) {
                    writer.write(lineaSegundo);
                    writer.newLine();
                    lineaSegundo = lectorSegundo.readLine();
                }
            }
        }
    }
}
