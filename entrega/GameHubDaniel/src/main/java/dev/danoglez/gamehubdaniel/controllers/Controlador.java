package dev.danoglez.gamehubdaniel.controllers;

import dev.danoglez.gamehubdaniel.exception.GameHubFileException;
import dev.danoglez.gamehubdaniel.models.Videojuego;
import dev.danoglez.gamehubdaniel.services.VideojuegoService;
import dev.danoglez.gamehubdaniel.views.Vista;
import java.util.List;

/** Controlador: coordina el menu, el servicio y la vista. */
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
                    if (confirmarCarga()) {
                        servicio.cargarDesdeTexto();
                        vista.mostrarTexto("Datos cargados desde el fichero de texto.");
                    }
                } else if (opcion == 2) {
                    if (confirmarCarga()) {
                        servicio.cargarDesdeBinario();
                        vista.mostrarTexto("Datos cargados desde el fichero binario.");
                    }
                } else if (opcion == 3) {
                    anadirVideojuego();
                } else if (opcion == 4) {
                    buscarVideojuego();
                } else if (opcion == 5) {
                    listarVideojuegos();
                } else if (opcion == 6) {
                    modificarVideojuego();
                } else if (opcion == 7) {
                    eliminarVideojuego();
                } else if (opcion == 8) {
                    servicio.guardarEnTexto();
                    vista.mostrarTexto("Datos guardados en el fichero de texto.");
                } else if (opcion == 9) {
                    servicio.guardarEnBinario();
                    vista.mostrarTexto("Datos guardados en el fichero binario.");
                } else if (opcion == 10) {
                    // EXTRA: sincroniza TXT y DAT; si un id coincide, el del DAT
                    // recibe un id nuevo y va al final (no se pierde ni se sobrescribe)
                    servicio.sincronizar();
                    vista.mostrarTexto("Archivos sincronizados.");
                } else if (opcion == 0) {
                    salir = confirmarSalida();
                } else {
                    vista.mostrarError("Opcion no valida.");
                }
            } catch (GameHubFileException e) {
                vista.mostrarError(e.getMessage());
            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        }

        vista.mostrarTexto("----- Fin del Programa -----");
    }

    private void anadirVideojuego() {
        int id = vista.pedirNumero("ID: ");
        if (servicio.buscarPorId(id) != null) {
            vista.mostrarError("Ya existe un videojuego con ese ID.");
            return;
        }

        String titulo = vista.pedirTexto("Titulo: ").trim();
        String plataforma = vista.pedirTexto("Plataforma: ").trim();
        String genero = vista.pedirTexto("Genero: ").trim();
        double horas = vista.pedirDouble("Horas jugadas: ");
        boolean finalizado = vista.pedirBooleano("¿Finalizado? (true/false): ");

        Videojuego videojuego = new Videojuego(id, titulo, plataforma, genero, horas, finalizado);
        servicio.anadir(videojuego);
        vista.mostrarTexto("Videojuego anadido correctamente.");
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
            vista.mostrarTexto("No hay videojuegos registrados.");
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
        vista.mostrarTexto("Videojuego modificado correctamente.");
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
            vista.mostrarTexto("Videojuego eliminado correctamente.");
        } else {
            vista.mostrarTexto("Eliminacion cancelada.");
        }
    }

    // Si hay cambios en memoria, avisamos antes de pisar con una carga
    private boolean confirmarCarga() {
        if (servicio.hayCambios()) {
            return vista.pedirConfirmacion("Hay cambios sin guardar. ¿Quieres continuar?");
        }
        return true;
    }

    // El enunciado pide preguntar si actualizar los ficheros antes de salir
    private boolean confirmarSalida() throws GameHubFileException {
        if (servicio.hayDatosEnMemoria() || servicio.hayCambios()) {
            boolean guardar = vista.pedirConfirmacion(
                    "Hay datos en memoria. ¿Desea actualizar los ficheros antes de salir?");
            if (guardar) {
                servicio.guardarEnAmbos();
                vista.mostrarTexto("Ficheros actualizados.");
            }
        }
        return true;
    }

    /*
     * EXTRAS / VARIANTES (no las pide el menu del enunciado):
     * Las dejamos preparadas por si el profesor pide ampliar.
     *
    private void buscarPorTitulo() {
        String titulo = vista.pedirTexto("Introduce el titulo que quieres buscar: ");
        boolean encontrado = false;
        for (Videojuego videojuego : servicio.listar()) {
            if (videojuego.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                vista.mostrarTexto(videojuego.toString());
                encontrado = true;
            }
        }
        if (!encontrado) {
            vista.mostrarTexto("No se encontraron videojuegos.");
        }
    }

    private void filtrarPorEstado() {
        boolean finalizado = vista.pedirBooleano("¿Mostrar juegos finalizados? (true/false): ");
        boolean encontrado = false;
        for (Videojuego videojuego : servicio.listar()) {
            if (videojuego.isFinalizado() == finalizado) {
                vista.mostrarTexto(videojuego.toString());
                encontrado = true;
            }
        }
        if (!encontrado) {
            vista.mostrarTexto("No hay videojuegos con ese estado.");
        }
    }

    private void ordenarPorHoras() {
        List<Videojuego> videojuegos = servicio.listar();
        videojuegos.sort((a, b) -> Double.compare(a.getHorasJugadas(), b.getHorasJugadas()));
        for (Videojuego videojuego : videojuegos) {
            vista.mostrarTexto(videojuego.toString());
        }
    }

    private void contarFinalizados() {
        int contador = 0;
        for (Videojuego videojuego : servicio.listar()) {
            if (videojuego.isFinalizado()) {
                contador++;
            }
        }
        vista.mostrarTexto("Videojuegos finalizados: " + contador);
    }

    private void calcularHorasTotales() {
        double total = 0;
        for (Videojuego videojuego : servicio.listar()) {
            total += videojuego.getHorasJugadas();
        }
        vista.mostrarTexto("Total de horas jugadas: " + total);
    }
     */
}
