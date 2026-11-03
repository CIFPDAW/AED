package dev.danoglez.services;

import dev.danoglez.exception.VideojuegoFileException;
import dev.danoglez.models.Videojuego;
import dev.danoglez.repositories.IVideojuegoRepository;
import dev.danoglez.repositories.XMLVideojuegoRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio: logica de negocio y coleccion en memoria.
 * El CRUD se hace sobre el ArrayList. El XML solo se toca al cargar, guardar o buscar en fichero.
 */
public class VideojuegoService {

    // Variables
    private List<Videojuego> videojuegos;
    private final IVideojuegoRepository repositorio;
    private boolean hayCambios;

    // Constructores
    public VideojuegoService() {
        this(new XMLVideojuegoRepository("data/videojuegos.xml"));
    }

    public VideojuegoService(IVideojuegoRepository repositorio) {
        this.videojuegos = new ArrayList<>();
        this.repositorio = repositorio;
        this.hayCambios = false;
    }

    // Metodos
    public int cargarDesdeXml() throws VideojuegoFileException {
        videojuegos = repositorio.cargar();
        hayCambios = false;
        return videojuegos.size();
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

    public List<Videojuego> buscarPorPlataforma(String plataforma) {
        List<Videojuego> MismaPlataforma = new ArrayList<>();
        for (Videojuego videojuego : videojuegos) {
            if (videojuego.getPlataforma().toUpperCase().equals(plataforma)) {
                MismaPlataforma.add(videojuego);
            }
        }

        return MismaPlataforma;
    }

    public List<Videojuego> listar() {
        List<Videojuego> copia = new ArrayList<>(videojuegos);
        copia.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        return copia;
    }

    public void modificar(int id, String titulo, String plataforma, String genero,
            double horasJugadas, boolean finalizado, int valoracion) {
        Videojuego videojuego = buscarPorId(id);
        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }

        validarDatos(titulo, plataforma, genero, horasJugadas);

        videojuego.setTitulo(titulo);
        videojuego.setPlataforma(plataforma);
        videojuego.setGenero(genero);
        videojuego.setHorasJugadas(horasJugadas);
        videojuego.setFinalizado(finalizado);
        videojuego.setValoracion(valoracion);
        hayCambios = true;
    }

    public String estadisticas(){
        double horasJugadas = 0.0;
        int finalizados = 0;
        int juegos = 0;

        for (Videojuego videojuego : videojuegos) {
            horasJugadas += videojuego.getHorasJugadas();
            juegos++;
            if(videojuego.isFinalizado()){
                finalizados++;
            }
        }

        return juegos + " juegos, " + finalizados + " finalizado, " + horasJugadas+ " horas.";
    }

    public void eliminar(int id) {
        Videojuego videojuego = buscarPorId(id);
        if (videojuego == null) {
            throw new IllegalArgumentException("No existe un videojuego con ese ID.");
        }
        videojuegos.remove(videojuego);
        hayCambios = true;
    }

    public void guardarEnXml() throws VideojuegoFileException {
        repositorio.guardar(videojuegos);
        hayCambios = false;
    }

    public boolean hayCambios() {
        return hayCambios;
    }

    public void validarDatos(String titulo, String plataforma, String genero, double horasJugadas) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        if (plataforma == null || plataforma.isBlank()) {
            throw new IllegalArgumentException("La plataforma no puede estar vacia.");
        }
        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("El genero no puede estar vacio.");
        }
        if (horasJugadas < 0) {
            throw new IllegalArgumentException("Las horas jugadas no pueden ser negativas.");
        }
    }
}
