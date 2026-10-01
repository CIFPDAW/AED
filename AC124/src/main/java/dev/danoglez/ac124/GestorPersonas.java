package dev.danoglez.ac124;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * AC1.24. Se reescribe la lista completa.
 * ObjectOutputStream escribe una cabecera al crearse, así que no se abre el fichero en append.
 */
public class GestorPersonas {

    // Variables
    private final File fichero;

    // Constructores
    public GestorPersonas(File fichero) {
        assert fichero != null : "El fichero no puede ser nulo";
        this.fichero = fichero;
    }

    // Metodos
    public void guardar(ArrayList<Persona> personas) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichero))) {
            out.writeObject(personas);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Persona> recuperar() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichero))) {
            return (ArrayList<Persona>) in.readObject();
        }
    }
}
