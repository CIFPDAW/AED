package dev.danoglez.ac120.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * AC1.20. Copia el fichero byte a byte con el mismo bucle del apartado 6.1
 * y comprueba el tamaño con Files.size().
 */
public class CopiadorBinario {

    // Variables
    private final File origen;
    private final File destino;

    // Constructores
    public CopiadorBinario(String rutaOrigen, String rutaDestino) {
        this.origen = new File(rutaOrigen);
        this.destino = new File(rutaDestino);
    }

    // Metodos
    public String copiarYComprobar() throws IOException {
        try (InputStream in = new FileInputStream(origen);
                OutputStream out = new FileOutputStream(destino)) {
            byte[] buffer = new byte[8192];
            int leidos;
            while ((leidos = in.read(buffer)) != -1) {
                out.write(buffer, 0, leidos);
            }
        }

        long tamanoOrigen = Files.size(origen.toPath());
        long tamanoCopia = Files.size(destino.toPath());
        boolean coinciden = tamanoOrigen == tamanoCopia;

        return new StringBuilder()
                .append("Original: ").append(tamanoOrigen).append(" bytes\n")
                .append("Copia: ").append(tamanoCopia).append(" bytes\n")
                .append("Coinciden: ").append(coinciden ? "Si" : "No").append("\n")
                .toString();
    }
}
