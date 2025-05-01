package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance; // Singleton
    private Connection connection;
    private final String username = "yonecesito";
    private final String password = "yonecesito";
    private final String host = "192.168.1.11";
    private final String port = "1521";
    private final String service = "xe";

    private DBConnection() {
        try {
            // Cargar el driver JDBC de Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("No se encontró el driver JDBC de Oracle.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Verificar si la conexión está cerrada o es nula
            if (connection == null || connection.isClosed()) {
                // Cargar el driver JDBC de Oracle
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(getConnectionString(), username, password);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("No se encontró el driver JDBC de Oracle.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar a la base de datos: " + e.getMessage());
        }
        return connection;
    }

    public String getConnectionString() {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
    }
}