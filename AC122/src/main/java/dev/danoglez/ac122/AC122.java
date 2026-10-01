package dev.danoglez.ac122;

public class AC122 {

    public static void main(String[] args) {
        GeneradorArmstrong generador = new GeneradorArmstrong("numerosArmstrong.dat");

        try {
            int cantidad = generador.guardar(1, 1000);
            System.out.println("Se han guardado " + cantidad + " numeros en numerosArmstrong.dat");
        } catch (Exception e) {
            System.err.println("Error de escritura: " + e.getMessage());
        }
    }
}
