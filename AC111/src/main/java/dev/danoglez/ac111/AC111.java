package dev.danoglez.ac111;

public class AC111 {

    public static void main(String[] args) {
        GeneradorPrimos generador = new GeneradorPrimos("primos.txt");

        try {
            int cantidad = generador.guardar(1, 500);
            System.out.println("Se han guardado " + cantidad + " primos en primos.txt");
        } catch (Exception e) {
            System.err.println("Error de escritura: " + e.getMessage());
        }
    }
}
