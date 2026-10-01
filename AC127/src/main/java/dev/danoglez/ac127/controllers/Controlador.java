package dev.danoglez.ac127.controllers;

import dev.danoglez.ac127.models.Empleado;
import dev.danoglez.ac127.models.EmpleadoAccesoAleatorioRepositorio;
import dev.danoglez.ac127.views.Vista;
import java.io.IOException;
import java.util.ArrayList;

/** Controlador: coordina las operaciones del modelo y la interacción con la vista. */
public class Controlador {
    private final EmpleadoAccesoAleatorioRepositorio modelo;
    private final Vista vista;

    public Controlador(EmpleadoAccesoAleatorioRepositorio modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void ejecutar() {
        boolean seguir = true;

        while (seguir) {
            String opcion = vista.pedirTexto("""
                    1.- Alta
                    2.- Baja
                    3.- Modificar
                    4.- Listar
                    5.- Buscar por id
                    6.- Salir
                    Opcion:""");
            if (opcion == null || opcion.trim().equals("6")) {
                seguir = false;
            } else if (opcion.trim().equals("1")) {
                alta();
            } else if (opcion.trim().equals("2")) {
                baja();
            } else if (opcion.trim().equals("3")) {
                modificar();
            } else if (opcion.trim().equals("4")) {
                listar();
            } else if (opcion.trim().equals("5")) {
                buscar();
            } else {
                vista.mostrarTexto("Opcion no valida.");
            }
        }

        vista.mostrarTexto("----- Fin del Programa -----");
    }

    private void alta() {
        try {
            String apellido = vista.pedirTexto("Apellido:");
            int departamento = Integer.parseInt(vista.pedirTexto("Departamento:").trim());
            double salario = Double.parseDouble(vista.pedirTexto("Salario:").trim());
            Empleado empleado = modelo.alta(apellido.trim(), departamento, salario);
            vista.mostrarTexto("Alta realizada: " + empleado);
        } catch (IOException e) {
            vista.mostrarError("No se ha podido dar de alta: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }

    private void baja() {
        try {
            int id = Integer.parseInt(vista.pedirTexto("Id:").trim());
            if (modelo.baja(id)) {
                vista.mostrarTexto("Empleado dado de baja.");
            } else {
                vista.mostrarTexto("No existe un empleado con ese id.");
            }
        } catch (IOException e) {
            vista.mostrarError("No se ha podido dar de baja: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }

    private void modificar() {
        try {
            int id = Integer.parseInt(vista.pedirTexto("Id:").trim());
            String apellido = vista.pedirTexto("Apellido:").trim();
            int departamento = Integer.parseInt(vista.pedirTexto("Departamento:").trim());
            double salario = Double.parseDouble(vista.pedirTexto("Salario:").trim());
            if (modelo.modificar(id, apellido, departamento, salario)) {
                vista.mostrarTexto("Empleado modificado.");
            } else {
                vista.mostrarTexto("No existe un empleado con ese id.");
            }
        } catch (IOException e) {
            vista.mostrarError("No se ha podido modificar: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }

    private void listar() {
        try {
            ArrayList<Empleado> empleados = modelo.listar();
            if (empleados.isEmpty()) {
                vista.mostrarTexto("No hay empleados.");
                return;
            }
            StringBuilder texto = new StringBuilder();
            for (Empleado empleado : empleados) {
                texto.append(empleado).append(System.lineSeparator());
            }
            vista.mostrarTexto(texto.toString());
        } catch (IOException e) {
            vista.mostrarError("No se ha podido listar: " + e.getMessage());
        }
    }

    private void buscar() {
        try {
            int id = Integer.parseInt(vista.pedirTexto("Id:").trim());
            Empleado empleado = modelo.buscarPorId(id);
            if (empleado == null) {
                vista.mostrarTexto("No existe un empleado con ese id.");
            } else {
                vista.mostrarTexto(empleado.toString());
            }
        } catch (IOException e) {
            vista.mostrarError("No se ha podido buscar: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }
}
