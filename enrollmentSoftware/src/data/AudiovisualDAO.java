package data;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Audiovisual;

public class AudiovisualDAO {

    private Connection connection;

    public AudiovisualDAO(Connection connection) {
        this.connection = connection;
    }

    public void guardar(Audiovisual audiovisual) throws SQLException {
        String sql = "INSERT INTO audiovisual (id_audiovisual, nombre, detalle_audiovisual, estado) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, audiovisual.getId_audiovisual());
            stmt.setString(2, audiovisual.getNombre());
            stmt.setString(3, audiovisual.getDetalle_audiovisual());
            stmt.setString(4, audiovisual.getEstado());
            stmt.executeUpdate();
        }
    }

    public ObservableList<Audiovisual> obtenerTodos() throws SQLException {
        ObservableList<Audiovisual> audiovisuales = FXCollections.observableArrayList();
        String sql = "SELECT id_audiovisual, nombre, detalle_audiovisual, estado FROM audiovisual";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                audiovisuales.add(new Audiovisual(
                    rs.getInt("id_audiovisual"),
                    rs.getString("nombre"),
                    rs.getString("detalle_audiovisual"),
                    rs.getString("estado")
                ));
            }
        }
        return audiovisuales;
    }

    public void actualizar(Audiovisual audiovisual) throws SQLException {
        String sql = "UPDATE audiovisual SET nombre = ?, detalle_audiovisual = ?, estado = ? WHERE id_audiovisual = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, audiovisual.getNombre());
            stmt.setString(2, audiovisual.getDetalle_audiovisual());
            stmt.setString(3, audiovisual.getEstado());
            stmt.setInt(4, audiovisual.getId_audiovisual());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id_audiovisual) throws SQLException {
        String sql = "DELETE FROM audiovisual WHERE id_audiovisual = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_audiovisual);
            stmt.executeUpdate();
        }
    }

    public boolean existeId(int id_audiovisual) throws SQLException {
        String sql = "SELECT COUNT(*) FROM audiovisual WHERE id_audiovisual = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_audiovisual);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public Audiovisual buscarPorId(int id_audiovisual) throws SQLException {
        String sql = "SELECT id_audiovisual, nombre, detalle_audiovisual, estado FROM audiovisual WHERE id_audiovisual = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_audiovisual);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Audiovisual(
                    rs.getInt("id_audiovisual"),
                    rs.getString("nombre"),
                    rs.getString("detalle_audiovisual"),
                    rs.getString("estado")
                );
            }
            return null;
        }
    }
}