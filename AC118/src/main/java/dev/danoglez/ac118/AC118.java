package dev.danoglez.ac118;

import java.io.File;

public class AC118 {

    public static void main(String[] args) {
        if (args.length < 2 || args[0].equals("-h")) {
            mostrarAyuda();
            return;
        }

        File fichero = new File(args[0]);
        String opcion = args[1];
        CifradorTexto cifrador = new CifradorTexto();

        try {
            if (opcion.equals("-e")) {
                System.out.println("Fichero encriptado: " + cifrador.encriptar(fichero).getPath());
            } else if (opcion.equals("-d")) {
                System.out.println("Fichero desencriptado: " + cifrador.desencriptar(fichero).getPath());
            } else {
                mostrarAyuda();
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

    private static void mostrarAyuda() {
        System.out.println("Uso: java dev.danoglez.ac118.AC118 <fichero> <opcion>");
        System.out.println();
        System.out.println("  -e  Encripta el fichero y crea otro con extension .enc");
        System.out.println("  -d  Desencripta el fichero .enc y recupera el .txt");
        System.out.println("  -h  Muestra esta ayuda y sale");
    }
}
