package dev.danoglez.gamehubxml;

import dev.danoglez.gamehubxml.controllers.Controlador;
import dev.danoglez.gamehubxml.services.VideojuegoService;
import dev.danoglez.gamehubxml.views.Vista;
import dev.danoglez.gamehubxml.views.VistaTexto;

/**
 * Arranque. Solo ensambla vista, servicio y controlador.
 * El menu y el XML viven en otras clases.
 */
public class GameHubXml {

    public static void main(String[] args) {
        VideojuegoService servicio = new VideojuegoService();

        try (Vista vista = new VistaTexto()) {
            Controlador controlador = new Controlador(vista, servicio);
            controlador.ejecutar();
        }
    }
}
