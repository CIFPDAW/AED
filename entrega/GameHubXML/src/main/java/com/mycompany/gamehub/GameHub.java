package com.mycompany.gamehub;

import com.mycompany.gamehub.controller.GameHubController;
import com.mycompany.gamehub.service.GameHubService;
import com.mycompany.gamehub.view.GameHubView;

public class GameHub {

    public static void main(String[] args) {
        GameHubView vista = new GameHubView();

        GameHubService servicio = new GameHubService();

        GameHubController controlador = new GameHubController(vista, servicio);

        controlador.iniciar();
    }
}
