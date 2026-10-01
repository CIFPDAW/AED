package dev.danoglez.ac125;

import java.io.Serializable;

/**
 * AC1.25. cuenta solo puede ser Al día, Atrasada o Deudor.
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    // Variables
    private final int codigo;
    private final String nombre;
    private final String direccion;
    private final double saldo;
    private final String cuenta;

    // Constructores
    public Cliente(int codigo, String nombre, String direccion, double saldo, String cuenta) {
        assert nombre != null && direccion != null && cuenta != null;
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.saldo = saldo;
        this.cuenta = cuenta;
    }

    // Metodos
    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public boolean esMoroso() {
        return cuenta.equals("Atrasada") || cuenta.equals("Deudor");
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo
                + " | Nombre: " + nombre
                + " | Direccion: " + direccion
                + " | Saldo: " + saldo
                + " | Cuenta: " + cuenta;
    }
}
