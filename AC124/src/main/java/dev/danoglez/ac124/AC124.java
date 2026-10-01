package dev.danoglez.ac124;

import java.io.File;
import java.util.ArrayList;

public class AC124 {

    public static void main(String[] args) {
        GestorPersonas gestor = new GestorPersonas(new File("personas.dat"));

        try {
            ArrayList<Persona> personas = new ArrayList<>();
            personas.add(new Persona("Ada Lovelace", 36));
            personas.add(new Persona("Luis Lopez", 23));
            gestor.guardar(personas);

            System.out.println("--- Personas recuperadas ---");
            for (Persona persona : gestor.recuperar()) {
                System.out.println(persona);
            }

            ArrayList<Persona> modificadas = gestor.recuperar();
            modificadas.add(new Persona("Grace Hopper", 85));
            gestor.guardar(modificadas);

            System.out.println("--- Tras añadir una persona ---");
            for (Persona persona : gestor.recuperar()) {
                System.out.println(persona);
            }
        } catch (Exception e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
