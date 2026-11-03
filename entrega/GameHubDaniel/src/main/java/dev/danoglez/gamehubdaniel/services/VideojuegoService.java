package dev.danoglez.gamehubdaniel.services;

import dev.danoglez.gamehubdaniel.exception.GameHubFileException;
import dev.danoglez.gamehubdaniel.models.Videojuego;
import dev.danoglez.gamehubdaniel.repositories.BinarioVideojuegoRepositorio;
import dev.danoglez.gamehubdaniel.repositories.IVideojuegoRepositorio;
import dev.danoglez.gamehubdaniel.repositories.TextoVideojuegoRepositorio;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio: logica de negocio y coleccion en memoria.
 * El CRUD se hace sobre el ArrayList; los repositorios solo cargan/guardan.
 */
public class VideojuegoService {

    // Variables
    private List<Videojuego> videojuegos;
    private final IVideojuegoRepositorio repositorioTexto;
    private final IVideojuegoRepositorio repositorioBinario;
    private boolean hayCambios;

    // Constructores
    public VideojuegoService() {
        this.videojuegos = new ArrayList<>();
        this.repositorioTexto = new TextoVideojuegoRepositorio("data/videojuegos.txt");
        this.repositorioBinario = new BinarioVideojuegoRepositorio("data/videojuegos.dat");
        this.hayCambios = false;
    }

    // Metodos
    public void cargarDesdeTexto() throws GameHubFileException {
        videojuegos = repositorioTexto.cargar();
        hayCambios = false;
    }

    public void cargarDesdeBinario() throws GameHubFileException {
        videojuegos = repositorioBinario.cargar();
        hayCambios = false;
    }

    public void anadir(Videojuego videojuego) {
        assert videojuego != null : "El videojuego no puede ser nulo";

        if (buscarPorId(videojuego.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un videojuego con ese ID.");
        }

        videojuegos.add(videojuego);
        hayCambios = true;
    }

    public Videojuego buscarPorId(int id) {
        for (Videojuego videojuego : videojuegos) {
            if (videojuego.getId() == id) {
                return videojuego;
            }
        }
        return null;
    }

    public List<Videojuego> listar() {
        List<Videojuego> copia = new ArrayList<>(videojuegos);
        copia.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        return copia;
    }

    public void modificar(int id, String titulo, String plataforma, String genero,
            double horasJugadas, boolean finalizado) {
        Videojuego videojuego = buscarPorId(id);
        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }

        assert titulo != null && !titulo.isBlank() : "El titulo no puede estar vacio";
        assert plataforma != null && !plataforma.isBlank() : "La plataforma no puede estar vacia";
        assert genero != null && !genero.isBlank() : "El genero no puede estar vacio";
        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";

        // No modificamos el id, como recomienda el enunciado
        videojuego.setTitulo(titulo);
        videojuego.setPlataforma(plataforma);
        videojuego.setGenero(genero);
        videojuego.setHorasJugadas(horasJugadas);
        videojuego.setFinalizado(finalizado);
        hayCambios = true;
    }

    public void eliminar(int id) {
        Videojuego videojuego = buscarPorId(id);
        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }
        videojuegos.remove(videojuego);
        hayCambios = true;
    }

    public void guardarEnTexto() throws GameHubFileException {
        repositorioTexto.guardar(videojuegos);
        hayCambios = false;
    }

    public void guardarEnBinario() throws GameHubFileException {
        repositorioBinario.guardar(videojuegos);
        hayCambios = false;
    }

    // Guarda en los dos ficheros principales, usado al salir si el usuario confirma
    public void guardarEnAmbos() throws GameHubFileException {
        repositorioTexto.guardar(videojuegos);
        repositorioBinario.guardar(videojuegos);
        hayCambios = false;
    }

    public boolean hayCambios() {
        return hayCambios;
    }

    public boolean hayDatosEnMemoria() {
        return !videojuegos.isEmpty();
    }

    /**
     * EXTRA: une TXT y DAT con dos recorridos.
     * Si el id solo esta en uno, se anade.
     * Si el id esta en los dos, el del texto se queda y el del DAT se copia al final con un id nuevo,
     * aunque el titulo sea el mismo.
     */
    public void sincronizar() throws GameHubFileException {
        List<Videojuego> texto = repositorioTexto.cargar();
        List<Videojuego> binario = repositorioBinario.cargar();
        texto.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        binario.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        List<Videojuego> resultado = new ArrayList<>();
        List<Videojuego> alFinal = new ArrayList<>();
        int i = 0;
        int j = 0;

        // Recorremos los dos ficheros a la vez, como una mezcla ordenada por id
        while (i < texto.size() && j < binario.size()) {
            Videojuego delTexto = texto.get(i);
            Videojuego delBinario = binario.get(j);

            if (delTexto.getId() < delBinario.getId()) {
                resultado.add(delTexto);
                i++;
            } else if (delTexto.getId() > delBinario.getId()) {
                resultado.add(delBinario);
                j++;
            } else {
                // Mismo id: el del texto se queda y el del DAT se copia al final
                resultado.add(delTexto);
                alFinal.add(delBinario);
                i++;
                j++;
            }
        }

        // Lo que quede de uno de los dos ficheros
        while (i < texto.size()) {
            resultado.add(texto.get(i));
            i++;
        }
        while (j < binario.size()) {
            resultado.add(binario.get(j));
            j++;
        }

        // Los ids que coincidian van al final, con id nuevo
        int maxId = resultado.isEmpty() ? 0 : resultado.get(resultado.size() - 1).getId();
        for (Videojuego pendiente : alFinal) {
            maxId++;
            resultado.add(new Videojuego(maxId, pendiente.getTitulo(), pendiente.getPlataforma(),
                    pendiente.getGenero(), pendiente.getHorasJugadas(), pendiente.isFinalizado()));
        }

        repositorioTexto.guardar(resultado);
        repositorioBinario.guardar(resultado);
        videojuegos = resultado;
        hayCambios = false;
    }
}
