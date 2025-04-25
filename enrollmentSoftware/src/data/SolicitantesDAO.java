package data;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Solicitantes;

public class SolicitantesDAO {

    private Connection connection;

    public SolicitantesDAO(Connection connection) {
        this.connection = connection;
    }

    // ============ OPERACIONES CRUD ============
    public void guardar(Solicitantes solicitante) throws SQLException {
        String sql = "INSERT INTO solicitante (id_solicitante, nombre, correo, telefono) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, solicitante.getid_solicitante());
            stmt.setString(2, solicitante.getNombre());
            stmt.setString(3, solicitante.getCorreo());
            stmt.setString(4, solicitante.getTelefono());
            stmt.executeUpdate();
        }
    }

    public ObservableList<Solicitantes> obtenerTodos() throws SQLException {
        ObservableList<Solicitantes> solicitantes = FXCollections.observableArrayList();
        String sql = "SELECT id_solicitante, nombre, correo, telefono FROM solicitante";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                solicitantes.add(new Solicitantes(
                    rs.getInt("id_solicitante"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                ));
            }
        }
        return solicitantes;
    }

    public void actualizar(Solicitantes solicitante) throws SQLException {
        String sql = "UPDATE solicitante SET nombre = ?, correo = ?, telefono = ? WHERE id_solicitante = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, solicitante.getNombre());
            stmt.setString(2, solicitante.getCorreo());
            stmt.setString(3, solicitante.getTelefono());
            stmt.setInt(4, solicitante.getid_solicitante());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id_solicitante) throws SQLException {
        String sql = "DELETE FROM solicitante WHERE id_solicitante = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_solicitante);
            stmt.executeUpdate();
        }
    }

    // ============ VALIDACIONES ============
    public boolean existeId(int id_solicitante) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitante WHERE id_solicitante = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_solicitante);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitante WHERE correo = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // ============ BÚSQUEDAS ============
    public Solicitantes buscarPorId(int id_solicitante) throws SQLException {
        String sql = "SELECT id_solicitante, nombre, correo, telefono FROM solicitante WHERE id_solicitante = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_solicitante);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Solicitantes(
                    rs.getInt("id_solicitante"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                );
            }
            return null;
        }
    }

    public ArrayList<Solicitantes> buscarPorNombre(String nombre) throws SQLException {
        ArrayList<Solicitantes> resultados = new ArrayList<>();
        String sql = "SELECT id_solicitante, nombre, correo, telefono FROM solicitante WHERE nombre LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                resultados.add(new Solicitantes(
                    rs.getInt("id_solicitante"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                ));
            }
        }
        return resultados;
    }
}