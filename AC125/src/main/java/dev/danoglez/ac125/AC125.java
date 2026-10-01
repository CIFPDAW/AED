package dev.danoglez.ac125;

import java.io.File;
import java.util.Scanner;

public class AC125 {

    public static void main(String[] args) {
        File fichero = new File("clientes.dat");
        GestorClientes gestor = new GestorClientes(fichero);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean seguir = true;

            while (seguir) {
                System.out.println("1.- Añadir cliente");
                System.out.println("2.- Guardar clientes");
                System.out.println("3.- Recuperar clientes");
                System.out.println("4.- Consultar morosos");
                System.out.println("5.- Mostrar clientes");
                System.out.println("6.- Salir");
                System.out.print("Opcion: ");
                String opcion = scanner.nextLine().trim();

                if (opcion.equals("1")) {
                    anadirCliente(scanner, gestor);
                } else if (opcion.equals("2")) {
                    gestor.guardar();
                    System.out.println("Clientes guardados.");
                } else if (opcion.equals("3")) {
                    gestor.recuperar();
                    System.out.println("Clientes recuperados: " + gestor.getClientes().size());
                } else if (opcion.equals("4")) {
                    for (Cliente moroso : gestor.devolverMorosos(fichero)) {
                        System.out.println(moroso);
                    }
                } else if (opcion.equals("5")) {
                    for (Cliente cliente : gestor.getClientes()) {
                        System.out.println(cliente);
                    }
                } else if (opcion.equals("6")) {
                    seguir = false;
                } else {
                    System.out.println("Opcion no valida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

    private static void anadirCliente(Scanner scanner, GestorClientes gestor) {
        System.out.print("Codigo: ");
        int codigo = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Direccion: ");
        String direccion = scanner.nextLine().trim();

        System.out.print("Saldo: ");
        double saldo = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Cuenta (Al día, Atrasada, Deudor): ");
        String cuenta = normalizarCuenta(scanner.nextLine().trim());

        gestor.anadir(new Cliente(codigo, nombre, direccion, saldo, cuenta));
        System.out.println("Cliente añadido.");
    }

    private static String normalizarCuenta(String cuenta) {
        if (cuenta.equalsIgnoreCase("Al dia") || cuenta.equalsIgnoreCase("Al día")) {
            return "Al día";
        }
        if (cuenta.equalsIgnoreCase("Atrasada")) {
            return "Atrasada";
        }
        if (cuenta.equalsIgnoreCase("Deudor")) {
            return "Deudor";
        }
        throw new IllegalArgumentException("La cuenta debe ser Al día, Atrasada o Deudor");
    }
}
