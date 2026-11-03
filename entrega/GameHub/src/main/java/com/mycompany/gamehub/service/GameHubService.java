package com.mycompany.gamehub.service;

import com.mycompany.gamehub.exception.GameHubFileException;
import com.mycompany.gamehub.model.Game;
import com.mycompany.gamehub.repository.BinaryGameHubRepository;
import com.mycompany.gamehub.repository.IGameHubRepository;
import com.mycompany.gamehub.repository.TextGameHubRepository;

import java.util.ArrayList;
import java.util.List;

public class GameHubService {

    // Variables
    private List<Game> videojuegos;
    private final IGameHubRepository repositorioTexto;
    private final IGameHubRepository repositorioBinario;
    private boolean hayCambios;

    // Constructor
    public GameHubService() {
        this.videojuegos = new ArrayList<>();
        this.repositorioTexto = new TextGameHubRepository();
        this.repositorioBinario = new BinaryGameHubRepository();
        this.hayCambios = false;
    }

    // Cargar videojuegos desde el fichero de texto
    public void cargarDesdeTexto() throws GameHubFileException {
        videojuegos = repositorioTexto.cargar();
        hayCambios = false;
    }

    // Cargar videojuegos desde el fichero binario
    public void cargarDesdeBinario() throws GameHubFileException {
        videojuegos = repositorioBinario.cargar();
        hayCambios = false;
    }

    // Añadir videojuego
    public void anadir(Game videojuego) {

        assert videojuego != null : "El videojuego no puede ser null";

        if (buscarPorId(videojuego.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un videojuego con ese ID."
            );
        }

        assert !videojuego.getTitulo().isBlank() : "El título no puede estar vacío";

        assert videojuego.getHorasJugadas() >= 0 : "Las horas jugadas no pueden ser negativas";

        videojuegos.add(videojuego);
        hayCambios = true;
    }

    // Buscar videojuego por ID
    public Game buscarPorId(int id) {

        for (Game videojuego : videojuegos) {
            if (videojuego.getId() == id) {
                return videojuego;
            }
        }

        return null;
    }

    // Obtener todos los videojuegos
    public List<Game> listar() {
        List<Game> listaOrdenada = new ArrayList<>(videojuegos);

        listaOrdenada.sort(
                (a, b) -> Integer.compare(a.getId(), b.getId())
        );

        return listaOrdenada;
    }

    // Modificar videojuego
    public void modificar(int id, String titulo, String plataforma, String genero, double horasJugadas, boolean finalizado) {

        Game videojuego = buscarPorId(id);

        if (videojuego == null) {
            throw new IllegalArgumentException(
                    "No existe un videojuego con ese ID."
            );
        }

        assert titulo != null && !titulo.isBlank() : "El título no puede estar vacío";

        assert plataforma != null && !plataforma.isBlank() : "La plataforma no puede estar vacía";

        assert genero != null && !genero.isBlank() : "El género no puede estar vacío";

        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";

        videojuego.setTitulo(titulo);
        videojuego.setPlataforma(plataforma);
        videojuego.setGenero(genero);
        videojuego.setHorasJugadas(horasJugadas);
        videojuego.setFinalizado(finalizado);

        hayCambios = true;
    }

    // Eliminar videojuego
    public void eliminar(int id) {

        Game videojuego = buscarPorId(id);

        if (videojuego == null) {
            throw new IllegalArgumentException(
                    "No existe un videojuego con ese ID."
            );
        }

        videojuegos.remove(videojuego);
        hayCambios = true;
    }

    // Guardar videojuegos en el fichero de texto
    public void guardarEnTexto() throws GameHubFileException {
        repositorioTexto.guardar(videojuegos);
        hayCambios = false;
    }

    // Guardar videojuegos en el fichero binario
    public void guardarEnBinario() throws GameHubFileException {
        repositorioBinario.guardar(videojuegos);
        hayCambios = false;
    }

    // Sincronizar los juegos del fichero de texto y binario
    public void sincronizar() throws GameHubFileException {

        List<Game> juegosTexto = repositorioTexto.cargar();
        List<Game> juegosBinario = repositorioBinario.cargar();

        juegosTexto.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        juegosBinario.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        List<Game> juegosSincronizados = new ArrayList<>(juegosTexto);

        for (Game juegoBinario : juegosBinario) {

            boolean existe = false;

            for (Game juegoTexto : juegosTexto) {

                if (juegoTexto.getId() == juegoBinario.getId()) {
                    existe = true;
                    break;
                }

                if (juegoTexto.getId() > juegoBinario.getId()) {
                    break;
                }
            }

            if (!existe) {
                juegosSincronizados.add(juegoBinario);
            }
        }

        juegosSincronizados.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        // Guardar en ambos ficheros
        repositorioTexto.guardar(juegosSincronizados);
        repositorioBinario.guardar(juegosSincronizados);

        videojuegos = juegosSincronizados;
        hayCambios = false;
    }

    // Consultar si hay cambios sin guardar
    public boolean hayCambios() {
        return hayCambios;
    }

    // ----------------------------------------
    // Sincronizar los juegos del fichero de texto y binario
    /*
    public void sincronizar() throws GameHubFileException {

        List<Game> juegosTexto = repositorioTexto.cargar();
        List<Game> juegosBinario = repositorioBinario.cargar();


        juegosTexto.sort((j1, j2)
                -> Integer.compare(j1.getId(), j2.getId()));

        juegosBinario.sort((j1, j2)
                -> Integer.compare(j1.getId(), j2.getId()));

        List<Game> juegosSincronizados = new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < juegosTexto.size()
                && j < juegosBinario.size()) {

            Game juegoTexto = juegosTexto.get(i);
            Game juegoBinario = juegosBinario.get(j);

            if (juegoTexto.getId() < juegoBinario.getId()) {

                juegosSincronizados.add(juegoTexto);
                i++;

            } else if (juegoTexto.getId() > juegoBinario.getId()) {

                juegosSincronizados.add(juegoBinario);
                j++;

            } else {

                juegosSincronizados.add(juegoTexto);

                i++;
                j++;
            }
        }

        while (i < juegosTexto.size()) {
            juegosSincronizados.add(juegosTexto.get(i));
            i++;
        }

        while (j < juegosBinario.size()) {
            juegosSincronizados.add(juegosBinario.get(j));
            j++;
        }

        repositorioTexto.guardar(juegosSincronizados);
        repositorioBinario.guardar(juegosSincronizados);

        videojuegos = juegosSincronizados;

        hayCambios = false;
    }
     */
}
