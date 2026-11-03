package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubFileException;
import com.mycompany.gamehub.model.Game;
import java.util.List;

public interface IGameHubRepository {

    List<Game> cargar() throws GameHubFileException;

    void guardar(List<Game> videojuegos) throws GameHubFileException;

}
