package data;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Sancion;

public class SancionDAO {

    private Connection connection;

    public SancionDAO(Connection connection) {
        this.connection = connection;
    }

    // Crear una nueva sanción
    public void guardar(Sancion sancion) throws SQLException {
        String sql = "INSERT INTO sancion (id_sancion, id_solicitante, motivo, monto, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sancion.getid_sancion());
            stmt.setInt(2, sancion.getid_solicitante());
            stmt.setString(3, sancion.getmotivo());
            stmt.setInt(4, sancion.getmonto());
            stmt.setString(5, sancion.getestado());
            stmt.executeUpdate();
        }
    }

    // Leer una sanción por id_sancion
    public Sancion buscarPorId(int id_sancion) throws SQLException {
        String sql = "SELECT id_sancion, id_solicitante, motivo, monto, estado FROM sancion WHERE id_sancion = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_sancion);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Sancion(
                    rs.getInt("id_sancion"),
                    rs.getInt("id_solicitante"),
                    rs.getString("motivo"),
                    rs.getInt("monto"),
                    rs.getString("estado")
                );
            }
            return null;
        }
    }

    // Obtener todas las sanciones
    public ObservableList<Sancion> obtenerTodas() throws SQLException {
        ObservableList<Sancion> sanciones = FXCollections.observableArrayList();
        String sql = "SELECT id_sancion, id_solicitante, motivo, monto, estado FROM sancion";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sanciones.add(new Sancion(
                    rs.getInt("id_sancion"),
                    rs.getInt("id_solicitante"),
                    rs.getString("motivo"),
                    rs.getInt("monto"),
                    rs.getString("estado")
                ));
            }
        }
        return sanciones;
    }

    // Actualizar una sanción
    public void actualizar(Sancion sancion) throws SQLException {
        String sql = "UPDATE sancion SET id_solicitante = ?, motivo = ?, monto = ?, estado = ? WHERE id_sancion = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sancion.getid_solicitante());
            stmt.setString(2, sancion.getmotivo());
            stmt.setInt(3, sancion.getmonto());
            stmt.setString(4, sancion.getestado());
            stmt.setInt(5, sancion.getid_sancion());
            stmt.executeUpdate();
        }
    }
    //chao

    // Eliminar una sanción
    public void eliminar(int id_sancion) throws SQLException {
        String sql = "DELETE FROM sancion WHERE id_sancion = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_sancion);
            stmt.executeUpdate();
        }
    }

    // Verificar si existe una sanción por id_sancion
    public boolean existeId(int id_sancion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM sancion WHERE id_sancion = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_sancion);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
