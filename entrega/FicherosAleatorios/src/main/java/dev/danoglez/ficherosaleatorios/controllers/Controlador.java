package dev.danoglez.ficherosaleatorios.controllers;

import dev.danoglez.ficherosaleatorios.models.Empleado;
import dev.danoglez.ficherosaleatorios.repositories.EmpleadoRepositorio;
import dev.danoglez.ficherosaleatorios.views.Vista;
import java.io.IOException;
import java.util.ArrayList;

public class Controlador {

    // Variables
    private final EmpleadoRepositorio modelo;
    private final Vista vista;

    // Constructores
    public Controlador(EmpleadoRepositorio modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    // Metodos
    public void ejecutar() {
        boolean seguir = true;

        while (seguir) {
            String opcion = vista.pedirTexto(
                    "\n===== MENU =====\n"
                    + "1. Listar empleados\n"
                    + "2. Anadir empleado\n"
                    + "3. Modificar empleado\n"
                    + "4. Buscar empleado por ID\n"
                    + "5. Salir\n"
                    + "Selecciona una opcion: ");

            if (opcion == null || opcion.trim().equals("5")) {
                seguir = false;
            } else if (opcion.trim().equals("1")) {
                listarEmpleados();
            } else if (opcion.trim().equals("2")) {
                anadirEmpleado();
            } else if (opcion.trim().equals("3")) {
                modificarEmpleado();
            } else if (opcion.trim().equals("4")) {
                buscarEmpleado();
            } else {
                vista.mostrarTexto("Opcion no valida.");
            }
        }

        vista.mostrarTexto("----- Fin del Programa -----");
    }

    // Lista TODOS los empleados
    private void listarEmpleados() {
        try {
            ArrayList<Empleado> empleados = modelo.listar();
            if (empleados.isEmpty()) {
                vista.mostrarTexto("No hay empleados.");
                return;
            }

            vista.mostrarTexto("\n=== EMPLEADOS ===");
            StringBuilder texto = new StringBuilder();
            for (Empleado empleado : empleados) {
                texto.append(empleado).append(System.lineSeparator());
            }
            vista.mostrarTexto(texto.toString());
        } catch (IOException e) {
            vista.mostrarError("No se ha podido listar: " + e.getMessage());
        }
    }

    // Añade un nuevo Empleado
    private void anadirEmpleado() {
        try {
            int id = Integer.parseInt(vista.pedirTexto("ID: ").trim());
            String apellido = vista.pedirTexto("Apellido: ").trim();
            int departamento = Integer.parseInt(vista.pedirTexto("Departamento: ").trim());
            double salario = Double.parseDouble(vista.pedirTexto("Salario: ").trim());

            Empleado empleado = new Empleado(id, apellido, departamento, salario);
            if (modelo.guardar(empleado)) {
                vista.mostrarTexto("Empleado guardado correctamente.");
            } else {
                vista.mostrarTexto("Ya existe un empleado con ese id. Usa modificar.");
            }
        } catch (IOException e) {
            vista.mostrarError("No se ha podido guardar: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }

    // Edita un empleado por su ID
    private void modificarEmpleado() {
        try {
            int id = Integer.parseInt(vista.pedirTexto("Introduce el ID del empleado: ").trim());
            Empleado actual = modelo.buscarPorId(id);
            if (actual == null) {
                vista.mostrarTexto("No existe un empleado con ese id.");
                return;
            }

            vista.mostrarTexto("Empleado encontrado: " + actual);
            String apellido = vista.pedirTexto("Nuevo apellido: ").trim();
            int departamento = Integer.parseInt(vista.pedirTexto("Nuevo departamento: ").trim());
            double salario = Double.parseDouble(vista.pedirTexto("Nuevo salario: ").trim());

            if (modelo.modificar(id, apellido, departamento, salario)) {
                vista.mostrarTexto("Empleado modificado correctamente.");
            } else {
                vista.mostrarTexto("No existe un empleado con ese id.");
            }
        } catch (IOException e) {
            vista.mostrarError("No se ha podido modificar: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }

    // Busca un empleado por su ID
    private void buscarEmpleado() {
        try {
            int id = Integer.parseInt(vista.pedirTexto("Introduce el ID del empleado: ").trim());
            Empleado empleado = modelo.buscarPorId(id);
            if (empleado == null) {
                vista.mostrarTexto("No existe un empleado con el ID " + id);
            } else {
                vista.mostrarTexto("Empleado encontrado:");
                vista.mostrarTexto(empleado.toString());
            }
        } catch (IOException e) {
            vista.mostrarError("No se ha podido buscar: " + e.getMessage());
        } catch (RuntimeException e) {
            vista.mostrarError("Datos no validos.");
        }
    }
}
