package com.cifp.views;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Vista gráfica que se utiliza desde el hilo de eventos de Swing. */
public class VistaGrafica implements Vista {
    @Override
    public String pedirTexto() {
        JTextArea entrada = new JTextArea(10, 40);
        int opcion = JOptionPane.showConfirmDialog(null, new JScrollPane(entrada),
                "Escribe el texto que quieres guardar", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.OK_OPTION ? entrada.getText() : null;
    }

    @Override
    public void mostrarTexto(String texto) {
        JTextArea salida = new JTextArea(texto, 10, 40);
        salida.setEditable(false);
        salida.setCaretPosition(0);
        JOptionPane.showMessageDialog(null, new JScrollPane(salida),
                "Lectura y escritura de texto", JOptionPane.INFORMATION_MESSAGE);
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
