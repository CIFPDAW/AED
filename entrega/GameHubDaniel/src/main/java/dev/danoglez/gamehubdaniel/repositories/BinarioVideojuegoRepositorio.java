package dev.danoglez.gamehubdaniel.repositories;

import dev.danoglez.gamehubdaniel.exception.GameHubFileException;
import dev.danoglez.gamehubdaniel.models.Videojuego;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio binario: serializa la lista completa con ObjectInput/OutputStream.
 * Se reescribe el fichero entero (sin append) para evitar la doble cabecera del tema.
 */
public class BinarioVideojuegoRepositorio implements IVideojuegoRepositorio {

    // Variables
    private final File fichero;

    // Constructores
    public BinarioVideojuegoRepositorio(String ruta) {
        // Comprobamos que el parametro no sea nulo
        assert ruta != null : "La ruta del fichero no puede ser nula";

        // Inicializamos el objeto File con la ruta proporcionada
        this.fichero = new File(ruta);
    }

    // Metodos
    @Override
    @SuppressWarnings("unchecked")
    public List<Videojuego> cargar() throws GameHubFileException {
        if (!fichero.exists() || fichero.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero))) {
            return (List<Videojuego>) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GameHubFileException("No se pudo leer el fichero binario.", e);
        }
    }

    @Override
    public void guardar(List<Videojuego> videojuegos) throws GameHubFileException {
        assert videojuegos != null : "La lista de videojuegos no puede ser nula";

        File carpeta = fichero.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new GameHubFileException("No se pudo crear la carpeta del fichero binario.");
        }

        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero))) {
            // Guardamos una copia de la lista para no acoplar el fichero a la referencia en memoria
            salida.writeObject(new ArrayList<>(videojuegos));
        } catch (IOException e) {
            throw new GameHubFileException("No se pudo guardar el fichero binario.", e);
        }
    }
}
