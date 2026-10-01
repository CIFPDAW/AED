package dev.danoglez.ac125;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * AC1.25. Guardar y recuperar reescriben la colección completa.
 * devolverMorosos lee el fichero que recibe, no la lista que haya en memoria.
 */
public class GestorClientes {

    // Variables
    private final File fichero;
    private final ArrayList<Cliente> clientes = new ArrayList<>();

    // Constructores
    public GestorClientes(File fichero) {
        assert fichero != null : "El fichero no puede ser nulo";
        this.fichero = fichero;
    }

    // Metodos
    public void anadir(Cliente cliente) {
        clientes.add(cliente);
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void guardar() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichero))) {
            out.writeObject(clientes);
        }
    }

    @SuppressWarnings("unchecked")
    public void recuperar() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichero))) {
            ArrayList<Cliente> cargados = (ArrayList<Cliente>) in.readObject();
            clientes.clear();
            clientes.addAll(cargados);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Cliente> devolverMorosos(File fichero) throws IOException, ClassNotFoundException {
        ArrayList<Cliente> morosos = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichero))) {
            ArrayList<Cliente> cargados = (ArrayList<Cliente>) in.readObject();
            for (Cliente cliente : cargados) {
                if (cliente.esMoroso()) {
                    morosos.add(cliente);
                }
            }
        }

        return morosos;
    }
}
