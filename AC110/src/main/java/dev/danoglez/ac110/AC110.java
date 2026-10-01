package dev.danoglez.ac110;

public class AC110 {

    public static void main(String[] args) {
        String ruta = args.length > 0
                ? args[0]
                : "src/main/java/dev/danoglez/ac110/AC110.java";

        LectorLineas lector = new LectorLineas(ruta);

        try {
            System.out.print(lector.leer());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
