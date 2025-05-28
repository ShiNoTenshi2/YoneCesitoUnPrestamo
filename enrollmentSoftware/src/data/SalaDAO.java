package data;

import model.Sala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    private static SalaDAO instance;
    private DBConnection dbConnection = DBConnectionFactory.getConnectionByRole("Coordinador");

    private SalaDAO() {
        // Constructor privado para Singleton
    }

    public static SalaDAO getInstance() {
        if (instance == null) {
            instance = new SalaDAO();
        }
        return instance;
    }

    public boolean registrarSala(Sala sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "INSERT INTO sala (id_sala, nombre_sala, capacidad, detalles_sala, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, sala.getId_sala());
            stmt.setString(2, sala.getNombre_sala());
            stmt.setInt(3, sala.getCapacidad());
            stmt.setString(4, sala.getDetalles_sala());
            stmt.setString(5, sala.getEstado());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001: violación de restricción única
                return false;
            }
            throw e;
        }
    }

    public List<Sala> obtenerTodas() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_sala, nombre_sala, capacidad, detalles_sala, estado FROM sala";
        List<Sala> salas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                salas.add(new Sala(
                    rs.getLong("id_sala"),
                    rs.getString("nombre_sala"),
                    rs.getInt("capacidad"),
                    rs.getString("detalles_sala"),
                    rs.getString("estado")
                ));
            }
        }
        return salas;
    }

    public boolean actualizarSala(Sala sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "UPDATE sala SET nombre_sala = ?, capacidad = ?, detalles_sala = ?, estado = ? WHERE id_sala = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, sala.getNombre_sala());
            stmt.setInt(2, sala.getCapacidad());
            stmt.setString(3, sala.getDetalles_sala());
            stmt.setString(4, sala.getEstado());
            stmt.setLong(5, sala.getId_sala());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean eliminarSala(long id_sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "DELETE FROM sala WHERE id_sala = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_sala);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean existeId(long id_sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT COUNT(*) FROM sala WHERE id_sala = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_sala);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}