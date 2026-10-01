package dev.danoglez.ac116;

import java.util.List;
import java.util.Scanner;

public class AC116 {

    public static void main(String[] args) {
        GestorUsuarios gestor = new GestorUsuarios("usuarios.txt");

        try (Scanner scanner = new Scanner(System.in)) {
            gestor.prepararFichero();
            boolean seguir = true;

            while (seguir) {
                System.out.println("1.- Añadir un usuario.");
                System.out.println("2.- Mostrar todos los usuarios añadidos.");
                System.out.println("3.- Salir.");
                System.out.print("Opcion: ");
                String opcion = scanner.nextLine().trim();

                if (opcion.equals("1")) {
                    anadirUsuario(scanner, gestor);
                } else if (opcion.equals("2")) {
                    mostrarUsuarios(scanner, gestor);
                } else if (opcion.equals("3")) {
                    seguir = false;
                } else {
                    System.out.println("Opcion no valida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

    private static void anadirUsuario(Scanner scanner, GestorUsuarios gestor) throws Exception {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Videojuego: ");
        String videojuego = scanner.nextLine().trim();

        gestor.anadir(nombre, edad, videojuego);
        System.out.println("Usuario añadido.");
    }

    private static void mostrarUsuarios(Scanner scanner, GestorUsuarios gestor) throws Exception {
        List<String> usuarios = gestor.listarDatos();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios.");
            return;
        }

        for (String usuario : usuarios) {
            String[] campos = usuario.split(":", 3);
            System.out.println();
            System.out.println("Nombre: " + campos[0]);
            System.out.println();
            System.out.println("Edad: " + campos[1]);
            System.out.println();
            System.out.println("Videojuego: " + campos[2]);
            System.out.println();
            System.out.print("Pulsa s para el siguiente o n para no seguir: ");

            String respuesta = scanner.nextLine().trim();
            if (respuesta.equalsIgnoreCase("n")) {
                break;
            }
        }
    }
}
