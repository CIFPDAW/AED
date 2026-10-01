package dev.danoglez.ac18;

import java.util.Scanner;

public class AC18 {

    public static void main(String[] args) {
        RegistroNombres registro = new RegistroNombres("nombres.txt");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Escribe nombres. Para terminar, escribe salir.");

            while (true) {
                System.out.print("Nombre: ");
                String nombre = scanner.nextLine().trim();

                if (nombre.equalsIgnoreCase("salir")) {
                    break;
                }
                if (nombre.isEmpty()) {
                    continue;
                }

                registro.anadirNombre(nombre);
                System.out.println("Nombre añadido.");
            }
        } catch (Exception e) {
            System.err.println("Error de escritura: " + e.getMessage());
        }
    }
}
