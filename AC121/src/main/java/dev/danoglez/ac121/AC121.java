package dev.danoglez.ac121;

public class AC121 {

    public static void main(String[] args) {
        GestorFacturas gestor = new GestorFacturas("facturas.txt", "facturas.dat");

        try {
            gestor.guardarBinario();
            System.out.println(gestor.leerBinario());
        } catch (Exception e) {
            System.err.println("Error de lectura: " + e.getMessage());
        }
    }
}
