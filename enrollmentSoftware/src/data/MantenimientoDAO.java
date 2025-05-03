package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Mantenimiento;

public class MantenimientoDAO {
    private Connection conn;

    public MantenimientoDAO(Connection conn) {
        this.conn = conn;
    }

    // Guardar un nuevo mantenimiento
    public void guardar(Mantenimiento mantenimiento) throws SQLException {
        String sql = "INSERT INTO mantenimiento (id_mantenimiento, fecha, descripcion, responsable, id_sala, id_audiovisual) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mantenimiento.getId_mantenimiento());
            stmt.setDate(2, Date.valueOf(mantenimiento.getFecha()));
            stmt.setString(3, mantenimiento.getDescripcion());
            stmt.setString(4, mantenimiento.getResponsable());
            stmt.setObject(5, mantenimiento.getId_sala()); // Maneja null
            stmt.setObject(6, mantenimiento.getId_audiovisual()); // Maneja null
            stmt.executeUpdate();
        }
    }

    // Obtener todos los mantenimientos
    public ObservableList<Mantenimiento> obtenerTodas() throws SQLException {
        ObservableList<Mantenimiento> mantenimientos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM mantenimiento";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Mantenimiento mantenimiento = new Mantenimiento(
                    rs.getInt("id_mantenimiento"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getString("descripcion"),
                    rs.getString("responsable"),
                    rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null,
                    rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null
                );
                mantenimientos.add(mantenimiento);
            }
        }
        return mantenimientos;
    }

    // Actualizar un mantenimiento
    public void actualizar(Mantenimiento mantenimiento) throws SQLException {
        String sql = "UPDATE mantenimiento SET fecha = ?, descripcion = ?, responsable = ?, id_sala = ?, id_audiovisual = ? " +
                    "WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(mantenimiento.getFecha()));
            stmt.setString(2, mantenimiento.getDescripcion());
            stmt.setString(3, mantenimiento.getResponsable());
            stmt.setObject(4, mantenimiento.getId_sala()); // Maneja null
            stmt.setObject(5, mantenimiento.getId_audiovisual()); // Maneja null
            stmt.setInt(6, mantenimiento.getId_mantenimiento());
            stmt.executeUpdate();
        }
    }

    // Eliminar un mantenimiento
    public void eliminar(int id_mantenimiento) throws SQLException {
        String sql = "DELETE FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_mantenimiento);
            stmt.executeUpdate();
        }
    }

    // Verificar si un ID de mantenimiento ya existe
    public boolean existeId(int id_mantenimiento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_mantenimiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Obtener IDs de salas para el ComboBox
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

    // Obtener IDs de audiovisuales para el ComboBox
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
}
