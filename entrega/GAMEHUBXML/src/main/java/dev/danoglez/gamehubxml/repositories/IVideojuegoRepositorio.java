package dev.danoglez.gamehubxml.repositories;

import dev.danoglez.gamehubxml.exception.GameHubXmlException;
import dev.danoglez.gamehubxml.models.Videojuego;
import java.util.List;

/**
 * Contrato del almacen. El servicio solo conoce estas dos operaciones.
 * La clase XML es la que sabe abrir data/videojuegos.xml.
 */
public interface IVideojuegoRepositorio {

    List<Videojuego> cargar() throws GameHubXmlException;

    void guardar(List<Videojuego> videojuegos) throws GameHubXmlException;
}
