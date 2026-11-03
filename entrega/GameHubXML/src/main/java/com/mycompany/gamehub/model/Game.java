package com.mycompany.gamehub.model;

public class Game {

    private int id;
    private String titulo;
    private String plataforma;
    // private List<String> plataformas;
    private String genero;
    private double horasJugadas;
    private boolean finalizado;

    public Game(int id, String titulo, String plataforma, String genero, double horasJugadas, boolean finalizado) {
        setId(id);
        setTitulo(titulo);
        setPlataforma(plataforma);
        setGenero(genero);
        setHorasJugadas(horasJugadas);
        setFinalizado(finalizado);
    }

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

    public void setId(int id) {
        assert id > 0 : "El ID debe ser mayor que 0";
        this.id = id;
    }

    public void setTitulo(String titulo) {
        assert titulo != null && !titulo.isBlank() : "El título no puede estar vacío";
        this.titulo = titulo;
    }

    public void setPlataforma(String plataforma) {
        assert plataforma != null && !plataforma.isBlank() : "La plataforma no puede estar vacía";
        this.plataforma = plataforma;
    }

    public void setGenero(String genero) {
        assert genero != null && !genero.isBlank() : "El género no puede estar vacío";
        this.genero = genero;
    }

    public void setHorasJugadas(double horasJugadas) {
        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";
        this.horasJugadas = horasJugadas;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

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
    
    // Si tuviera varias plataformas
    /*
    public List<String> getPlataformas() {
    return plataformas;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }
    */
}
