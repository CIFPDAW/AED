package dev.danoglez.gamehubdaniel.models;

import java.io.Serializable;

/**
 * Modelo de videojuego para GameHub.
 * Implementa Serializable para poder guardarlo con ObjectOutputStream, apartado 7 del tema.
 */
public class Videojuego implements Serializable {

    private static final long serialVersionUID = 1L;

    // Variables
    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private double horasJugadas;
    private boolean finalizado;

    // Constructores
    public Videojuego(int id, String titulo, String plataforma, String genero,
            double horasJugadas, boolean finalizado) {
        // Comprobamos que los datos basicos sean correctos
        assert id > 0 : "El ID debe ser mayor que 0";
        assert titulo != null && !titulo.isBlank() : "El titulo no puede estar vacio";
        assert plataforma != null && !plataforma.isBlank() : "La plataforma no puede estar vacia";
        assert genero != null && !genero.isBlank() : "El genero no puede estar vacio";
        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";

        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.horasJugadas = horasJugadas;
        this.finalizado = finalizado;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getGenero() {
        return genero;
    }

    public double getHorasJugadas() {
        return horasJugadas;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    // Setters
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setHorasJugadas(double horasJugadas) {
        this.horasJugadas = horasJugadas;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    // Descripcion del objeto
    @Override
    public String toString() {
        return "Id: " + id
                + " | Titulo: " + titulo
                + " | Plataforma: " + plataforma
                + " | Genero: " + genero
                + " | Horas: " + horasJugadas
                + " | Finalizado: " + finalizado;
    }
}
