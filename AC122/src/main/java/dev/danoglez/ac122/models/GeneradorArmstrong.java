package dev.danoglez.ac122.models;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * AC1.22. Guarda con DataOutputStream.writeInt los números de Armstrong entre 1 y 1000.
 * Un número de Armstrong es igual a la suma de sus dígitos elevados al número de cifras.
 */
public class GeneradorArmstrong {

    // Variables
    private final File fichero;

    // Constructores
    public GeneradorArmstrong(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public int guardar(int desde, int hasta) throws IOException {
        int cantidad = 0;

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fichero)))) {
            for (int numero = desde; numero <= hasta; numero++) {
                if (esArmstrong(numero)) {
                    out.writeInt(numero);
                    cantidad++;
                }
            }
        }

        return cantidad;
    }

    private boolean esArmstrong(int numero) {
        int cifras = String.valueOf(numero).length();
        int suma = 0;
        int resto = numero;

        while (resto > 0) {
            int digito = resto % 10;
            int potencia = 1;
            for (int i = 0; i < cifras; i++) {
                potencia *= digito;
            }
            suma += potencia;
            resto /= 10;
        }

        return suma == numero;
    }
}
