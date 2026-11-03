package dev.danoglez.gamehubxml.views;

import java.util.Locale;
import java.util.Scanner;

/** Menu y lectura de teclado. No abre el XML. */
public class VistaTexto implements Vista {

    // Variables
    private final Scanner entrada;

    // Constructores
    public VistaTexto() {
        // Locale.US para aceptar decimales con punto (45.5)
        this.entrada = new Scanner(System.in).useLocale(Locale.US);
    }

    // Metodos
    @Override
    public String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.hasNextLine() ? entrada.nextLine() : "";
    }

    @Override
    public int pedirNumero(String mensaje) {
        System.out.print(mensaje);
        while (!entrada.hasNextInt()) {
            System.out.println("Introduce un numero valido.");
            entrada.nextLine();
            System.out.print(mensaje);
        }
        int numero = entrada.nextInt();
        entrada.nextLine();
        return numero;
    }

    @Override
    public double pedirDouble(String mensaje) {
        System.out.print(mensaje);
        while (!entrada.hasNextDouble()) {
            System.out.println("Introduce un numero valido.");
            entrada.nextLine();
            System.out.print(mensaje);
        }
        double numero = entrada.nextDouble();
        entrada.nextLine();
        return numero;
    }

    @Override
    public boolean pedirBooleano(String mensaje) {
        System.out.print(mensaje);
        while (!entrada.hasNextBoolean()) {
            System.out.println("Introduce true o false.");
            entrada.nextLine();
            System.out.print(mensaje);
        }
        boolean valor = entrada.nextBoolean();
        entrada.nextLine();
        return valor;
    }

    @Override
    public boolean pedirConfirmacion(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String respuesta = entrada.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Introduce s o n.");
        }
    }

    @Override
    public void mostrarTexto(String texto) {
        System.out.println(texto);
    }

    @Override
    public void mostrarError(String mensaje) {
        System.err.println("Error: " + mensaje);
    }

    @Override
    public int pedirOpcion() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("         GAMEHUB XML");
        System.out.println("==============================");
        System.out.println("1. Cargar datos desde XML");
        System.out.println("2. Anadir videojuego");
        System.out.println("3. Buscar videojuego por ID");
        System.out.println("4. Mostrar todos los videojuegos");
        System.out.println("5. Modificar videojuego");
        System.out.println("6. Eliminar videojuego");
        System.out.println("7. Guardar cambios en XML");
        System.out.println("0. Salir");
        return pedirNumero("Seleccione una opcion: ");
    }

    @Override
    public void close() {
        entrada.close();
    }
}
