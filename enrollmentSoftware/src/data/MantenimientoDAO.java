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

    public void guardar(Mantenimiento mantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        if (mantenimiento.getIdSala() != null) {
            actualizarEstadoSala(mantenimiento.getIdSala(), "Mantenimiento");
        }
        if (mantenimiento.getIdAudiovisual() != null) {
            actualizarEstadoAudiovisual(mantenimiento.getIdAudiovisual(), "Mantenimiento");
        }

        mantenimiento.setEstado("EnProceso");

        String sql = "INSERT INTO mantenimiento (id_mantenimiento, fecha, descripcion, responsable, estado, id_sala, id_audiovisual) " +
                    "VALUES (seq_mantenimiento.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(mantenimiento.getFecha()));
            stmt.setString(2, mantenimiento.getDescripcion());
            stmt.setString(3, mantenimiento.getResponsable());
            stmt.setString(4, mantenimiento.getEstado());
            stmt.setObject(5, mantenimiento.getIdSala());
            stmt.setObject(6, mantenimiento.getIdAudiovisual());
            stmt.executeUpdate();
        }
    }

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
                    rs.getString("estado"),
                    rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null,
                    rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null
                );
                mantenimientos.add(mantenimiento);
            }
        }
        return mantenimientos;
    }

    public void actualizar(Mantenimiento mantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        Mantenimiento viejoMantenimiento = obtenerPorId(mantenimiento.getIdMantenimiento());
        if (viejoMantenimiento != null) {
            if (viejoMantenimiento.getIdSala() != null && 
                !viejoMantenimiento.getIdSala().equals(mantenimiento.getIdSala())) {
                actualizarEstadoSala(viejoMantenimiento.getIdSala(), "Disponible");
            }
            if (viejoMantenimiento.getIdAudiovisual() != null && 
                !viejoMantenimiento.getIdAudiovisual().equals(mantenimiento.getIdAudiovisual())) {
                actualizarEstadoAudiovisual(viejoMantenimiento.getIdAudiovisual(), "Disponible");
            }
        }

        if (mantenimiento.getIdSala() != null) {
            actualizarEstadoSala(mantenimiento.getIdSala(), "Mantenimiento");
        }
        if (mantenimiento.getIdAudiovisual() != null) {
            actualizarEstadoAudiovisual(mantenimiento.getIdAudiovisual(), "Mantenimiento");
        }

        String sql = "UPDATE mantenimiento SET fecha = ?, descripcion = ?, responsable = ?, estado = ?, id_sala = ?, id_audiovisual = ? " +
                    "WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(mantenimiento.getFecha()));
            stmt.setString(2, mantenimiento.getDescripcion());
            stmt.setString(3, mantenimiento.getResponsable());
            stmt.setString(4, mantenimiento.getEstado());
            stmt.setObject(5, mantenimiento.getIdSala());
            stmt.setObject(6, mantenimiento.getIdAudiovisual());
            stmt.setInt(7, mantenimiento.getIdMantenimiento());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int idMantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        Mantenimiento mantenimiento = obtenerPorId(idMantenimiento);
        if (mantenimiento != null) {
            if (mantenimiento.getIdSala() != null) {
                actualizarEstadoSala(mantenimiento.getIdSala(), "Disponible");
            }
            if (mantenimiento.getIdAudiovisual() != null) {
                actualizarEstadoAudiovisual(mantenimiento.getIdAudiovisual(), "Disponible");
            }
        }

        String sql = "DELETE FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMantenimiento);
            stmt.executeUpdate();
        }
    }

    public boolean existeId(int idMantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "SELECT COUNT(*) FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMantenimiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private Mantenimiento obtenerPorId(int idMantenimiento) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "SELECT * FROM mantenimiento WHERE id_mantenimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMantenimiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Mantenimiento(
                        rs.getInt("id_mantenimiento"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("descripcion"),
                        rs.getString("responsable"),
                        rs.getString("estado"),
                        rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null,
                        rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null
                    );
                }
            }
        }
        return null;
    }

    public ObservableList<Integer> obtenerIdsSalasDisponibles() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_sala FROM sala WHERE estado IN ('Disponible', 'MalEstado')";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_sala"));
            }
        }
        return ids;
    }

    public ObservableList<Integer> obtenerIdsAudiovisualesDisponibles() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_audiovisual FROM audiovisual WHERE estado IN ('Disponible', 'MalEstado')";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_audiovisual"));
            }
        }
        return ids;
    }

    private void actualizarEstadoSala(int idSala, String estado) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "UPDATE sala SET estado = ? WHERE id_sala = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, idSala);
            stmt.executeUpdate();
        }
    }

    private void actualizarEstadoAudiovisual(int idAudiovisual, String estado) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "UPDATE audiovisual SET estado = ? WHERE id_audiovisual = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, idAudiovisual);
            stmt.executeUpdate();
        }
    }
}