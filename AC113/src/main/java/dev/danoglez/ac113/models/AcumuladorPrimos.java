package dev.danoglez.ac113.models;

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
 * AC1.13. Añade al final de primos.txt la suma alterna del ejemplo del PDF:
 * la línea 1 se suma, la 2 se resta, la 3 se suma, la 4 se resta...
 * Ejemplo del enunciado: 2 - 3 + 5 - 7 = -3.
 * El resultado se añade con FileWriter en modo append, apartado 5.3.
 */
public class AcumuladorPrimos {

    // Variables
    private final File fichero;

    // Constructores
    public AcumuladorPrimos(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public String calcularYAnadir() throws IOException {
        List<Integer> numeros = leerNumeros();
        String expresion = construirExpresion(numeros);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fichero, StandardCharsets.UTF_8, true))) {
            writer.write(expresion);
            writer.newLine();
        }

        return expresion;
    }

    private List<Integer> leerNumeros() throws IOException {
        List<Integer> numeros = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fichero, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String texto = linea.trim();
                if (texto.isEmpty() || texto.contains("=")) {
                    continue;
                }
                numeros.add(Integer.parseInt(texto));
            }
        }

        return numeros;
    }

    private String construirExpresion(List<Integer> numeros) {
        int resultado = 0;
        StringBuilder expresion = new StringBuilder();

        for (int i = 0; i < numeros.size(); i++) {
            int numero = numeros.get(i);
            boolean lineaImpar = i % 2 == 0;

            if (i == 0) {
                expresion.append(numero);
                resultado = numero;
            } else if (lineaImpar) {
                expresion.append(" + ").append(numero);
                resultado += numero;
            } else {
                expresion.append(" - ").append(numero);
                resultado -= numero;
            }
        }

        expresion.append(" = ").append(resultado);
        return expresion.toString();
    }
}
