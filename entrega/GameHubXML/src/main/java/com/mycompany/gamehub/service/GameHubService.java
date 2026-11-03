package com.mycompany.gamehub.service;

import com.mycompany.gamehub.exception.GameHubXmlException;
import com.mycompany.gamehub.model.Game;
import com.mycompany.gamehub.repository.IGameHubRepository;
import com.mycompany.gamehub.repository.XmlGameHubRepository;

import java.util.ArrayList;
import java.util.List;

public class GameHubService {

    private List<Game> videojuegos;
    private final IGameHubRepository repositorio;
    private boolean hayCambios;

    public GameHubService() {
        this.videojuegos = new ArrayList<>();
        this.repositorio = new XmlGameHubRepository();
        this.hayCambios = false;
    }

    public int cargar() throws GameHubXmlException {
        videojuegos = repositorio.cargar();
        hayCambios = false;
        return videojuegos.size();
    }

    public void guardar() throws GameHubXmlException {
        repositorio.guardar(listar());
        hayCambios = false;
    }

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

    public Game buscarPorId(int id) {

        for (Game videojuego : videojuegos) {
            if (videojuego.getId() == id) {
                return videojuego;
            }
        }

        return null;
    }

    public Game buscarEnXml(int id) throws GameHubXmlException {
        return repositorio.buscarPorId(id);
    }

    public List<Game> listar() {
        List<Game> listaOrdenada = new ArrayList<>(videojuegos);
        listaOrdenada.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        return listaOrdenada;
    }

    public void modificar(int id, String titulo, String plataforma, String genero, double horasJugadas, boolean finalizado) {

        Game videojuego = buscarPorId(id);

        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }


        Game validado = new Game(id, titulo, plataforma, genero, horasJugadas, finalizado);

        videojuego.setTitulo(validado.getTitulo());
        videojuego.setPlataforma(validado.getPlataforma());
        videojuego.setGenero(validado.getGenero());
        videojuego.setHorasJugadas(validado.getHorasJugadas());
        videojuego.setFinalizado(validado.isFinalizado());

        hayCambios = true;
    }

    public void eliminar(int id) {

        Game videojuego = buscarPorId(id);

        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }

        videojuegos.remove(videojuego);
        hayCambios = true;
    }

    public boolean hayCambios() {
        return hayCambios;
    }
}
