package dev.danoglez.ac126;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * AC1.26. Acceso aleatorio con registro fijo, apartado 8.
 * id (4) + apellido de 10 char con writeChars (20) + departamento (4) + salario (8) = 36 bytes.
 * Si el id empieza en 1, la posición es (id - 1) * BYTES_REGISTRO.
 */
public class EmpleadoAccesoAleatorioRepositorio {

    public static final int CHARS_APELLIDO = 10;
    public static final int BYTES_REGISTRO = 4 + (CHARS_APELLIDO * 2) + 4 + 8;

    // Variables
    private final File fichero;

    // Constructores
    public EmpleadoAccesoAleatorioRepositorio(String ruta) {
        assert ruta != null : "La ruta no puede ser nula";
        this.fichero = new File(ruta);
    }

    // Metodos
    public static long posicionRegistro(int indice) {
        return (long) indice * BYTES_REGISTRO;
    }

    public Empleado escribir(String apellido, int departamento, double salario) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            int id = (int) (raf.length() / BYTES_REGISTRO) + 1;
            raf.seek(posicionRegistro(id - 1));
            escribirRegistro(raf, id, apellido, departamento, salario);
            return new Empleado(id, ajustarApellido(apellido).trim(), departamento, salario);
        }
    }

    public ArrayList<Empleado> listar() throws IOException {
        ArrayList<Empleado> empleados = new ArrayList<>();
        if (!fichero.exists()) {
            return empleados;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            long registros = raf.length() / BYTES_REGISTRO;
            for (int indice = 0; indice < registros; indice++) {
                raf.seek(posicionRegistro(indice));
                Empleado empleado = leerRegistro(raf);
                if (empleado.getId() > 0) {
                    empleados.add(empleado);
                }
            }
        }

        return empleados;
    }

    public boolean modificar(int id, String apellido, int departamento, double salario) throws IOException {
        Empleado actual = buscarPorId(id);
        if (actual == null) {
            return false;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            raf.seek(posicionRegistro(id - 1));
            escribirRegistro(raf, id, apellido, departamento, salario);
        }
        return true;
    }

    public Empleado buscarPorId(int id) throws IOException {
        if (id <= 0 || !fichero.exists()) {
            return null;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            long pos = (id - 1L) * BYTES_REGISTRO;
            if (pos + BYTES_REGISTRO > raf.length()) {
                return null;
            }

            raf.seek(pos);
            Empleado empleado = leerRegistro(raf);
            return empleado.getId() <= 0 ? null : empleado;
        }
    }

    private void escribirRegistro(RandomAccessFile raf, int id, String apellido, int departamento, double salario)
            throws IOException {
        raf.writeInt(id);
        escribirApellidoFijo(raf, apellido);
        raf.writeInt(departamento);
        raf.writeDouble(salario);
    }

    private Empleado leerRegistro(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        String apellido = leerApellidoFijo(raf);
        int departamento = raf.readInt();
        double salario = raf.readDouble();
        return new Empleado(id, apellido, departamento, salario);
    }

    private static void escribirApellidoFijo(RandomAccessFile raf, String apellido) throws IOException {
        raf.writeChars(ajustarApellido(apellido));
    }

    private static String ajustarApellido(String apellido) {
        String limpio = apellido == null ? "" : apellido;
        if (limpio.length() > CHARS_APELLIDO) {
            limpio = limpio.substring(0, CHARS_APELLIDO);
        }
        return String.format("%-" + CHARS_APELLIDO + "s", limpio);
    }

    private static String leerApellidoFijo(RandomAccessFile raf) throws IOException {
        char[] chars = new char[CHARS_APELLIDO];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars).trim();
    }
}
