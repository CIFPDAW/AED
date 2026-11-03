package com.mycompany.gamehub.model;

import java.io.Serializable;

public class Game implements Serializable {

    // Variables
    private int id;

    private String titulo;

    private String plataforma;

    private String genero;

    private double horasJugadas;

    private boolean finalizado;

    // Constructores
    public Game(int id, String titulo, String plataforma, String genero, double horasJugadas, boolean finalizado) {
        
        // Condiciones
        assert id > 0 : "El ID debe ser mayor que 0";
        
        assert titulo != null && !titulo.isBlank() : "El título no puede estar vacío";
        
        assert plataforma != null && !plataforma.isBlank() : "La plataforma no puede estar vacía";
        
        assert genero != null && !genero.isBlank() : "El género no puede estar vacío";
        
        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";
        
        // Rellenar variables
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
    public void setId(int id) {
        this.id = id;
    }

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

    // Descripción del objeto
    @Override
    public String toString() {
        return "Videojuego{" + "id=" + id
                + ", titulo='" + titulo + '\''
                + ", plataforma='" + plataforma + '\''
                + ", genero='" + genero + '\''
                + ", horasJugadas=" + horasJugadas
                + ", finalizado=" + finalizado
                + '}';
    }

}
