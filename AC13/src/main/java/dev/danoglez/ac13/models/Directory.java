package dev.danoglez.ac13.models;

import java.io.File;

public class Directory {

    // Variables
    private final File directorio;

    // Constructores
    public Directory(String nombreDelDirectorio) throws Exception {
        assert nombreDelDirectorio != null : "La ruta del directorio no puede ser nula";

        this.directorio = new File(nombreDelDirectorio);

        if (!this.directorio.isDirectory()) {
            throw new Exception("La ruta no es un directorio");
        }
    }

    // Metodos
    public StringBuilder obtenerTodosLosElementosDelDirectorio() {
        StringBuilder sb = new StringBuilder();

        File[] elementos = directorio.listFiles();
        
        for (File elemento : elementos) {
            if (elemento.isDirectory()) {
                sb.append("\n-> Es un Directorio: " + elemento.getName());
            } else if (elemento.isFile()) {
                sb.append("\n-> Es un Archivo: " + elemento.getName());
            }
        }
        
        return sb;
    }

    // AC1.3 ampliacion: recorre todo el arbol con java.io.File, igual que listFiles() del apartado 2.1.
    public StringBuilder obtenerArbolDeDirectorios() {
        StringBuilder sb = new StringBuilder();
        recorrerArbol(directorio, sb, 0);
        return sb;
    }

    private void recorrerArbol(File actual, StringBuilder sb, int nivel) {
        File[] elementos = actual.listFiles();
        if (elementos == null) {
            return;
        }

        for (File elemento : elementos) {
            sb.append("  ".repeat(nivel));
            if (elemento.isDirectory()) {
                sb.append("-> Es un Directorio: ").append(elemento.getName()).append("\n");
                recorrerArbol(elemento, sb, nivel + 1);
            } else if (elemento.isFile()) {
                sb.append("-> Es un Archivo: ").append(elemento.getName()).append("\n");
            }
        }
    }
}
