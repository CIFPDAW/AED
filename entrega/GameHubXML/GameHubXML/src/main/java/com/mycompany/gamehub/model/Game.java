package com.mycompany.gamehub.model;

/**
 * MODELO: representa UN videojuego.
 *
 * Solo guarda datos y comprueba que sean válidos. No lee del teclado,
 * no muestra nada por pantalla y no sabe nada del XML (eso lo hace el repositorio).
 *
 * Si un dato no es válido se lanza IllegalArgumentException; el controlador
 * la captura y la vista muestra el mensaje.
 */
public class Game {

    // Variables (privadas)
    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private double horasJugadas;
    private boolean finalizado;

    // Constructor: usa los setters para que se apliquen las validaciones
    public Game(int id, String titulo, String plataforma, String genero, double horasJugadas, boolean finalizado) {
        setId(id);
        setTitulo(titulo);
        setPlataforma(plataforma);
        setGenero(genero);
        setHorasJugadas(horasJugadas);
        setFinalizado(finalizado);
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

    // Setters con validaciones básicas
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        }
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = validarTexto(titulo, "El título");
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = validarTexto(plataforma, "La plataforma");
    }

    public void setGenero(String genero) {
        this.genero = validarTexto(genero, "El género");
    }

    public void setHorasJugadas(double horasJugadas) {
        if (horasJugadas < 0) {
            throw new IllegalArgumentException("Las horas jugadas no pueden ser negativas.");
        }
        this.horasJugadas = horasJugadas;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    // Comprueba que un texto no sea null ni esté vacío y le quita espacios de los lados
    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    // Descripción del objeto
    @Override
    public String toString() {
        return String.format("[%d] %s | Plataforma: %s | Género: %s | Horas: %.1f | Finalizado: %s",
                id, titulo, plataforma, genero, horasJugadas, finalizado ? "Sí" : "No");
    }
}
