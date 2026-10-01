package dev.danoglez.ac113;

public class AC113 {

    public static void main(String[] args) {
        AcumuladorPrimos acumulador = new AcumuladorPrimos("primos.txt");

        try {
            System.out.println(acumulador.calcularYAnadir());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
