package com.mycompany.gamehub.view;

import java.util.Scanner;

public class GameHubView implements IGameHubView {

    // Variable
    private final Scanner scanner;

    
    // Constructor
    public GameHubView() {
        this.scanner = new Scanner(System.in);
    }

    // Metodos
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

    @Override
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

    @Override
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

    @Override
    public boolean pedirConfirmacion(String texto) {

        while (true) {

            System.out.print(texto + " (s/n): ");

            String respuesta = scanner.nextLine().trim().toLowerCase();

            if (respuesta.equals("s")) {
                return true;
            }

            if (respuesta.equals("n")) {
                return false;
            }

            System.out.println("Introduce 's' para sí o 'n' para no.");
        }
    }

    @Override
    public void mostrarTexto(String texto) {
        System.out.println(texto);
    }

    @Override
    public void mostrarMenu() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("        GAMEHUB - UT1");
        System.out.println("==============================");
        System.out.println("1. Cargar datos desde fichero de texto");
        System.out.println("2. Cargar datos desde fichero binario");
        System.out.println("3. Añadir videojuego");
        System.out.println("4. Buscar videojuego por ID");
        System.out.println("5. Mostrar todos los videojuegos");
        System.out.println("6. Modificar videojuego");
        System.out.println("7. Eliminar videojuego");
        System.out.println("8. Guardar datos en fichero de texto");
        System.out.println("9. Guardar datos en fichero binario");
        System.out.println("10. Sincronizar TXT y DAT");
        System.out.println("0. Salir");
    }

    @Override
    public int pedirOpcion() {
        mostrarMenu();
        return pedirNumero("Seleccione una opción: ");
    }

    @Override
    public void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    @Override
    public void close() {
        scanner.close();
    }
}
