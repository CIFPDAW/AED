package dev.danoglez.ac14;

import java.io.File;
import java.io.IOException;

/**
 * AC1.4. Crea un directorio y un fichero con java.io.File, en el directorio del proyecto.
 */
public class GestorProyecto {

    // Variables
    private final File directorio;
    private final File fichero;

    // Constructores
    public GestorProyecto(String nombreDirectorio, String nombreFichero) {
        assert nombreDirectorio != null : "El nombre del directorio no puede ser nulo";
        assert nombreFichero != null : "El nombre del fichero no puede ser nulo";

        this.directorio = new File(nombreDirectorio);
        this.fichero = new File(this.directorio, nombreFichero);
    }

    // Metodos
    public void crear() throws IOException {
        if (!directorio.exists() && !directorio.mkdir()) {
            throw new IOException("No se pudo crear el directorio " + directorio.getPath());
        }
        if (!fichero.exists() && !fichero.createNewFile()) {
            throw new IOException("No se pudo crear el fichero " + fichero.getPath());
        }
    }

    public void borrar() {
        if (fichero.exists()) {
            fichero.delete();
        }
        if (directorio.exists()) {
            directorio.delete();
        }
    }

    public String obtenerInformacion() {
        return new StringBuilder()
                .append("Directorio: ").append(directorio.getAbsolutePath())
                .append(" (existe: ").append(directorio.exists() ? "Si" : "No").append(")\n")
                .append("Fichero: ").append(fichero.getAbsolutePath())
                .append(" (existe: ").append(fichero.exists() ? "Si" : "No").append(")\n")
                .toString();
    }
}
