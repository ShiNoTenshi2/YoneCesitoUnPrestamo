package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Devolucion;

public class DevolucionDAO {
    private Connection conn;

    public DevolucionDAO(Connection conn) {
        this.conn = conn;
    }

    // Guardar una nueva devolución
    public void guardar(Devolucion devolucion) throws SQLException {
        String sql = "INSERT INTO devolucion (id_devolucion, fecha_devolucion, estado_equipo, id_prestamo) " +
                    "VALUES (seq_devolucion.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(devolucion.getFecha_devolucion()));
            stmt.setString(2, devolucion.getEstado_equipo());
            stmt.setInt(3, devolucion.getId_prestamo());
            stmt.executeUpdate();
        }
    }

    // Obtener todas las devoluciones
    public ObservableList<Devolucion> obtenerTodas() throws SQLException {
        ObservableList<Devolucion> devoluciones = FXCollections.observableArrayList();
        String sql = "SELECT * FROM devolucion";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Devolucion devolucion = new Devolucion(
                    rs.getInt("id_devolucion"),
                    rs.getDate("fecha_devolucion").toLocalDate(),
                    rs.getString("estado_equipo"),
                    rs.getObject("id_prestamo") != null ? rs.getInt("id_prestamo") : null
                );
                devoluciones.add(devolucion);
            }
        }
        return devoluciones;
    }

    // Actualizar una devolución
    public void actualizar(Devolucion devolucion) throws SQLException {
        String sql = "UPDATE devolucion SET fecha_devolucion = ?, estado_equipo = ?, id_prestamo = ? " +
                    "WHERE id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(devolucion.getFecha_devolucion()));
            stmt.setString(2, devolucion.getEstado_equipo());
            stmt.setInt(3, devolucion.getId_prestamo());
            stmt.setInt(4, devolucion.getId_devolucion());
            stmt.executeUpdate();
        }
    }

    // Eliminar una devolución
    public void eliminar(int id_devolucion) throws SQLException {
        String sql = "DELETE FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_devolucion);
            stmt.executeUpdate();
        }
    }

    // Verificar si un ID de devolución ya existe
    public boolean existeId(int id_devolucion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_devolucion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Obtener IDs de préstamos para el ComboBox
    public ObservableList<Integer> obtenerIdsPrestamos() throws SQLException {
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_prestamo FROM prestamo";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_prestamo"));
            }
        }
        return ids;
    }
}