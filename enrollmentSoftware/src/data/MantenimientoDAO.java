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

    // Guardar un nuevo mantenimiento y actualizar el estado
    public void guardar(Mantenimiento mantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        // Cambiar estado a "Mantenimiento" si hay ID de sala o audiovisual
        if (mantenimiento.getId_sala() != null) {
            actualizarEstadoSala(mantenimiento.getId_sala(), "Mantenimiento");
        }
        if (mantenimiento.getId_audiovisual() != null) {
            actualizarEstadoAudiovisual(mantenimiento.getId_audiovisual(), "Mantenimiento");
        }

        String sql = "INSERT INTO mantenimiento (id_mantenimiento, fecha, descripcion, responsable, id_sala, id_audiovisual) " +
                    "VALUES (seq_mantenimiento.NEXTVAL, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(mantenimiento.getFecha()));
            stmt.setString(2, mantenimiento.getDescripcion());
            stmt.setString(3, mantenimiento.getResponsable());
            stmt.setObject(4, mantenimiento.getId_sala()); // Maneja null
            stmt.setObject(5, mantenimiento.getId_audiovisual()); // Maneja null
            stmt.executeUpdate();
        }
    }

    // Obtener todos los mantenimientos
    public ObservableList<Mantenimiento> obtenerTodas() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

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

    // Actualizar un mantenimiento y actualizar el estado
    public void actualizar(Mantenimiento mantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        // Cambiar estado a "Mantenimiento" si hay ID de sala o audiovisual
        if (mantenimiento.getId_sala() != null) {
            actualizarEstadoSala(mantenimiento.getId_sala(), "Mantenimiento");
        }
        if (mantenimiento.getId_audiovisual() != null) {
            actualizarEstadoAudiovisual(mantenimiento.getId_audiovisual(), "Mantenimiento");
        }

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
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        // Obtener el mantenimiento antes de eliminarlo para restaurar estados
        Mantenimiento mantenimiento = obtenerPorId(id_mantenimiento);
        if (mantenimiento != null) {
            if (mantenimiento.getId_sala() != null) {
                actualizarEstadoSala(mantenimiento.getId_sala(), "Disponible");
            }
            if (mantenimiento.getId_audiovisual() != null) {
                actualizarEstadoAudiovisual(mantenimiento.getId_audiovisual(), "Disponible");
            }
        }

        String sql = "DELETE FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_mantenimiento);
            stmt.executeUpdate();
        }
    }

    // Verificar si un ID de mantenimiento ya existe
    public boolean existeId(int id_mantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

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

    // Obtener un mantenimiento por ID
    private Mantenimiento obtenerPorId(int id_mantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "SELECT * FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_mantenimiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Mantenimiento(
                        rs.getInt("id_mantenimiento"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("descripcion"),
                        rs.getString("responsable"),
                        rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null,
                        rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null
                    );
                }
            }
        }
        return null;
    }

    // Obtener IDs de salas en estado "Disponible"
    public ObservableList<Integer> obtenerIdsSalasDisponibles() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_sala FROM sala WHERE estado = 'Disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_sala"));
            }
        }
        return ids;
    }

    // Obtener IDs de audiovisuales en estado "Disponible"
    public ObservableList<Integer> obtenerIdsAudiovisualesDisponibles() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_audiovisual FROM audiovisual WHERE estado = 'Disponible'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_audiovisual"));
            }
        }
        return ids;
    }

    // Actualizar estado de una sala
    private void actualizarEstadoSala(int id_sala, String estado) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "UPDATE sala SET estado = ? WHERE id_sala = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, id_sala);
            stmt.executeUpdate();
        }
    }

    // Actualizar estado de un audiovisual
    private void actualizarEstadoAudiovisual(int id_audiovisual, String estado) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "UPDATE audiovisual SET estado = ? WHERE id_audiovisual = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, id_audiovisual);
            stmt.executeUpdate();
        }
    }
}