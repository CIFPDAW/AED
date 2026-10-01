package dev.danoglez.ac114;

public class AC114 {

    public static void main(String[] args) {
        MezcladorFicheros mezclador = new MezcladorFicheros("fichero1.txt", "fichero2.txt", "mezcla.txt");

        try {
            mezclador.mezclar();
            System.out.println("Lineas mezcladas en mezcla.txt");
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
