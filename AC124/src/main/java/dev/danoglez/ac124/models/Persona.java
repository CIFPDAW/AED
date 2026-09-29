package dev.danoglez.ac124.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * AC1.24. serialVersionUID fijo para que un cambio automático de la clase
 * no impida leer las personas ya guardadas.
 */
public class Persona implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Variables
    private final String nombre;
    private final int edad;

    // Constructores
    public Persona(String nombre, int edad) {
        assert nombre != null : "El nombre no puede ser nulo";
        this.nombre = nombre;
        this.edad = edad;
    }

    // Metodos
    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Edad: " + edad;
    }
}
