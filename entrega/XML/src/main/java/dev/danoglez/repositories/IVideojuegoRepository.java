package dev.danoglez.repositories;

import dev.danoglez.exception.VideojuegoFileException;
import dev.danoglez.models.Videojuego;
import java.util.List;

/**
 * Contrato del almacen XML.
 * El servicio solo conoce estas operaciones; no sabe como esta escrito el fichero.
 */
public interface IVideojuegoRepository {

    List<Videojuego> cargar() throws VideojuegoFileException;

    void guardar(List<Videojuego> videojuegos) throws VideojuegoFileException;
}
