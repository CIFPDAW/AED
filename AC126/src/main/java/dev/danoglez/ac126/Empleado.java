package dev.danoglez.ac126;

/**
 * Registro de empleado: id + apellido de 10 caracteres + departamento + salario.
 */
public class Empleado {

    // Variables
    private final int id;
    private final String apellido;
    private final int departamento;
    private final double salario;

    // Constructores
    public Empleado(int id, String apellido, int departamento, double salario) {
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
