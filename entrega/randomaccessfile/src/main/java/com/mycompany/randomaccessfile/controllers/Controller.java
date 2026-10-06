package com.mycompany.randomaccessfile.controllers;

import com.mycompany.randomaccessfile.views.VistaTexto;
import com.mycompany.randomaccessfile.models.Empleado;
import com.mycompany.randomaccessfile.repositories.EmpleadoAccesoAleatorioRepositorio;

import java.io.IOException;
import java.nio.file.Path;

public class Controller {

    private final Path fichero;
    private final VistaTexto vista;

    public Controller() {
        this.fichero = Path.of("/home/2damb@informatica.edu/NetBeansProjects/randomaccessfile/src/main/java/com/mycompany/randomaccessfile/fichero/empleados.dat");
        this.vista = new VistaTexto();
    }

    public void iniciar() {

        boolean salir = false;

        while (!salir) {

            int opcion = vista.pedirOpcion();

            try {

                switch (opcion) {

                    case 1 ->
                        listarEmpleados();

                    case 2 ->
                        anadirEmpleado();

                    case 3 ->
                        modificarEmpleado();

                    case 4 ->
                        buscarEmpleado();

                    case 5 ->
                        salir = true;

                    default ->
                        vista.mostrarError("Opción no válida.");
                }

            } catch (IOException e) {
                vista.mostrarError(
                        "Error al acceder al fichero: " + e.getMessage()
                );
            }
        }

        vista.close();
    }

    // LISTAR
    private void listarEmpleados() throws IOException {

        vista.mostrarTexto("\n=== EMPLEADOS ===");

        EmpleadoAccesoAleatorioRepositorio.listar(fichero);
    }

    // BUSCAR
    private void buscarEmpleado() throws IOException {

        vista.mostrarTexto("\n=== BUSCAR EMPLEADO ===");

        int id = vista.pedirNumero("Introduce el ID del empleado: ");

        Empleado empleado = EmpleadoAccesoAleatorioRepositorio.buscarPorId(fichero, id);

        if (empleado == null) {
            vista.mostrarError("No existe un empleado con el ID " + id);

            return;
        }

        vista.mostrarTexto("Empleado encontrado:");

        vista.mostrarTexto(empleado.toString());
    }

    // AÑADIR
    private void anadirEmpleado() throws IOException {

        vista.mostrarTexto("\n=== AÑADIR EMPLEADO ===");

        int id = vista.pedirNumero("ID: ");

        String apellido = vista.pedirTexto("Apellido: ");

        int departamento = vista.pedirNumero("Departamento: ");

        double salario = vista.pedirDouble("Salario: ");

        Empleado empleado = new Empleado(id, apellido, departamento, salario);

        EmpleadoAccesoAleatorioRepositorio.guardar(fichero, empleado);

        vista.mostrarTexto("Empleado guardado correctamente.");
    }

    // MODIFICAR
    private void modificarEmpleado() throws IOException {

        vista.mostrarTexto("\n=== MODIFICAR EMPLEADO ===");

        int id = vista.pedirNumero("Introduce el ID del empleado: ");

        Empleado empleado = EmpleadoAccesoAleatorioRepositorio.buscarPorId(fichero, id);

        if (empleado == null) {
            vista.mostrarError("No existe un empleado con ese ID.");
            return;
        }

        vista.mostrarTexto("Empleado encontrado: " + empleado);

        String apellido = vista.pedirTexto("Nuevo apellido: ");

        int departamento = vista.pedirNumero("Nuevo departamento: ");

        double salario = vista.pedirDouble("Nuevo salario: ");

        Empleado modificado = new Empleado(id, apellido, departamento, salario);

        EmpleadoAccesoAleatorioRepositorio.modificar(fichero, modificado);

        vista.mostrarTexto("Empleado modificado correctamente.");
    }
}
