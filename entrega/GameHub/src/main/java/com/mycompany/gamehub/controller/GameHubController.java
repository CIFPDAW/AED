package com.mycompany.gamehub.controller;

import com.mycompany.gamehub.exception.GameHubFileException;
import com.mycompany.gamehub.model.Game;
import com.mycompany.gamehub.service.GameHubService;
import com.mycompany.gamehub.view.IGameHubView;

import java.util.List;

public class GameHubController {

    private final IGameHubView vista;
    private final GameHubService servicio;

    // Constructor
    public GameHubController(IGameHubView vista, GameHubService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    // Metodo principal del controlador
    public void iniciar() {

        boolean salir = false;

        while (!salir) {

            int opcion = vista.pedirOpcion();

            try {

                switch (opcion) {

                    case 1:
                        if (confirmarCarga()) {
                            servicio.cargarDesdeTexto();
                            vista.mostrarTexto(
                                    "Datos cargados desde el fichero de texto."
                            );
                        }
                        break;

                    case 2:
                        if (confirmarCarga()) {
                            servicio.cargarDesdeBinario();
                            vista.mostrarTexto(
                                    "Datos cargados desde el fichero binario."
                            );
                        }
                        break;

                    case 3:
                        anadirVideojuego();
                        break;

                    case 4:
                        buscarVideojuego();
                        break;

                    case 5:
                        listarVideojuegos();
                        break;

                    case 6:
                        modificarVideojuego();
                        break;

                    case 7:
                        eliminarVideojuego();
                        break;

                    case 8:
                        servicio.guardarEnTexto();
                        vista.mostrarTexto("Datos guardados en el fichero de texto.");
                        break;

                    case 9:
                        servicio.guardarEnBinario();
                        vista.mostrarTexto("Datos guardados en el fichero binario.");
                        break;

                    case 10:
                        servicio.sincronizar();
                        vista.mostrarTexto("Ficheros sincronizados correctamente.");
                        break;

                    case 0:
                        if (servicio.hayCambios()) {
                            boolean confirmar = vista.pedirConfirmacion(
                                    "Hay cambios sin guardar. ¿Quieres salir igualmente?"
                            );

                            if (!confirmar) {
                                break;
                            }
                        }

                        salir = true;
                        break;

                    default:
                        vista.mostrarError("Opción no válida.");
                        break;
                }

            } catch (GameHubFileException e) {
                vista.mostrarError(e.getMessage());

            } catch (IllegalArgumentException e) {
                vista.mostrarError(e.getMessage());
            }
        }

        vista.close();
        vista.mostrarTexto("Hasta pronto.");
    }

    // Añadir videojuego
    private void anadirVideojuego() {

        int id = vista.pedirNumero("ID: ");

        if (servicio.buscarPorId(id) != null) {
            vista.mostrarError("Ya existe un videojuego con ese ID.");
            return;
        }

        String titulo = vista.pedirTexto("Título: ");
        String plataforma = vista.pedirTexto("Plataforma: ");
        String genero = vista.pedirTexto("Género: ");
        double horas = vista.pedirDouble("Horas jugadas: ");
        boolean finalizado = vista.pedirBooleano(
                "¿Finalizado? (true/false): "
        );

        Game videojuego = new Game(
                id, titulo, plataforma, genero, horas, finalizado
        );

        servicio.anadir(videojuego);

        vista.mostrarTexto("Videojuego añadido correctamente.");
    }

    // Buscar videojuego
    private void buscarVideojuego() {

        int id = vista.pedirNumero("Introduce el ID que quieres buscar: ");

        Game videojuego = servicio.buscarPorId(id);

        if (videojuego == null) {
            vista.mostrarTexto("No existe ningún videojuego con ese ID.");
        } else {
            mostrarVideojuego(videojuego);
        }
    }

    // Listar videojuegos
    private void listarVideojuegos() {

        List<Game> videojuegos = servicio.listar();

        if (videojuegos.isEmpty()) {
            vista.mostrarTexto("No hay videojuegos registrados.");
            return;
        }

        for (Game videojuego : videojuegos) {
            mostrarVideojuego(videojuego);
        }
    }

    // Modificar videojuego
    private void modificarVideojuego() {

        int id = vista.pedirNumero("ID del videojuego que quieres modificar: ");

        Game videojuego = servicio.buscarPorId(id);

        if (videojuego == null) {
            vista.mostrarTexto("No existe ningún videojuego con ese ID.");
            return;
        }

        String titulo = vista.pedirTexto("Nuevo título: ");
        String plataforma = vista.pedirTexto("Nueva plataforma: ");
        String genero = vista.pedirTexto("Nuevo género: ");
        double horas = vista.pedirDouble("Nuevas horas jugadas: ");
        boolean finalizado = vista.pedirBooleano("¿Finalizado? (true/false): ");

        servicio.modificar(
                id, titulo, plataforma, genero, horas, finalizado
        );

        vista.mostrarTexto("Videojuego modificado correctamente.");
    }

    // Eliminar videojuego
    private void eliminarVideojuego() {

        int id = vista.pedirNumero("ID del videojuego que quieres eliminar: ");

        Game videojuego = servicio.buscarPorId(id);

        if (videojuego == null) {
            vista.mostrarTexto("No existe ningún videojuego con ese ID.");
            return;
        }

        boolean confirmar = vista.pedirConfirmacion(
                "¿Seguro que quieres eliminar " + videojuego.getTitulo() + "?"
        );

        if (confirmar) {
            servicio.eliminar(id);
            vista.mostrarTexto("Videojuego eliminado correctamente.");
        } else {
            vista.mostrarTexto("Eliminación cancelada.");
        }
    }

    // Mostrar los datos de un videojuego
    private void mostrarVideojuego(Game videojuego) {
        vista.mostrarTexto(videojuego.toString());
    }

    // Comprobar si podemos cargar datos sin perder cambios
    private boolean confirmarCarga() {

        if (servicio.hayCambios()) {
            return vista.pedirConfirmacion(
                    "Hay cambios sin guardar. ¿Quieres continuar?"
            );
        }

        return true;
    }

    // -----------------------------------------------------------------------
    // Posibles modificaciones
    // Buscar videojuegos por título
    private void buscarPorTitulo() {

        String titulo = vista.pedirTexto(
                "Introduce el título que quieres buscar: "
        );

        boolean encontrado = false;

        for (Game videojuego : servicio.listar()) {

            if (videojuego.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                mostrarVideojuego(videojuego);
                encontrado = true;
            }
        }

        if (!encontrado) {
            vista.mostrarTexto("No se encontraron videojuegos.");
        }
    }

    // Filtrar videojuegos por estado
    private void filtrarPorEstado() {

        boolean finalizado = vista.pedirBooleano(
                "¿Mostrar juegos finalizados? (true/false): "
        );

        boolean encontrado = false;

        for (Game videojuego : servicio.listar()) {

            if (videojuego.isFinalizado() == finalizado) {
                mostrarVideojuego(videojuego);
                encontrado = true;
            }
        }

        if (!encontrado) {
            vista.mostrarTexto(
                    "No hay videojuegos con ese estado."
            );
        }
    }

    // Ordenar videojuegos por horas jugadas
    private void ordenarPorHoras() {

        List<Game> videojuegos = servicio.listar();

        videojuegos.sort(
                (juego1, juego2)
                -> Double.compare(
                        juego1.getHorasJugadas(),
                        juego2.getHorasJugadas()
                )
        );

        for (Game videojuego : videojuegos) {
            mostrarVideojuego(videojuego);
        }
    }

    // Comprobar si existe un videojuego
    private boolean existeVideojuego(int id) {

        return servicio.buscarPorId(id) != null;
    }

    // Contar videojuegos finalizados
    private void contarFinalizados() {

        int contador = 0;

        for (Game videojuego : servicio.listar()) {

            if (videojuego.isFinalizado()) {
                contador++;
            }
        }

        vista.mostrarTexto(
                "Videojuegos finalizados: " + contador
        );
    }

    // Calcular el total de horas jugadas
    private void calcularHorasTotales() {

        double total = 0;

        for (Game videojuego : servicio.listar()) {
            total += videojuego.getHorasJugadas();
        }

        vista.mostrarTexto(
                "Total de horas jugadas: " + total
        );
    }

    // Listar videojuegos con más horas que las indicadas
    private void listarPorHoras() {

        double horas = vista.pedirDouble(
                "Introduce el mínimo de horas jugadas: "
        );

        boolean encontrado = false;

        for (Game videojuego : servicio.listar()) {

            if (videojuego.getHorasJugadas() > horas) {
                mostrarVideojuego(videojuego);
                encontrado = true;
            }
        }

        if (!encontrado) {
            vista.mostrarTexto(
                    "No hay videojuegos con más de " + horas + " horas."
            );
        }
    }
}
