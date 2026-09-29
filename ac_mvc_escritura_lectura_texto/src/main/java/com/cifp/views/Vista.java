package com.cifp.views;

/** Contrato común de las vistas de la aplicación. */
public interface Vista extends AutoCloseable {
    
    // Devuelve el texto, o null si se cancela o termina la entrada. 
    String pedirTexto();
    void mostrarTexto(String texto);
    void mostrarError(String mensaje);
    
    @Override
    void close();
}
