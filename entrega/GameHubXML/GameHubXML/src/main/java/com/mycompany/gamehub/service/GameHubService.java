package com.mycompany.gamehub.service;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;
import com.mycompany.gamehub.repository.IGameHubRepository;
import com.mycompany.gamehub.repository.XmlGameHubRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICIO: gestiona la lista de videojuegos EN MEMORIA y aplica las reglas
 * de negocio (no repetir IDs, etc.).
 *
 * Funcionamiento general:
 *  - cargar()  -> el repositorio lee el XML y nos da un ArrayList<Game>.
 *  - añadir / modificar / eliminar -> SOLO cambian la lista en memoria.
 *  - guardar() -> el repositorio escribe la lista entera en el XML.
 *
 * Por eso existe "hayCambios": para saber si la memoria y el fichero
 * son distintos y avisar al salir.
 */
public class GameHubService {

    // Variables
    private List<Game> videojuegos;
    private final IGameHubRepository repositorio;
    private boolean hayCambios;

    // Constructor
    public GameHubService() {
        this.videojuegos = new ArrayList<>();
        this.repositorio = new XmlGameHubRepository();
        this.hayCambios = false;
    }

    // Cargar videojuegos desde el XML. Devuelve cuántos se han cargado.
    public int cargar() throws GameHubXmlException {
        videojuegos = repositorio.cargar();
        hayCambios = false;
        return videojuegos.size();
    }

    // Guardar la lista en memoria en el XML
    public void guardar() throws GameHubXmlException {
        repositorio.guardar(listar()); // se guarda ordenado por ID
        hayCambios = false;
    }

    // Añadir videojuego
    public void anadir(Game videojuego) {

        if (videojuego == null) {
            throw new IllegalArgumentException("El videojuego no puede ser null.");
        }

        if (buscarPorId(videojuego.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un videojuego con ese ID.");
        }

        videojuegos.add(videojuego);
        hayCambios = true;
    }

    // Buscar videojuego por ID (en memoria)
    public Game buscarPorId(int id) {

        for (Game videojuego : videojuegos) {
            if (videojuego.getId() == id) {
                return videojuego;
            }
        }

        return null;
    }

    // Buscar videojuego por ID directamente en el fichero XML (con XPath)
    public Game buscarEnXml(int id) throws GameHubXmlException {
        return repositorio.buscarPorId(id);
    }

    // Obtener todos los videojuegos ordenados por ID
    public List<Game> listar() {
        List<Game> listaOrdenada = new ArrayList<>(videojuegos);
        listaOrdenada.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        return listaOrdenada;
    }

    // Modificar videojuego
    public void modificar(int id, String titulo, String plataforma, String genero, double horasJugadas, boolean finalizado) {

        Game videojuego = buscarPorId(id);

        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }

        // Primero se VALIDAN los datos nuevos creando un Game de prueba.
        // Si algo está mal salta la excepción aquí y el original no se toca
        // (así no se queda "a medias", con el título cambiado pero las horas no).
        Game validado = new Game(id, titulo, plataforma, genero, horasJugadas, finalizado);

        videojuego.setTitulo(validado.getTitulo());
        videojuego.setPlataforma(validado.getPlataforma());
        videojuego.setGenero(validado.getGenero());
        videojuego.setHorasJugadas(validado.getHorasJugadas());
        videojuego.setFinalizado(validado.isFinalizado());

        hayCambios = true;
    }

    // Eliminar videojuego
    public void eliminar(int id) {

        Game videojuego = buscarPorId(id);

        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }

        videojuegos.remove(videojuego);
        hayCambios = true;
    }

    // Consultar si hay cambios sin guardar
    public boolean hayCambios() {
        return hayCambios;
    }
}
