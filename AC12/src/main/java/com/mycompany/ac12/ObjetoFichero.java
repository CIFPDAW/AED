package com.mycompany.ac12;

import java.io.File;

public class ObjetoFichero {

    private final File fichero;

    public ObjetoFichero(String nombreDelFichero) {
        // Comprobamos que el parámetro no sea nulo
        assert nombreDelFichero != null : "El nombre del fichero no puede ser nulo";

        // Inicializamos el objeto File con la ruta proporcionada
        this.fichero = new File(nombreDelFichero);
    }

    public String obtenerInformacion() {
        boolean existe = fichero.exists();

        StringBuilder sb = new StringBuilder()
                .append("\n--- RESULTADO ---\n")
                .append("Existe: ")
                .append(existe ? "Si" : "No")
                .append("\n");

        if (existe) {
            sb.append("Nombre: ")
                    .append(fichero.getName())
                    .append("\n")
                    .append("Se puede leer: ")
                    .append(fichero.canRead() ? "Si" : "No")
                    .append("\n")
                    .append("Tamaño: ")
                    .append(fichero.length())
                    .append(" bytes\n")
                    .append(fichero.isDirectory() ? "Tipo: Directorio\n" : fichero.isFile() ? "Tipo: Fichero\n" : "")
                    .append(fichero.isAbsolute() ? "Es una ruta absoluta" : "Es una ruta relativa")
                    .append(" (")
                    .append(fichero.getAbsolutePath())
                    .append(")\n");
        }

        return sb.toString();
    }
}
