package dev.danoglez.ac16;

public class AC16 {

    public static void main(String[] args) {
        String ruta = args.length > 0
                ? args[0]
                : "src/main/java/dev/danoglez/ac16/AC16.java";

        ContadorTexto contador = new ContadorTexto(ruta);

        try {
            System.out.println(contador.contar());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
