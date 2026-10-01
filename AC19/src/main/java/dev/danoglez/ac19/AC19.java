package dev.danoglez.ac19;

import java.util.Scanner;

public class AC19 {

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

                if (registro.existeNombre(nombre)) {
                    System.out.println("Ese nombre ya está en el fichero.");
                } else {
                    registro.anadirNombre(nombre);
                    System.out.println("Nombre añadido.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
