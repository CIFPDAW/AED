package com.mycompany.gamehub.view;

import java.util.Scanner;

/**
 * VISTA: lo único que hace es mostrar cosas por consola y pedir datos.
 * No sabe nada del XML ni de la lista de videojuegos.
 *
 * Todos los métodos leen la línea entera con nextLine() y luego la convierten.
 * Así se evitan los típicos problemas de Scanner con el salto de línea.
 */
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
        return scanner.nextLine().trim();
    }

    @Override
    public int pedirNumero(String texto) {
        while (true) {
            String linea = pedirTexto(texto);
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    @Override
    public double pedirDouble(String texto) {
        while (true) {
            // Se acepta tanto 45.5 como 45,5
            String linea = pedirTexto(texto).replace(',', '.');
            try {
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido (por ejemplo 45.5).");
            }
        }
    }

    @Override
    public boolean pedirBooleano(String texto) {
        while (true) {
            String linea = pedirTexto(texto).toLowerCase();

            if (linea.equals("true") || linea.equals("s") || linea.equals("si") || linea.equals("sí")) {
                return true;
            }
            if (linea.equals("false") || linea.equals("n") || linea.equals("no")) {
                return false;
            }
            System.out.println("Introduce true/false (o s/n).");
        }
    }

    @Override
    public boolean pedirConfirmacion(String texto) {
        while (true) {
            String respuesta = pedirTexto(texto + " (s/n): ").toLowerCase();

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
        System.out.println("         GAMEHUB XML");
        System.out.println("==============================");
        System.out.println("1. Cargar datos desde XML");
        System.out.println("2. Añadir videojuego");
        System.out.println("3. Buscar videojuego por ID");
        System.out.println("4. Mostrar todos los videojuegos");
        System.out.println("5. Modificar videojuego");
        System.out.println("6. Eliminar videojuego");
        System.out.println("7. Guardar cambios en XML");
        System.out.println("8. Buscar por ID directamente en el XML (XPath)");
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
