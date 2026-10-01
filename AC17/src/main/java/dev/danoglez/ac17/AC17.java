package dev.danoglez.ac17;

public class AC17 {

    public static void main(String[] args) {
        String texto = "En un lugar de la Mancha, de cuyo nombre no quiero acordarme, "
                + "no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, "
                + "adarga antigua, rocín flaco y galgo corredor.";

        EscritorTexto escritor = new EscritorTexto("quijote.txt");

        try {
            escritor.escribir(texto);
            System.out.println("Texto escrito en quijote.txt");
        } catch (Exception e) {
            System.err.println("Error de escritura: " + e.getMessage());
        }
    }
}
