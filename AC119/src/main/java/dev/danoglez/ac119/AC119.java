package dev.danoglez.ac119;

public class AC119 {

    public static void main(String[] args) {
        DiferenciaFicheros diferencia = new DiferenciaFicheros("input.txt", "borrar.txt", "output.txt");

        try {
            diferencia.guardarDiferencia();
            System.out.println("Diferencia guardada en output.txt");
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
