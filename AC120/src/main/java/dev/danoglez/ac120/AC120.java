package dev.danoglez.ac120;

public class AC120 {

    public static void main(String[] args) {
        CopiadorBinario copiador = new CopiadorBinario("imagen.jpg", "imagen_copia.jpg");

        try {
            System.out.println(copiador.copiarYComprobar());
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
