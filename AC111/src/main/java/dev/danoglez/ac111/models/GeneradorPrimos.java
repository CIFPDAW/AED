package dev.danoglez.ac111.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * AC1.11. Escribe los primos entre 1 y 500, un número por línea, con BufferedWriter.
 */
public class GeneradorPrimos {

    // Variables
    private final File fichero;

    // Constructores
    public GeneradorPrimos(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public int guardar(int desde, int hasta) throws IOException {
        int cantidad = 0;

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fichero, StandardCharsets.UTF_8))) {
            for (int numero = desde; numero <= hasta; numero++) {
                if (esPrimo(numero)) {
                    writer.write(Integer.toString(numero));
                    writer.newLine();
                    cantidad++;
                }
            }
        }

        return cantidad;
    }

    private boolean esPrimo(int numero) {
        if (numero < 2) {
            return false;
        }
        for (int divisor = 2; divisor * divisor <= numero; divisor++) {
            if (numero % divisor == 0) {
                return false;
            }
        }
        return true;
    }
}
