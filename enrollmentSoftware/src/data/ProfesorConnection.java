package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProfesorConnection implements DBConnection {
    private static ProfesorConnection instance; // Singleton
    private Connection connection;
    private final String username = "profesorYNC";
    private final String password = "yonecesito";
    private final String host = "192.168.254.215";
    private final String port = "1521";
    private final String service = "orcl";

    private ProfesorConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static ProfesorConnection getInstance() {
        if (instance == null) instance = new ProfesorConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getConnectionString() {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
    }
}