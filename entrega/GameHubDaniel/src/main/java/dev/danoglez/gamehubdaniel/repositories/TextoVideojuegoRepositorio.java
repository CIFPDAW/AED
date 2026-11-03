package dev.danoglez.gamehubdaniel.repositories;

import dev.danoglez.gamehubdaniel.exception.GameHubFileException;
import dev.danoglez.gamehubdaniel.models.Videojuego;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de texto: cada linea es id:titulo:plataforma:genero:horasJugadas:finalizado.
 * Usa BufferedReader/BufferedWriter, apartado 5.2 del tema.
 */
public class TextoVideojuegoRepositorio implements IVideojuegoRepositorio {

    // Variables
    private final File fichero;

    // Constructores
    public TextoVideojuegoRepositorio(String ruta) {
        // Comprobamos que el parametro no sea nulo
        assert ruta != null : "La ruta del fichero no puede ser nula";

        // Inicializamos el objeto File con la ruta proporcionada
        this.fichero = new File(ruta);
    }

    // Metodos
    @Override
    public List<Videojuego> cargar() throws GameHubFileException {
        List<Videojuego> videojuegos = new ArrayList<>();

        if (!fichero.exists()) {
            return videojuegos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(":", -1);
                if (datos.length != 6) {
                    throw new GameHubFileException("Formato incorrecto en la linea: " + linea);
                }

                try {
                    int id = Integer.parseInt(datos[0].trim());
                    String titulo = datos[1].trim();
                    String plataforma = datos[2].trim();
                    String genero = datos[3].trim();
                    double horasJugadas = Double.parseDouble(datos[4].trim());
                    boolean finalizado = parsearBoolean(datos[5].trim(), linea);

                    videojuegos.add(new Videojuego(
                            id, titulo, plataforma, genero, horasJugadas, finalizado));
                } catch (IllegalArgumentException e) {
                    throw new GameHubFileException("Error en los datos de la linea: " + linea, e);
                }
            }
        } catch (IOException e) {
            throw new GameHubFileException("No se pudo leer el fichero de texto.", e);
        }

        return videojuegos;
    }

    @Override
    public void guardar(List<Videojuego> videojuegos) throws GameHubFileException {
        assert videojuegos != null : "La lista de videojuegos no puede ser nula";

        File carpeta = fichero.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new GameHubFileException("No se pudo crear la carpeta del fichero de texto.");
        }

        // Sobreescribe el fichero completo con la lista actual en memoria
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero))) {
            for (Videojuego videojuego : videojuegos) {
                writer.write(videojuego.getId() + ":"
                        + videojuego.getTitulo() + ":"
                        + videojuego.getPlataforma() + ":"
                        + videojuego.getGenero() + ":"
                        + videojuego.getHorasJugadas() + ":"
                        + videojuego.isFinalizado());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new GameHubFileException("No se pudo guardar el fichero de texto.", e);
        }
    }

    private boolean parsearBoolean(String valor, String linea) {
        if (valor.equalsIgnoreCase("true")) {
            return true;
        }
        if (valor.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException(
                "El campo finalizado debe ser true o false. Linea: " + linea);
    }

    /*
     * EXTRA / VARIANTE (no lo pide el enunciado tal cual):
     * Lectura caracter a caracter con FileReader, como el apartado 5.1.
     * Sirve para ver la diferencia con BufferedReader.readLine().
     *
    public List<Videojuego> cargarCaracterACaracter() throws GameHubFileException {
        List<Videojuego> videojuegos = new ArrayList<>();
        if (!fichero.exists()) {
            return videojuegos;
        }

        try (FileReader reader = new FileReader(fichero)) {
            StringBuilder linea = new StringBuilder();
            int caracter;
            while ((caracter = reader.read()) != -1) {
                if (caracter == '\n') {
                    if (!linea.toString().isBlank()) {
                        // Aqui se parsearia la linea igual que en cargar()
                    }
                    linea.setLength(0);
                } else if (caracter != '\r') {
                    linea.append((char) caracter);
                }
            }
            // Procesar la ultima linea si el fichero no termina en salto
        } catch (IOException e) {
            throw new GameHubFileException("No se pudo leer el fichero de texto.", e);
        }
        return videojuegos;
    }
     */
}
