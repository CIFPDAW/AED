package dev.danoglez.gamehubxml.controllers;

import dev.danoglez.gamehubxml.exception.GameHubXmlException;
import dev.danoglez.gamehubxml.models.Videojuego;
import dev.danoglez.gamehubxml.services.VideojuegoService;
import dev.danoglez.gamehubxml.views.Vista;
import java.util.List;

/** Coordina el menu. Pide datos a la vista y llama al servicio. No abre el XML. */
public class Controlador {

    // Variables
    private final Vista vista;
    private final VideojuegoService servicio;

    // Constructores
    public Controlador(Vista vista, VideojuegoService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    // Metodos
    public void ejecutar() {
        boolean salir = false;

        while (!salir) {
            int opcion = vista.pedirOpcion();

            try {
                if (opcion == 1) {
                    cargar();
                } else if (opcion == 2) {
                    anadirVideojuego();
                } else if (opcion == 3) {
                    buscarVideojuego();
                } else if (opcion == 4) {
                    listarVideojuegos();
                } else if (opcion == 5) {
                    modificarVideojuego();
                } else if (opcion == 6) {
                    eliminarVideojuego();
                } else if (opcion == 7) {
                    servicio.guardarEnXml();
                    vista.mostrarTexto("Cambios guardados en el XML.");
                } else if (opcion == 0) {
                    salir = confirmarSalida();
                } else {
                    vista.mostrarError("Opcion no valida.");
                }
            } catch (GameHubXmlException e) {
                vista.mostrarError(e.getMessage());
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        }

        vista.mostrarTexto("----- Fin del Programa -----");
    }

    private void cargar() throws GameHubXmlException {
        if (servicio.hayCambios()) {
            boolean continuar = vista.pedirConfirmacion("Hay cambios sin guardar. ¿Quieres continuar?");
            if (!continuar) {
                vista.mostrarTexto("Carga cancelada.");
                return;
            }
        }
        int cantidad = servicio.cargarDesdeXml();
        vista.mostrarTexto("Videojuegos cargados: " + cantidad);
    }

    private void anadirVideojuego() {
        int id = vista.pedirNumero("ID: ");
        if (id <= 0) {
            vista.mostrarError("El ID debe ser mayor que 0.");
            return;
        }
        if (servicio.buscarPorId(id) != null) {
            vista.mostrarError("Ya existe un videojuego con ese ID.");
            return;
        }

        String titulo = vista.pedirTexto("Titulo: ").trim();
        String plataforma = vista.pedirTexto("Plataforma: ").trim();
        String genero = vista.pedirTexto("Genero: ").trim();
        double horas = vista.pedirDouble("Horas jugadas: ");
        boolean finalizado = vista.pedirBooleano("¿Finalizado? (true/false): ");

        servicio.validarDatos(titulo, plataforma, genero, horas);
        Videojuego videojuego = new Videojuego(id, titulo, plataforma, genero, horas, finalizado);
        servicio.anadir(videojuego);
        vista.mostrarTexto("Videojuego anadido en memoria. El XML se actualiza al guardar.");
    }

    private void buscarVideojuego() {
        int id = vista.pedirNumero("Introduce el ID que quieres buscar: ");
        Videojuego videojuego = servicio.buscarPorId(id);
        if (videojuego == null) {
            vista.mostrarTexto("No existe ningun videojuego con ese ID.");
        } else {
            vista.mostrarTexto(videojuego.toString());
        }
    }

    private void listarVideojuegos() {
        List<Videojuego> videojuegos = servicio.listar();
        if (videojuegos.isEmpty()) {
            vista.mostrarTexto("El catalogo esta vacio.");
            return;
        }
        for (Videojuego videojuego : videojuegos) {
            vista.mostrarTexto(videojuego.toString());
        }
    }

    private void modificarVideojuego() {
        int id = vista.pedirNumero("ID del videojuego que quieres modificar: ");
        Videojuego actual = servicio.buscarPorId(id);
        if (actual == null) {
            vista.mostrarTexto("No existe ningun videojuego con ese ID.");
            return;
        }

        vista.mostrarTexto("Actual: " + actual);
        String titulo = vista.pedirTexto("Nuevo titulo: ").trim();
        String plataforma = vista.pedirTexto("Nueva plataforma: ").trim();
        String genero = vista.pedirTexto("Nuevo genero: ").trim();
        double horas = vista.pedirDouble("Nuevas horas jugadas: ");
        boolean finalizado = vista.pedirBooleano("¿Finalizado? (true/false): ");

        servicio.modificar(id, titulo, plataforma, genero, horas, finalizado);
        vista.mostrarTexto("Videojuego modificado en memoria. El XML se actualiza al guardar.");
    }

    private void eliminarVideojuego() {
        int id = vista.pedirNumero("ID del videojuego que quieres eliminar: ");
        Videojuego videojuego = servicio.buscarPorId(id);
        if (videojuego == null) {
            vista.mostrarTexto("No existe ningun videojuego con ese ID.");
            return;
        }

        boolean confirmar = vista.pedirConfirmacion(
                "¿Seguro que quieres eliminar " + videojuego.getTitulo() + "?");
        if (confirmar) {
            servicio.eliminar(id);
            vista.mostrarTexto("Videojuego eliminado de la memoria. El XML se actualiza al guardar.");
        } else {
            vista.mostrarTexto("Eliminacion cancelada.");
        }
    }

    private boolean confirmarSalida() throws GameHubXmlException {
        if (servicio.hayCambios()) {
            boolean guardar = vista.pedirConfirmacion(
                    "Hay cambios pendientes. ¿Deseas guardarlos en el XML?");
            if (guardar) {
                servicio.guardarEnXml();
                vista.mostrarTexto("Cambios guardados en el XML.");
            }
        }
        return true;
    }
}
