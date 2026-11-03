import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Nombre del archivo de la base de datos
        String url = "jdbc:sqlite:mi_base_de_datos.db";
        // Intentamos conectar
        try {
            // Cargar el driver explícitamente (por si acaso)
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC:");
            System.out.println(e.getMessage());
            return;
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Conexión a SQLite establecida correctamente!");
                Statement stmt = conn.createStatement();
                // Crear tabla
                String sqlCreate = "CREATE TABLE IF NOT EXISTS pacientes (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + " nombre TEXT NOT NULL,\n"
                        + " edad INTEGER\n"
                        + ");";
                stmt.execute(sqlCreate);
                System.out.println("Tabla 'pacientes' creada (si no existía).");
                // Insertar dos pacientes
                String sqlInsert1 = "INSERT INTO pacientes (nombre, edad) VALUES ('Ana García', 30);";
                String sqlInsert2 = "INSERT INTO pacientes (nombre, edad) VALUES ('Luis Pérez', 45);";
                stmt.execute(sqlInsert1);
                stmt.execute(sqlInsert2);
                System.out.println("Se insertaron dos pacientes.");
                // Consultar todos los pacientes
                String sqlSelect = "SELECT * FROM pacientes;";
                ResultSet rs = stmt.executeQuery(sqlSelect);
                System.out.println("Listado de pacientes:");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    int edad = rs.getInt("edad");
                    System.out.println("ID: " + id + ", Nombre: " + nombre + ", Edad: "
                            + edad);
                }
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar o ejecutar operaciones en la base de datos:");
            System.out.println(e.getMessage());
        }
    }
}