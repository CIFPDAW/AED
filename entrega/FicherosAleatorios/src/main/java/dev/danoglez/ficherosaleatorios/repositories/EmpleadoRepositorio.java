package dev.danoglez.ficherosaleatorios.repositories;

import dev.danoglez.ficherosaleatorios.models.Empleado;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class EmpleadoRepositorio {

    // Constantes
    public static final int CHARS_APELLIDO = 10;
    public static final int BYTES_REGISTRO = 4 + (CHARS_APELLIDO * 2) + 4 + 8;

    // Variables
    private final File fichero;

    // Constructores
    public EmpleadoRepositorio(String ruta) {
        // Comprobamos que el parametro no sea nulo
        assert ruta != null : "La ruta del fichero no puede ser nula";

        // Inicializamos el objeto File con la ruta proporcionada
        this.fichero = new File(ruta);
    }

    // Metodos
    public static long posicionRegistro(int indice) {
        return (long) indice * BYTES_REGISTRO;
    }

    // Guardamos el empleado en la posicion de su id. Si el id es 3, se salta a (3 - 1) * 36.
    public boolean guardar(Empleado empleado) throws IOException {
        assert empleado != null : "El empleado no puede ser nulo";

        // Si ya hay un empleado vivo en ese id, no lo pisamos: eso es modificar
        if (buscarPorId(empleado.getId()) != null) {
            return false;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            // seek coloca el puntero en la posicion del registro
            raf.seek(posicionRegistro(empleado.getId() - 1));
            escribirRegistro(raf, empleado.getId(), empleado.getApellido(),
                    empleado.getDepartamento(), empleado.getSalario());
        }
        return true;
    }

    // Modificar Valores
    public boolean modificar(int id, String apellido, int departamento, double salario) throws IOException {
        // Solo se puede modificar si el registro existe y no esta dado de baja
        if (buscarPorId(id) == null) {
            return false;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            raf.seek(posicionRegistro(id - 1));
            escribirRegistro(raf, id, apellido, departamento, salario);
        }
        return true;
    }

    // Lista empleados
    public ArrayList<Empleado> listar() throws IOException {
        ArrayList<Empleado> empleados = new ArrayList<>();

        if (!fichero.exists()) {
            return empleados;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            long numeroRegistros = raf.length() / BYTES_REGISTRO;

            for (int indice = 0; indice < numeroRegistros; indice++) {
                raf.seek(posicionRegistro(indice));
                Empleado empleado = leerRegistro(raf);

                // id <= 0 es el hueco vacio del ejemplo del tema
                if (empleado.getId() > 0) {
                    empleados.add(empleado);
                }
            }
        }

        return empleados;
    }

    // Busca empleado por ID
    public Empleado buscarPorId(int id) throws IOException {
        if (id <= 0 || !fichero.exists()) {
            return null;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            long posicion = (id - 1L) * BYTES_REGISTRO;

            // Si la posicion se sale del fichero, ese id todavia no existe
            if (posicion + BYTES_REGISTRO > raf.length()) {
                return null;
            }

            raf.seek(posicion);
            Empleado empleado = leerRegistro(raf);

            // Si el id leido es 0 o negativo, el hueco esta libre
            return empleado.getId() <= 0 ? null : empleado;
        }
    }

    // Escribimos los campos en el mismo orden en el que despues se leen
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

    // writeChars guarda cada caracter en 2 bytes (UTF-16). "Gatito" ocuparia 12 bytes.
    private static void escribirApellidoFijo(RandomAccessFile raf, String apellido) throws IOException {
        raf.writeChars(ajustarApellido(apellido));
    }

    // Recortamos o rellenamos con espacios para que todos los apellidos ocupen 10 char
    private static String ajustarApellido(String apellido) {
        String limpio = apellido == null ? "" : apellido;
        if (limpio.length() > CHARS_APELLIDO) {
            limpio = limpio.substring(0, CHARS_APELLIDO);
        }
        return String.format("%-" + CHARS_APELLIDO + "s", limpio);
    }

    // Leemos siempre 10 char, que es lo que escribimos, y luego quitamos los espacios
    private static String leerApellidoFijo(RandomAccessFile raf) throws IOException {
        char[] chars = new char[CHARS_APELLIDO];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = raf.readChar();
        }
        return new String(chars).trim();
    }
}
