package dev.danoglez.ac123;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * AC1.23. Lee numerosArmstrong.dat con DataInputStream y muestra solo los pares.
 * El bucle termina con EOFException, como en el ejemplo de lectura del apartado 6.2.
 */
public class LectorArmstrong {

    // Variables
    private final File fichero;

    // Constructores
    public LectorArmstrong(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public String leerPares() throws IOException {
        StringBuilder salida = new StringBuilder();

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(fichero)))) {
            while (true) {
                int numero = in.readInt();
                if (numero % 2 == 0) {
                    salida.append(numero).append(System.lineSeparator());
                }
            }
        } catch (EOFException fin) {
            salida.append("Fin de fichero.");
        }

        return salida.toString();
    }
}
