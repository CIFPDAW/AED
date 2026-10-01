package dev.danoglez.ac117;

import java.util.Scanner;

public class AC117 {

    public static void main(String[] args) {
        GestorUsuarios gestor = new GestorUsuarios("usuarios.txt");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean seguir = true;

            while (seguir) {
                System.out.println("1.- Cargar datos del fichero");
                System.out.println("2.- Guardar los datos en el fichero");
                System.out.println("3.- Añadir un nuevo Usuario");
                System.out.println("4.- Mostrar los datos de los usuarios");
                System.out.println("5.- Salir");
                System.out.print("Opcion: ");
                String opcion = scanner.nextLine().trim();

                if (opcion.equals("1")) {
                    gestor.cargar();
                    System.out.println("Datos cargados: " + gestor.getUsuarios().size());
                } else if (opcion.equals("2")) {
                    gestor.guardar();
                    System.out.println("Datos guardados.");
                } else if (opcion.equals("3")) {
                    anadirUsuario(scanner, gestor);
                } else if (opcion.equals("4")) {
                    mostrarUsuarios(gestor);
                } else if (opcion.equals("5")) {
                    seguir = false;
                } else {
                    System.out.println("Opcion no valida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

    private static void anadirUsuario(Scanner scanner, GestorUsuarios gestor) {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("NIF: ");
        String nif = scanner.nextLine().trim();

        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine().trim());

        gestor.anadir(new Usuario(nombre, nif, edad));
        System.out.println("Usuario añadido al ArrayList.");
    }

    private static void mostrarUsuarios(GestorUsuarios gestor) {
        if (gestor.getUsuarios().isEmpty()) {
            System.out.println("No hay usuarios en memoria.");
            return;
        }
        for (Usuario usuario : gestor.getUsuarios()) {
            System.out.println(usuario);
            System.out.println();
        }
    }
}
