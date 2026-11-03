package com.mycompany.gamehub;

import com.mycompany.gamehub.controller.GameHubController;
import com.mycompany.gamehub.service.GameHubService;
import com.mycompany.gamehub.view.GameHubView;

public class GameHub {

    public static void main(String[] args) {
        // Crear la vista
        GameHubView vista = new GameHubView();

        // Crear el servicio
        GameHubService servicio = new GameHubService();

        // Crear el controlador
        GameHubController controlador = new GameHubController(vista, servicio);

        // Iniciar el programa
        controlador.iniciar();
    }
}
