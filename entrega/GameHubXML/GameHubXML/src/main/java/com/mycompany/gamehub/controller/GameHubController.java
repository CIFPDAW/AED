package com.mycompany.gamehub.controller;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;
import com.mycompany.gamehub.service.GameHubService;
import com.mycompany.gamehub.view.IGameHubView;

import java.util.List;

/**
 * CONTROLADOR: hace de "puente". Recoge la opción del menú desde la vista,
 * llama al servicio y le dice a la vista qué mostrar.
 * No toca el XML (eso es del repositorio) ni usa System.out (eso es de la vista).
 */
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
                    case 1 -> cargarDatos();
                    case 2 -> anadirVideojuego();
                    case 3 -> buscarVideojuego();
                    case 4 -> listarVideojuegos();
                    case 5 -> modificarVideojuego();
                    case 6 -> eliminarVideojuego();
                    case 7 -> guardarDatos();
                    case 8 -> buscarEnXml();
                    case 0 -> salir = salir();
                    default -> vista.mostrarError("Opción no válida.");
                }

            } catch (GameHubXmlException e) {
                // Errores de lectura/escritura/parseo del XML
                vista.mostrarError(e.getMessage());

            } catch (IllegalArgumentException e) {
                // Errores de validación (ID repetido, título vacío, horas negativas...)
                vista.mostrarError(e.getMessage());
            }
        }

        vista.mostrarTexto("Hasta pronto.");
        vista.close();
    }

    // 1. Cargar datos desde el XML
    private void cargarDatos() throws GameHubXmlException {

        if (servicio.hayCambios()) {
            boolean continuar = vista.pedirConfirmacion(
                    "Hay cambios sin guardar que se perderán. ¿Quieres continuar?");
            if (!continuar) {
                return;
            }
        }

        int cantidad = servicio.cargar();
        vista.mostrarTexto("Datos cargados desde XML. Videojuegos cargados: " + cantidad);
    }

    // 2. Añadir videojuego
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
        boolean finalizado = vista.pedirBooleano("¿Finalizado? (true/false): ");

        // Si algún dato no es válido, el constructor lanza IllegalArgumentException
        Game videojuego = new Game(id, titulo, plataforma, genero, horas, finalizado);
        servicio.anadir(videojuego);

        vista.mostrarTexto("Videojuego añadido. Recuerda guardar (opción 7) para escribirlo en el XML.");
    }

    // 3. Buscar videojuego (en memoria)
    private void buscarVideojuego() {

        int id = vista.pedirNumero("Introduce el ID que quieres buscar: ");
        Game videojuego = servicio.buscarPorId(id);

        if (videojuego == null) {
            vista.mostrarTexto("No existe ningún videojuego con ese ID.");
        } else {
            vista.mostrarTexto(videojuego.toString());
        }
    }

    // 4. Listar videojuegos
    private void listarVideojuegos() {

        List<Game> videojuegos = servicio.listar();

        if (videojuegos.isEmpty()) {
            vista.mostrarTexto("El catálogo está vacío. (¿Has cargado el XML con la opción 1?)");
            return;
        }

        vista.mostrarTexto("---- Catálogo (" + videojuegos.size() + " videojuegos) ----");
        for (Game videojuego : videojuegos) {
            vista.mostrarTexto(videojuego.toString());
        }
    }

    // 5. Modificar videojuego
    private void modificarVideojuego() {

        int id = vista.pedirNumero("ID del videojuego que quieres modificar: ");
        Game videojuego = servicio.buscarPorId(id);

        if (videojuego == null) {
            vista.mostrarTexto("No existe ningún videojuego con ese ID.");
            return;
        }

        vista.mostrarTexto("Datos actuales: " + videojuego);

        String titulo = vista.pedirTexto("Nuevo título: ");
        String plataforma = vista.pedirTexto("Nueva plataforma: ");
        String genero = vista.pedirTexto("Nuevo género: ");
        double horas = vista.pedirDouble("Nuevas horas jugadas: ");
        boolean finalizado = vista.pedirBooleano("¿Finalizado? (true/false): ");

        servicio.modificar(id, titulo, plataforma, genero, horas, finalizado);

        vista.mostrarTexto("Videojuego modificado correctamente.");
    }

    // 6. Eliminar videojuego
    private void eliminarVideojuego() {

        int id = vista.pedirNumero("ID del videojuego que quieres eliminar: ");
        Game videojuego = servicio.buscarPorId(id);

        if (videojuego == null) {
            vista.mostrarError("No existe ningún videojuego con ese ID.");
            return;
        }

        boolean confirmar = vista.pedirConfirmacion(
                "¿Seguro que quieres eliminar \"" + videojuego.getTitulo() + "\"?");

        if (confirmar) {
            servicio.eliminar(id);
            vista.mostrarTexto("Videojuego eliminado correctamente.");
        } else {
            vista.mostrarTexto("Eliminación cancelada.");
        }
    }

    // 7. Guardar en el XML
    private void guardarDatos() throws GameHubXmlException {
        servicio.guardar();
        vista.mostrarTexto("Cambios guardados correctamente en data/videojuegos.xml");
    }

    // 8. Buscar directamente en el fichero XML con XPath
    private void buscarEnXml() throws GameHubXmlException {

        int id = vista.pedirNumero("ID a buscar en el fichero XML: ");
        Game videojuego = servicio.buscarEnXml(id);

        if (videojuego == null) {
            vista.mostrarTexto("No hay ningún videojuego con ese ID en el fichero XML.");
        } else {
            vista.mostrarTexto("Encontrado en el XML: " + videojuego);
        }
    }

    // 0. Salir. Devuelve true si el programa debe terminar.
    private boolean salir() throws GameHubXmlException {

        if (servicio.hayCambios()) {
            boolean guardar = vista.pedirConfirmacion(
                    "Hay cambios sin guardar. ¿Deseas guardarlos en el XML antes de salir?");

            if (guardar) {
                servicio.guardar();
                vista.mostrarTexto("Cambios guardados correctamente.");
            } else {
                vista.mostrarTexto("Saliendo sin guardar.");
            }
        }

        return true;
    }
}
