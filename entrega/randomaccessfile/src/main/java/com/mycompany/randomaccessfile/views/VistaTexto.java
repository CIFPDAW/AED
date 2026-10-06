package com.mycompany.randomaccessfile.views;

import com.mycompany.objectinputstreamandobjectoutputstream.views.Vista;
import java.util.Scanner;

public class VistaTexto implements Vista {

    // Variables
    private final Scanner scanner;

    // Constructor
    public VistaTexto() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String pedirTexto(String texto) {
        System.out.print(texto);
        return scanner.nextLine();
    }

    @Override
    public int pedirNumero(String texto) {
        System.out.print(texto);

        while (!scanner.hasNextInt()) {
            System.out.println("Introduce un número válido.");
            scanner.nextLine();
            System.out.print(texto);
        }

        int numero = scanner.nextInt();
        scanner.nextLine();

        return numero;
    }

    // Pedir un numero double
    public double pedirDouble(String texto) {
        System.out.print(texto);

        while (!scanner.hasNextDouble()) {
            System.out.println("Introduce un número válido.");
            scanner.nextLine();
            System.out.print(texto);
        }

        double numero = scanner.nextDouble();
        scanner.nextLine();

        return numero;
    }
    
    // Pedir boolean
    public boolean pedirBooleano(String texto) {
        System.out.print(texto);

        while (!scanner.hasNextBoolean()) {
            System.out.println("Introduce true o false.");
            scanner.nextLine();
            System.out.print(texto);
        }

        boolean valor = scanner.nextBoolean();
        scanner.nextLine();

        return valor;
    }

    // Mostrar texto
    @Override
    public void mostrarTexto(String texto) {
        System.out.println(texto);
    }

    // Mostrar menú
    public void mostrarMenu() {
        System.out.println("\n===== MENÚ =====");
        System.out.println("1. Listar empleados");
        System.out.println("2. Añadir empleado");
        System.out.println("3. Modificar empleado");
        System.out.println("4. Buscar empleado por ID");
        System.out.println("5. Salir");
    }

    // Pedir opción del menú
    public int pedirOpcion() {
        mostrarMenu();
        return pedirNumero("Selecciona una opción: ");
    }

    // Mostrar mensaje de error
    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    // Cerrar Scanner
    @Override
    public void close() {
        scanner.close();
    }
}
