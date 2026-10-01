package dev.danoglez.ac126;

import java.util.Scanner;

public class AC126 {

    public static void main(String[] args) {
        EmpleadoAccesoAleatorioRepositorio repositorio = new EmpleadoAccesoAleatorioRepositorio("empleados.dat");

        try (Scanner scanner = new Scanner(System.in)) {
            boolean seguir = true;

            while (seguir) {
                System.out.println("1.- Escribir empleado");
                System.out.println("2.- Listar empleados");
                System.out.println("3.- Modificar empleado");
                System.out.println("4.- Salir");
                System.out.print("Opcion: ");
                String opcion = scanner.nextLine().trim();

                if (opcion.equals("1")) {
                    escribir(scanner, repositorio);
                } else if (opcion.equals("2")) {
                    listar(repositorio);
                } else if (opcion.equals("3")) {
                    modificar(scanner, repositorio);
                } else if (opcion.equals("4")) {
                    seguir = false;
                } else {
                    System.out.println("Opcion no valida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

    private static void escribir(Scanner scanner, EmpleadoAccesoAleatorioRepositorio repositorio) throws Exception {
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        System.out.print("Departamento: ");
        int departamento = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Salario: ");
        double salario = Double.parseDouble(scanner.nextLine().trim());

        Empleado empleado = repositorio.escribir(apellido, departamento, salario);
        System.out.println("Empleado escrito: " + empleado);
    }

    private static void listar(EmpleadoAccesoAleatorioRepositorio repositorio) throws Exception {
        for (Empleado empleado : repositorio.listar()) {
            System.out.println(empleado);
        }
    }

    private static void modificar(Scanner scanner, EmpleadoAccesoAleatorioRepositorio repositorio) throws Exception {
        System.out.print("Id: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        System.out.print("Departamento: ");
        int departamento = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Salario: ");
        double salario = Double.parseDouble(scanner.nextLine().trim());

        if (repositorio.modificar(id, apellido, departamento, salario)) {
            System.out.println("Empleado modificado.");
        } else {
            System.out.println("No existe un empleado con ese id.");
        }
    }
}
