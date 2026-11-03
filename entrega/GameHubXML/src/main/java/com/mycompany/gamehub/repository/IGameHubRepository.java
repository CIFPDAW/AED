package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;
import java.util.List;

public interface IGameHubRepository {

    List<Game> cargar() throws GameHubXmlException;

    void guardar(List<Game> videojuegos) throws GameHubXmlException;

    Game buscarPorId(int id) throws GameHubXmlException;
}
