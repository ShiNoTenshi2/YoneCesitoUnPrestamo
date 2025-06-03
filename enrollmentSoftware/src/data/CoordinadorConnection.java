package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CoordinadorConnection implements DBConnection {
    private static CoordinadorConnection instance; // Singleton
    private Connection connection;
    private final String username = "coordinadorYNC";
    private final String password = "yonecesito";
    private final String host = "192.168.254.215";
    private final String port = "1521";
    private final String service = "orcl";

    private CoordinadorConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static CoordinadorConnection getInstance() {
        if (instance == null) instance = new CoordinadorConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getConnectionString() {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
    }
}