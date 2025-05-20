package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Prestamo;

public class PrestamoDAO {
    private Connection conn;

    public PrestamoDAO(Connection conn) {
        this.conn = conn;
    }

    // Guardar un nuevo préstamo
    public void guardar(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO prestamo (id_prestamo, id_solicitante, fecha_solicitud, estado, nombre_usuario, id_audiovisual, id_sala, detalle_prestamo, hora_inicio, hora_fin) " +
                    "VALUES (seq_prestamo.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prestamo.getId_solicitante());
            stmt.setDate(2, Date.valueOf(prestamo.getFecha_solicitud()));
            stmt.setString(3, prestamo.getEstado());
            stmt.setString(4, prestamo.getNombre_usuario());
            stmt.setObject(5, prestamo.getId_audiovisual());
            stmt.setObject(6, prestamo.getId_sala());
            stmt.setString(7, prestamo.getDetalle_prestamo());
            stmt.setString(8, prestamo.getHora_inicio());
            stmt.setString(9, prestamo.getHora_fin());
            stmt.executeUpdate();
        }
    }

    // Obtener todos los préstamos
    public ObservableList<Prestamo> obtenerTodas() throws SQLException {
        ObservableList<Prestamo> prestamos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM prestamo";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                    rs.getInt("id_prestamo"),
                    rs.getInt("id_solicitante"),
                    rs.getDate("fecha_solicitud").toLocalDate(),
                    rs.getString("estado"),
                    rs.getString("nombre_usuario"),
                    rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null,
                    rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null,
                    rs.getString("detalle_prestamo"),
                    rs.getString("hora_inicio"),
                    rs.getString("hora_fin")
                );
                prestamos.add(prestamo);
            }
        }
        return prestamos;
    }

    // Actualizar un préstamo
    public void actualizar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE prestamo SET id_solicitante = ?, fecha_solicitud = ?, estado = ?, nombre_usuario = ?, id_audiovisual = ?, id_sala = ?, detalle_prestamo = ?, hora_inicio = ?, hora_fin = ? " +
                    "WHERE id_prestamo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prestamo.getId_solicitante());
            stmt.setDate(2, Date.valueOf(prestamo.getFecha_solicitud()));
            stmt.setString(3, prestamo.getEstado());
            stmt.setString(4, prestamo.getNombre_usuario());
            stmt.setObject(5, prestamo.getId_audiovisual());
            stmt.setObject(6, prestamo.getId_sala());
            stmt.setString(7, prestamo.getDetalle_prestamo());
            stmt.setString(8, prestamo.getHora_inicio());
            stmt.setString(9, prestamo.getHora_fin());
            stmt.setInt(10, prestamo.getId_prestamo());
            stmt.executeUpdate();
        }
    }

    // Eliminar un préstamo
    public void eliminar(int id_prestamo) throws SQLException {
        String sql = "DELETE FROM prestamo WHERE id_prestamo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prestamo);
            stmt.executeUpdate();
        }
    }

    // Verificar si un ID de préstamo ya existe
    public boolean existeId(int id_prestamo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamo WHERE id_prestamo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Métodos para obtener IDs de tablas relacionadas
    public ObservableList<Integer> obtenerIdsSolicitantes() throws SQLException {
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_solicitante FROM solicitante";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_solicitante"));
            }
        }
        return ids;
    }

    public ObservableList<Integer> obtenerIdsAudiovisuales() throws SQLException {
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_audiovisual FROM audiovisual";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_audiovisual"));
            }
        }
        return ids;
    }

    public ObservableList<Integer> obtenerIdsSalas() throws SQLException {
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_sala FROM sala";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_sala"));
            }
        }
        return ids;
    }
}