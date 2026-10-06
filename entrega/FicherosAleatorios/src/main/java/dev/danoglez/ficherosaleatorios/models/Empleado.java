package dev.danoglez.ficherosaleatorios.models;

/**
 * Registro de empleado para el fichero de acceso aleatorio.
 * Formato del tema: id + apellido (10 char) + departamento + salario.
 */
public class Empleado {

    // Variables
    private final int id;
    private final String apellido;
    private final int departamento;
    private final double salario;

    // Constructores
    public Empleado(int id, String apellido, int departamento, double salario) {
        // Comprobamos que el apellido no sea nulo
        assert apellido != null : "El apellido no puede ser nulo";

        this.id = id;
        this.apellido = apellido;
        this.departamento = departamento;
        this.salario = salario;
    }

    // Metodos
    public int getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public int getDepartamento() {
        return departamento;
    }

    public double getSalario() {
        return salario;
    }

    @Override
    public String toString() {
        return "Id: " + id
                + " | Apellido: " + apellido
                + " | Departamento: " + departamento
                + " | Salario: " + salario;
    }
}
