package data;

import model.Audiovisual;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AudiovisualDAO {
    private static AudiovisualDAO instance;
    private DBConnection dbConnection = DBConnectionFactory.getConnectionByRole("Coordinador");

    private AudiovisualDAO() {
        // Constructor privado para Singleton
    }

    public static AudiovisualDAO getInstance() {
        if (instance == null) {
            instance = new AudiovisualDAO();
        }
        return instance;
    }

    public boolean registrarAudiovisual(Audiovisual audiovisual) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "INSERT INTO audiovisual (id_audiovisual, nombre_audiov, detalle_audiovisual, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, audiovisual.getId_audiovisual());
            stmt.setString(2, audiovisual.getNombre_audiov());
            stmt.setString(3, audiovisual.getDetalle_audiovisual());
            stmt.setString(4, audiovisual.getEstado());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001: violación de restricción única
                return false;
            }
            throw e;
        }
    }

    public List<Audiovisual> obtenerTodos() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_audiovisual, nombre_audiov, detalle_audiovisual, estado FROM audiovisual";
        List<Audiovisual> audiovisuales = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                audiovisuales.add(new Audiovisual(
                    rs.getLong("id_audiovisual"),
                    rs.getString("nombre_audiov"),
                    rs.getString("detalle_audiovisual"),
                    rs.getString("estado")
                ));
            }
        }
        return audiovisuales;
    }

    public boolean actualizarAudiovisual(Audiovisual audiovisual) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "UPDATE audiovisual SET nombre_audiov = ?, detalle_audiovisual = ?, estado = ? WHERE id_audiovisual = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, audiovisual.getNombre_audiov());
            stmt.setString(2, audiovisual.getDetalle_audiovisual());
            stmt.setString(3, audiovisual.getEstado());
            stmt.setLong(4, audiovisual.getId_audiovisual());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean eliminarAudiovisual(long id_audiovisual) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "DELETE FROM audiovisual WHERE id_audiovisual = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_audiovisual);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean existeId(long id_audiovisual) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT COUNT(*) FROM audiovisual WHERE id_audiovisual = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_audiovisual);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}