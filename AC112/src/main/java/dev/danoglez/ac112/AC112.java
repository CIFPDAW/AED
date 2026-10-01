package dev.danoglez.ac112;

public class AC112 {

    public static void main(String[] args) {
        LectorPrimos lector = new LectorPrimos("primos.txt");

        try {
            System.out.print(lector.leer());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
