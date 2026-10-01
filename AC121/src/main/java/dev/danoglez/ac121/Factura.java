package dev.danoglez.ac121;

/**
 * Misma forma que el ejemplo del apartado 6.2: objeto(), unidades() y precio().
 */
public class Factura {

    // Variables
    private final String objeto;
    private final int unidades;
    private final double precio;

    // Constructores
    public Factura(String objeto, int unidades, double precio) {
        assert objeto != null : "El articulo no puede ser nulo";
        this.objeto = objeto;
        this.unidades = unidades;
        this.precio = precio;
    }

    // Metodos
    public String objeto() {
        return objeto;
    }

    public int unidades() {
        return unidades;
    }

    public double precio() {
        return precio;
    }
}
