package dev.danoglez.gamehubxml.repositories;

import dev.danoglez.gamehubxml.exception.GameHubXmlException;
import dev.danoglez.gamehubxml.models.Videojuego;
import java.nio.file.Path;
import java.util.List;

/**
 * Repositorio XML. La ruta ya esta preparada.
 * La lectura y el guardado con DOM los escribes tu. Guia en el README, apartado 6.
 *
 * Orden recomendado:
 * 1. cargar(): si el fichero no existe, crear el XML base y devolver una lista vacia.
 * 2. cargar(): si existe, pasar cada elemento videojuego a un objeto Videojuego.
 * 3. guardar(): volcar la lista recibida a data/videojuegos.xml, indentado y en UTF-8.
 *
 * El id va en el atributo del elemento videojuego.
 * titulo, plataforma, genero, horasJugadas y finalizado van en elementos hijos.
 * Esta clase no muestra menu ni pide datos por teclado.
 */
public class XmlVideojuegoRepositorio implements IVideojuegoRepositorio {

    // Variables
    private final Path fichero;

    // Constructores
    public XmlVideojuegoRepositorio(String ruta) {
        this.fichero = Path.of(ruta);
    }

    public Path getFichero() {
        return fichero;
    }

    @Override
    public List<Videojuego> cargar() throws GameHubXmlException {
        throw new GameHubXmlException(
                "Carga XML pendiente. Implementala en XmlVideojuegoRepositorio.cargar().");
    }

    @Override
    public void guardar(List<Videojuego> videojuegos) throws GameHubXmlException {
        throw new GameHubXmlException(
                "Guardado XML pendiente. Implementalo en XmlVideojuegoRepositorio.guardar().");
    }
}
