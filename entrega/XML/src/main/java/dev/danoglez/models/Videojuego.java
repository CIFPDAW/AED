package dev.danoglez.models;

/**
 * Modelo de videojuego para GameHub XML.
 * Solo guarda datos: no pide teclado, no imprime y no conoce el fichero.
 */
public class Videojuego {

    // Variables
    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private int valoracion;
    private double horasJugadas;
    private boolean finalizado;

    // Constructores
    public Videojuego(int id, String titulo, String plataforma, String genero,
            double horasJugadas, boolean finalizado, int valoracion) {
        assert id > 0 : "El ID debe ser mayor que 0";
        assert titulo != null && !titulo.isBlank() : "El titulo no puede estar vacio";
        assert plataforma != null && !plataforma.isBlank() : "La plataforma no puede estar vacia";
        assert genero != null && !genero.isBlank() : "El genero no puede estar vacio";
        assert valoracion < 10 && valoracion > 0 : "El valoracion debe ser mayor que 0 y menor que 10";
        assert valoracion == 0 : "Sin valor";
        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";

        this.id = id;
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.valoracion = valoracion;
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

    // Setters. El id no se cambia: el enunciado no recomienda modificarlo.
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

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public String toString() {
        String valorar;
        if(valoracion == 0){
             valorar = "Sin valorar";
        }else{
            valorar = String.valueOf(valoracion);
        }
        return "Id: " + id
                + " | Titulo: " + titulo
                + " | Plataforma: " + plataforma
                + " | Genero: " + genero
                + " | Valoracion: " + valorar
                + " | Horas: " + horasJugadas
                + " | Finalizado: " + finalizado;
    }
}
