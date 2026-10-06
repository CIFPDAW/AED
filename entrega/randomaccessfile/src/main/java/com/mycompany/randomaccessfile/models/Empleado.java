package com.mycompany.randomaccessfile.models;

public class Empleado {
    // Variables
    private int id;
    private String apellido;
    private int departamento;
    private double salario;
    
    // Constructor
    public Empleado(int id, String apellido, int departamento, double salario) {
        assert id <= 0 : "El id no puede ser menor o igual a 0";
        assert apellido != null && !apellido.isBlank() && apellido.length() <= 10 : "El apellido no puede estar vacío o ser mayor a 10 caracteres";
        assert departamento <= 0 : "El departamento no puede ser menor o igual a 0";
        assert salario <= 0.0 : "El salario no puede ser menor o igual a 0";
        
        this.id = id;
        this.apellido = apellido;
        this.departamento = departamento;
        this.salario = salario;
    }
    
    // Getters
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
    
    // Setters
     public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    // toString
    @Override
    public String toString() {
        return "Empleado{" + "id=" + id + ", apellido='" + apellido + '\'' + ", departamento=" + departamento + ", salario=" + salario + '}';
    }
}
