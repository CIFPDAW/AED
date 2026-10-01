package dev.danoglez.ac127.views;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Vista gráfica que se utiliza desde el hilo de eventos de Swing. */
public class VistaGrafica implements Vista {

    @Override
    public String pedirTexto(String mensaje) {
        return JOptionPane.showInputDialog(null, mensaje);
    }

    @Override
    public void mostrarTexto(String texto) {
        JTextArea salida = new JTextArea(texto, 12, 40);
        salida.setEditable(false);
        salida.setCaretPosition(0);
        JOptionPane.showMessageDialog(null, new JScrollPane(salida),
                "Empleados", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void close() {
        // Los diálogos se cierran al responder; no hay recursos pendientes.
    }
}
