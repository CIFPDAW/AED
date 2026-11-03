package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubFileException;
import com.mycompany.gamehub.model.Game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BinaryGameHubRepository implements IGameHubRepository {

    // Variables
    private final Path ruta;

    // Constructor
    public BinaryGameHubRepository() {
        this.ruta = Path.of("src", "main", "java",
                "com", "mycompany", "gamehub", "data", "games.dat");
    }

    // Cargar videojuegos del fichero binario
    @Override
    public List<Game> cargar() throws GameHubFileException {

        if (!Files.exists(ruta) || ruta.toFile().length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta.toFile()))) {

            return (List<Game>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new GameHubFileException(
                    "No se pudo leer el fichero binario.", e);
        }
    }

    // Guardar videojuegos en el fichero binario
    @Override
    public void guardar(List<Game> videojuegos) throws GameHubFileException {

        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta.toFile()))) {

                oos.writeObject(new ArrayList<>(videojuegos));
            }

        } catch (IOException e) {
            throw new GameHubFileException(
                    "No se pudo guardar el fichero binario.", e);
        }
    }
}
