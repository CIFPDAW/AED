package dev.danoglez.ac13;

import java.util.Scanner;

public class AC13 {

    public static void main(String[] args) throws Exception {
        Directory directorio = new Directory("/home/danoglez/Documentos");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("1.- Mostrar el contenido del directorio");
            System.out.println("2.- Mostrar todo el arbol de directorios");
            System.out.print("Opcion: ");

            String opcion = scanner.nextLine().trim();
            if (opcion.equals("2")) {
                System.out.println(directorio.obtenerArbolDeDirectorios());
            } else {
                System.out.println(directorio.obtenerTodosLosElementosDelDirectorio());
            }
        }
    }
}
