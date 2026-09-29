package com.mycompany.ac12;

public class AC12 {

    public static void main(String[] args) {
        // Fichero.AC12Ficheros();
        
        ObjetoFichero fichero = new ObjetoFichero("/home/2damb@informatica.edu/Documentos/Prueba AC12");
        System.out.println(fichero.obtenerInformacion());
    }
}
