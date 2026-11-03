package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;
import java.util.List;

/**
 * Lo que tiene que saber hacer cualquier repositorio (sitio donde se guardan
 * los videojuegos). El servicio solo conoce esta interfaz, no sabe si
 * por debajo hay un XML, un fichero de texto o lo que sea.
 */
public interface IGameHubRepository {

    // Lee todos los videojuegos del almacenamiento
    List<Game> cargar() throws GameHubXmlException;

    // Sobrescribe el almacenamiento con la lista que se le pasa
    void guardar(List<Game> videojuegos) throws GameHubXmlException;

    // Busca un videojuego directamente en el almacenamiento (no en memoria)
    Game buscarPorId(int id) throws GameHubXmlException;
}
