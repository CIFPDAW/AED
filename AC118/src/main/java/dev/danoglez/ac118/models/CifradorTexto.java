package dev.danoglez.ac118.models;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.18. Encripta y desencripta transformando cada carácter al copiar el flujo,
 * que es la lectura/escritura carácter a carácter del apartado 5.1.
 * El desplazamiento es 3. -d aplica el desplazamiento contrario.
 */
public class CifradorTexto {

    private static final int DESPLAZAMIENTO = 3;

    // Metodos
    public File encriptar(File origen) throws IOException {
        File destino = new File(cambiarExtension(origen.getPath(), ".enc"));
        transformar(origen, destino, DESPLAZAMIENTO);
        return destino;
    }

    public File desencriptar(File origen) throws IOException {
        File destino = new File(cambiarExtension(origen.getPath(), ".txt"));
        transformar(origen, destino, -DESPLAZAMIENTO);
        return destino;
    }

    private void transformar(File origen, File destino, int desplazamiento) throws IOException {
        try (FileReader reader = new FileReader(origen, StandardCharsets.UTF_8);
                FileWriter writer = new FileWriter(destino, StandardCharsets.UTF_8)) {
            int dato;
            while ((dato = reader.read()) != -1) {
                writer.write((char) (dato + desplazamiento));
            }
        }
    }

    private String cambiarExtension(String ruta, String extension) {
        int punto = ruta.lastIndexOf('.');
        if (punto > 0) {
            return ruta.substring(0, punto) + extension;
        }
        return ruta + extension;
    }
}
