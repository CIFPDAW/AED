package dev.danoglez.ac121.models;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * AC1.21. facturas.txt tiene la forma articulo:unidades:precioUnitario.
 * En facturas.dat se escribe String, int y double, y se lee en ese mismo orden.
 */
public class GestorFacturas {

    // Variables
    private final File texto;
    private final File binario;

    // Constructores
    public GestorFacturas(String rutaTexto, String rutaBinario) {
        this.texto = new File(rutaTexto);
        this.binario = new File(rutaBinario);
    }

    // Metodos
    public void guardarBinario() throws IOException {
        List<Factura> facturas = leerTexto();

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(binario)))) {
            for (Factura factura : facturas) {
                out.writeUTF(factura.objeto());
                out.writeInt(factura.unidades());
                out.writeDouble(factura.precio());
            }
        }
    }

    public String leerBinario() throws IOException {
        StringBuilder salida = new StringBuilder();

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(binario)))) {
            while (true) {
                String objeto = in.readUTF();
                int unidades = in.readInt();
                double precio = in.readDouble();
                salida.append(String.format(
                        "Objeto:%-20s | Unidades:%2d | Precio:%.2f €%n",
                        objeto, unidades, precio));
            }
        } catch (EOFException fin) {
            salida.append("Fin de fichero.");
        }

        return salida.toString();
    }

    private List<Factura> leerTexto() throws IOException {
        List<Factura> facturas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(texto, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] campos = linea.split(":");
                facturas.add(new Factura(
                        campos[0],
                        Integer.parseInt(campos[1].trim()),
                        Double.parseDouble(campos[2].trim())));
            }
        }

        return facturas;
    }
}
