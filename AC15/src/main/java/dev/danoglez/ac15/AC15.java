package dev.danoglez.ac15;

public class AC15 {

    public static void main(String[] args) {
        String ruta = args.length > 0
                ? args[0]
                : "src/main/java/dev/danoglez/ac15/AC15.java";

        LectorCaracteres lector = new LectorCaracteres(ruta);

        try {
            System.out.println(lector.leer());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
