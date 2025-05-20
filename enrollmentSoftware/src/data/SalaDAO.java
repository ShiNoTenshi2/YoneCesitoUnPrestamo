package data;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Sala;

public class SalaDAO {

    private Connection connection;

    public SalaDAO(Connection connection) {
        this.connection = connection;
    }

    public void guardar(Sala sala) throws SQLException {
        String sql = "INSERT INTO sala (id_sala, capacidad, detalles_sala, estado) VALUES (seq_sala.NEXTVAL, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sala.getcapacidad());
            stmt.setString(2, sala.getdetalles_sala());
            stmt.setString(3, sala.getestado());
            stmt.executeUpdate();
        }
    }

    public ObservableList<Sala> obtenerTodos() throws SQLException {
        ObservableList<Sala> salas = FXCollections.observableArrayList();
        String sql = "SELECT id_sala, capacidad, detalles_sala, estado FROM sala";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                salas.add(new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("capacidad"),
                    rs.getString("detalles_sala"),
                    rs.getString("estado")
                ));
            }
        }
        return salas;
    }

    public void actualizar(Sala sala) throws SQLException {
        String sql = "UPDATE sala SET capacidad = ?, detalles_sala = ?, estado = ? WHERE id_sala = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sala.getcapacidad());
            stmt.setString(2, sala.getdetalles_sala());
            stmt.setString(3, sala.getestado());
            stmt.setInt(4, sala.getid_sala());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id_sala) throws SQLException {
        String sql = "DELETE FROM sala WHERE id_sala = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_sala);
            stmt.executeUpdate();
        }
    }

    public boolean existeId(int id_sala) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sala WHERE id_sala = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_sala);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public Sala buscarPorId(int id_sala) throws SQLException {
        String sql = "SELECT id_sala, capacidad, detalles_sala, estado FROM sala WHERE id_sala = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_sala);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Sala(
                    rs.getInt("id_sala"),
                    rs.getInt("capacidad"),
                    rs.getString("detalles_sala"),
                    rs.getString("estado")
                );
            }
            return null;
        }
    }
}