package dev.danoglez.gamehubdaniel.repositories;

import dev.danoglez.gamehubdaniel.exception.GameHubFileException;
import dev.danoglez.gamehubdaniel.models.Videojuego;
import java.util.List;

/**
 * Contrato comun de los repositorios: cargar y guardar la lista.
 * Asi el servicio puede trabajar igual con texto o con binario.
 */
public interface IVideojuegoRepositorio {

    List<Videojuego> cargar() throws GameHubFileException;

    void guardar(List<Videojuego> videojuegos) throws GameHubFileException;
}
