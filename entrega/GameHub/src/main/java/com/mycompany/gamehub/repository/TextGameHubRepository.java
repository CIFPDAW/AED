package com.mycompany.gamehub.repository;

import com.mycompany.gamehub.exception.GameHubFileException;
import com.mycompany.gamehub.model.Game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextGameHubRepository implements IGameHubRepository {

    // Variables
    private final Path ruta;

    // Constructor
    public TextGameHubRepository() {
        this.ruta = Path.of("src", "main", "java", "com", "mycompany", "gamehub", "data", "games.txt");
    }

    // Cargar videojuegos del fichero de texto
    @Override
    public List<Game> cargar() throws GameHubFileException {

        List<Game> videojuegos = new ArrayList<>();

        if (!Files.exists(ruta)) {
            return videojuegos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ruta.toFile()))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(":", -1);

                if (datos.length != 6) {
                    throw new GameHubFileException(
                            "Formato incorrecto en una línea del fichero: " + linea
                    );
                }

                try {
                    int id = Integer.parseInt(datos[0]);
                    String titulo = datos[1];
                    String plataforma = datos[2];
                    String genero = datos[3];
                    double horasJugadas = Double.parseDouble(datos[4]);
                    boolean finalizado;

                    if (datos[5].equalsIgnoreCase("true")) {
                        finalizado = true;
                    } else if (datos[5].equalsIgnoreCase("false")) {
                        finalizado = false;
                    } else {
                        throw new IllegalArgumentException(
                                "El campo finalizado debe ser true o false."
                        );
                    }

                    Game videojuego = new Game(
                            id,
                            titulo,
                            plataforma,
                            genero,
                            horasJugadas,
                            finalizado
                    );

                    videojuegos.add(videojuego);

                } catch (IllegalArgumentException e) {
                    throw new GameHubFileException(
                            "Error en los datos de la línea: " + linea,
                            e
                    );
                }
            }

        } catch (IOException e) {
            throw new GameHubFileException(
                    "No se pudo leer el fichero de texto.", e);
        }

        return videojuegos;
    }

    // Guardar videojuegos en el fichero de texto
    @Override
    public void guardar(List<Game> videojuegos) throws GameHubFileException {

        try {
            Files.createDirectories(ruta.getParent());

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta.toFile()))) {

                for (Game videojuego : videojuegos) {

                    bw.write(
                            videojuego.getId() + ":"
                            + videojuego.getTitulo() + ":"
                            + videojuego.getPlataforma() + ":"
                            + videojuego.getGenero() + ":"
                            + videojuego.getHorasJugadas() + ":"
                            + videojuego.isFinalizado()
                    );

                    bw.newLine();
                }
            }

        } catch (IOException e) {
            throw new GameHubFileException(
                    "No se pudo guardar el fichero de texto.", e);
        }
    }
    
    /*
    @Override
    public List<Game> cargar() throws GameHubFileException {

        List<Game> videojuegos = new ArrayList<>();

        if (!Files.exists(ruta)) {
            return videojuegos;
        }

        try (FileReader fr = new FileReader(ruta.toFile())) {

            String linea = "";
            int caracter;

            while ((caracter = fr.read()) != -1) {

                if (caracter == '\n') {

                    if (!linea.isBlank()) {
                        String[] datos = linea.split(":", -1);

                        if (datos.length != 6) {
                            throw new GameHubFileException(
                                    "Formato incorrecto: " + linea
                            );
                        }

                        try {
                            int id = Integer.parseInt(datos[0]);
                            String titulo = datos[1];
                            String plataforma = datos[2];
                            String genero = datos[3];
                            double horas = Double.parseDouble(datos[4]);

                            boolean finalizado;

                            if (datos[5].trim().equalsIgnoreCase("true")) {
                                finalizado = true;
                            } else if (datos[5].trim().equalsIgnoreCase("false")) {
                                finalizado = false;
                            } else {
                                throw new IllegalArgumentException(
                                        "El campo finalizado debe ser true o false."
                                );
                            }

                            videojuegos.add(new Game(
                                    id, titulo, plataforma, genero,
                                    horas, finalizado
                            ));

                        } catch (IllegalArgumentException e) {
                            throw new GameHubFileException(
                                    "Error en los datos: " + linea, e
                            );
                        }
                    }

                    linea = "";

                } else if (caracter != '\r') {
                    linea += (char) caracter;
                }
            }

            // Procesar la última línea si el fichero no termina en salto de línea.
            if (!linea.isBlank()) {
                String[] datos = linea.split(":", -1);

                if (datos.length != 6) {
                    throw new GameHubFileException(
                            "Formato incorrecto: " + linea
                    );
                }

                try {
                    int id = Integer.parseInt(datos[0]);
                    String titulo = datos[1];
                    String plataforma = datos[2];
                    String genero = datos[3];
                    double horas = Double.parseDouble(datos[4]);

                    boolean finalizado;

                    if (datos[5].trim().equalsIgnoreCase("true")) {
                        finalizado = true;
                    } else if (datos[5].trim().equalsIgnoreCase("false")) {
                        finalizado = false;
                    } else {
                        throw new IllegalArgumentException(
                                "El campo finalizado debe ser true o false."
                        );
                    }

                    videojuegos.add(new Game(
                            id, titulo, plataforma, genero,
                            horas, finalizado
                    ));

                } catch (IllegalArgumentException e) {
                    throw new GameHubFileException(
                            "Error en los datos: " + linea, e
                    );
                }
            }

        } catch (IOException e) {
            throw new GameHubFileException(
                    "No se pudo leer el fichero de texto.", e
            );
        }

        return videojuegos;
    }
    */
}
