package dev.danoglez.controllers;

import dev.danoglez.exception.VideojuegoFileException;
import dev.danoglez.models.Videojuego;
import dev.danoglez.services.VideojuegoService;
import dev.danoglez.views.IVista;
import java.util.List;

/** Controlador: coordina el menu, el servicio y la vista. No abre el XML. */
public class Controlador {

    // Variables
    private final IVista vista;
    private final VideojuegoService servicio;

    // Constructores
    public Controlador(IVista vista, VideojuegoService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    // Metodos
    public void ejecutar() {
        boolean salir = false;

        while (!salir) {
            int opcion = vista.pedirOpcion();

            try {
                switch (opcion) {
                    case 1:
                        cargar();
                        break;
                    case 2:
                        anadirVideojuego();
                        break;
                    case 3:
                        buscarVideojuego();
                        break;
                    case 4:
                        listarVideojuegos();
                        break;
                    case 5:
                        modificarVideojuego();
                        break;
                    case 6:
                        eliminarVideojuego();
                        break;
                    case 7:
                        servicio.guardarEnXml();
                        vista.mostrarTexto("Cambios guardados en el XML.");
                        break;
                    case 8:
                        listarVideojuegosPorPlataforma();
                        break;
                    case 9:
                        stats();
                        break;
                    case 0:
                        salir = confirmarSalida();
                        break;
                    default:
                        vista.mostrarError("Opcion no valida.");
                        break;
                }
            } catch (VideojuegoFileException e) {
                vista.mostrarError(e.getMessage());
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        }

        vista.mostrarTexto("----- Fin del Programa -----");
    }

    private void cargar() throws VideojuegoFileException {
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
        int valoracion  = vista.pedirNumero("Valoracion: ");

        servicio.validarDatos(titulo, plataforma, genero, horas);
        Videojuego videojuego = new Videojuego(id, titulo, plataforma, genero, horas, finalizado, valoracion);
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
        int valoracion  = vista.pedirNumero("Valoracion: ");

        servicio.modificar(id, titulo, plataforma, genero, horas, finalizado, valoracion);
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

    private void listarVideojuegosPorPlataforma() {
        String plataforma = vista.pedirTexto("Introduce la plataforma por la que quieres buscar: ");
        List<Videojuego> resultados = servicio.buscarPorPlataforma(plataforma.toUpperCase());
        if (resultados == null || resultados.isEmpty()) {
            vista.mostrarTexto("No existe ningun videojuego con esa plataforma.");
        } else {
            for (Videojuego videojuego : resultados) {
                vista.mostrarTexto(videojuego.toString());
            }
        }
    }

    private void stats(){
        vista.mostrarTexto(servicio.estadisticas());
    }

    private boolean confirmarSalida() throws VideojuegoFileException {
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
