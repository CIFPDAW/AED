package dev.danoglez.ac14;

import java.util.Scanner;

public class AC14 {

    public static void main(String[] args) {
        GestorProyecto gestor = new GestorProyecto("directorioAC14", "fichero.txt");

        try (Scanner scanner = new Scanner(System.in)) {
            gestor.crear();
            System.out.println("Se han creado el directorio y el fichero en el proyecto:");
            System.out.println(gestor.obtenerInformacion());

            System.out.print("¿Desea borrarlos? (s/n): ");
            String respuesta = scanner.nextLine().trim();

            if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("sí")) {
                gestor.borrar();
                System.out.println("Se han borrado.");
                System.out.println(gestor.obtenerInformacion());
            } else {
                System.out.println("Se mantienen el directorio y el fichero.");
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
