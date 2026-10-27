package dev.danoglez;

import dev.danoglez.controllers.Controlador;
import dev.danoglez.services.VideojuegoService;
import dev.danoglez.views.IVista;
import dev.danoglez.views.VistaTexto;

/**
 * Arranque. Solo ensambla vista, servicio y controlador.
 */
public class Main {

    public static void main(String[] args) {
        VideojuegoService servicio = new VideojuegoService();

        try (IVista vista = new VistaTexto()) {
            Controlador controlador = new Controlador(vista, servicio);
            controlador.ejecutar();
        }
    }
}
