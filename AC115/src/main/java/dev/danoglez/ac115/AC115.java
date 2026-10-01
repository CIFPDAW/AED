package dev.danoglez.ac115;

public class AC115 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java dev.danoglez.ac115.AC115 <fichero>");
            System.out.println("Ejemplo: java dev.danoglez.ac115.AC115 palabras.txt");
            return;
        }

        OrdenadorPalabras ordenador = new OrdenadorPalabras(args[0]);

        try {
            System.out.println("Fichero ordenado: " + ordenador.ordenar().getPath());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
