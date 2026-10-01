package dev.danoglez.ac117;

/**
 * AC1.17. Usuario con nombre, NIF y edad. Se guarda en texto como nombre:nif:edad.
 */
public class Usuario {

    // Variables
    private final String nombre;
    private final String nif;
    private final int edad;

    // Constructores
    public Usuario(String nombre, String nif, int edad) {
        assert nombre != null && nif != null;
        this.nombre = nombre;
        this.nif = nif;
        this.edad = edad;
    }

    // Metodos
    public String getNombre() {
        return nombre;
    }

    public String getNif() {
        return nif;
    }

    public int getEdad() {
        return edad;
    }

    public String aLinea() {
        return nombre + ":" + nif + ":" + edad;
    }

    public static Usuario desdeLinea(String linea) {
        String[] campos = linea.split(":", 3);
        return new Usuario(campos[0], campos[1], Integer.parseInt(campos[2]));
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + System.lineSeparator()
                + "NIF: " + nif + System.lineSeparator()
                + "Edad: " + edad;
    }
}
