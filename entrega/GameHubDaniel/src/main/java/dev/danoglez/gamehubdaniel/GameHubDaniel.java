package dev.danoglez.gamehubdaniel;

import dev.danoglez.gamehubdaniel.controllers.Controlador;
import dev.danoglez.gamehubdaniel.services.VideojuegoService;
import dev.danoglez.gamehubdaniel.views.Vista;
import dev.danoglez.gamehubdaniel.views.VistaTexto;

public class GameHubDaniel {

    public static void main(String[] args) {
        VideojuegoService servicio = new VideojuegoService();

        try (Vista vista = new VistaTexto()) {
            Controlador controlador = new Controlador(vista, servicio);
            controlador.ejecutar();
        }
    }
}
