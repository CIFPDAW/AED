package dev.danoglez.ac123;

public class AC123 {

    public static void main(String[] args) {
        LectorArmstrong lector = new LectorArmstrong("numerosArmstrong.dat");

        try {
            System.out.println(lector.leerPares());
        } catch (Exception e) {
            System.err.println("Error de lectura: " + e.getMessage());
        }
    }
}
