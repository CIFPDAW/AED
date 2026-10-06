package com.mycompany.randomaccessfile.repositories;

import com.mycompany.randomaccessfile.models.Empleado;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class EmpleadoAccesoAleatorioRepositorio {

    private static final int CHARS_APELLIDO = 10;

    private static final int BYTES_REGISTRO = 4 + (CHARS_APELLIDO * 2) + 4 + 8;

    private static long posicionRegistro(int indice) {
        return (long) indice * BYTES_REGISTRO;
    }

    public static void guardar(Path fichero, Empleado empleado) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(fichero.toFile(), "rw")) {

            long posicion = posicionRegistro(empleado.getId() - 1);

            raf.seek(posicion);

            raf.writeInt(empleado.getId());

            escribirApellidoFijo(raf, empleado.getApellido());

            raf.writeInt(empleado.getDepartamento());

            raf.writeDouble(empleado.getSalario());
        }
    }

    public static Empleado buscarPorId(Path fichero, int id) throws IOException {

        if (id <= 0) {
            return null;
        }

        try (RandomAccessFile raf = new RandomAccessFile(fichero.toFile(), "r")) {

            long posicion = posicionRegistro(id - 1);

            if (posicion + BYTES_REGISTRO > raf.length()) {
                return null;
            }

            raf.seek(posicion);

            int idLeido = raf.readInt();

            String apellido = leerApellidoFijo(raf);

            int departamento = raf.readInt();

            double salario = raf.readDouble();

            if (idLeido <= 0) {
                return null;
            }

            return new Empleado(
                    idLeido,
                    apellido,
                    departamento,
                    salario
            );
        }
    }

    public static void listar(Path fichero) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(fichero.toFile(), "r")) {

            long numeroRegistros = raf.length() / BYTES_REGISTRO;

            for (int i = 0; i < numeroRegistros; i++) {

                long posicion = posicionRegistro(i);

                raf.seek(posicion);

                int id = raf.readInt();

                String apellido = leerApellidoFijo(raf);

                int departamento = raf.readInt();

                double salario = raf.readDouble();

                if (id > 0) {
                    Empleado empleado = new Empleado(
                            id,
                            apellido,
                            departamento,
                            salario
                    );

                    System.out.println(empleado);
                }
            }
        }
    }

    public static void modificar(Path fichero, Empleado empleado) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(fichero.toFile(), "rw")) {

            long posicion = posicionRegistro(empleado.getId() - 1);

            if (posicion + BYTES_REGISTRO > raf.length()) {
                return;
            }

            raf.seek(posicion);

            int idLeido = raf.readInt();

            if (idLeido <= 0) {
                return;
            }

            escribirApellidoFijo(
                    raf,
                    empleado.getApellido()
            );

            raf.writeInt(empleado.getDepartamento());

            raf.writeDouble(empleado.getSalario());
        }
    }

    private static void escribirApellidoFijo(RandomAccessFile raf, String apellido) throws IOException {

        String limpio = apellido == null ? "" : apellido;

        if (limpio.length() > CHARS_APELLIDO) {
            limpio = limpio.substring(0, CHARS_APELLIDO);
        }

        String ajustado = String.format(
                "%-" + CHARS_APELLIDO + "s",
                limpio
        );

        raf.writeChars(ajustado);
    }

    private static String leerApellidoFijo(RandomAccessFile raf) throws IOException {

        char[] chars = new char[CHARS_APELLIDO];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = raf.readChar();
        }

        return new String(chars).trim();
    }
}
