package com.mycompany.ac12;

import java.io.File;
import java.util.Scanner;

public class Fichero {
    public static void AC12Ficheros() {
        // Creamos un objeto Scanner para leer del terminal dentro de un try catch para controlar errores
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("\nSe inicio el programa para localizar el archivo");
            System.out.println("Escriba la ruta del archivo o directorio");

            // Creamos una variable String para leer la siguiente linea de la terminal
            String inputRuta = scanner.nextLine();

            // Crea un archivo si no existe y si existe lo instancia
            File archivo = new File(inputRuta);

            System.out.println("\n--- RESULTADO ---\n");

            // Creamos una variable boleana para comprobar si existe a traves de un ternario
            boolean existe = archivo.exists();
            System.out.println("Existe: " + (existe ? "Si" : "No"));

            // Si existe miramos las propiedades
            if (existe) {
                // Mostrar nombre del archivo
                System.out.println("Nombre: " + archivo.getName());

                // Muestra si se puede leer o no
                System.out.println("Se puede leer: " + (archivo.canRead() ? "Si" : "No"));

                // Muestra el tamaño del archivo
                System.out.println("Tamaño: " + archivo.length() + " bytes");

                // Comprueba si es un archivo o un directorio
                if (archivo.isDirectory()) {
                    System.out.println("Tipo: Directorio");

                } else if (archivo.isFile()) {
                    System.out.println("Tipo: Fichero");
                }

                // Comprobamos si la ruta proporcionada es absoluta o relativa
                System.out.println((archivo.isAbsolute() ? "Es una ruta absoluta" : "Es una ruta relativa") + " (" + archivo.getAbsolutePath() + ")");
            }

        } catch (Exception e) {

            System.out.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
