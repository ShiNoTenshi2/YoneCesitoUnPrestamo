package data;

import model.Prestamo;
import model.UsuarioSesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private static PrestamoDAO instance;
    private DBConnection dbConnection = DBConnectionFactory.getConnectionByRole(UsuarioSesion.getInstance().getRol());

    private PrestamoDAO() {
        // Constructor privado para Singleton
    }

    public static PrestamoDAO getInstance() {
        if (instance == null) {
            instance = new PrestamoDAO();
        }
        return instance;
    }

    public boolean registrarPrestamo(Prestamo prestamo) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "INSERT INTO prestamo (id_prestamo, fecha_solicitud, detalle_prestamo, estado, hora_inicio, hora_fin, cedula_usuario, id_sala, id_audiovisual) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, prestamo.getId_prestamo());
            stmt.setDate(2, java.sql.Date.valueOf(prestamo.getFecha_solicitud()));
            stmt.setString(3, prestamo.getDetalle_prestamo());
            stmt.setString(4, prestamo.getEstado());
            stmt.setTimestamp(5, prestamo.getHora_inicio());
            stmt.setTimestamp(6, prestamo.getHora_fin());
            stmt.setLong(7, prestamo.getCedula_usuario());
            stmt.setObject(8, prestamo.getId_sala(), java.sql.Types.NUMERIC);
            stmt.setObject(9, prestamo.getId_audiovisual(), java.sql.Types.NUMERIC);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001: violación de restricción única
                return false;
            }
            throw e;
        }
    }

    public List<Prestamo> obtenerTodos() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_prestamo, fecha_solicitud, detalle_prestamo, estado, hora_inicio, hora_fin, cedula_usuario, id_sala, id_audiovisual " +
                      "FROM prestamo";
        List<Prestamo> prestamos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                prestamos.add(new Prestamo(
                    rs.getLong("id_prestamo"),
                    rs.getDate("fecha_solicitud").toLocalDate(),
                    rs.getString("detalle_prestamo"),
                    rs.getString("estado"),
                    rs.getTimestamp("hora_inicio"),
                    rs.getTimestamp("hora_fin"),
                    rs.getLong("cedula_usuario"),
                    rs.getObject("id_sala") != null ? rs.getLong("id_sala") : null,
                    rs.getObject("id_audiovisual") != null ? rs.getLong("id_audiovisual") : null
                ));
            }
        }
        return prestamos;
    }

    public List<Prestamo> obtenerEnRevision() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_prestamo, fecha_solicitud, detalle_prestamo, estado, hora_inicio, hora_fin, cedula_usuario, id_sala, id_audiovisual " +
                      "FROM prestamo WHERE estado = 'EnRevision'";
        List<Prestamo> prestamos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                prestamos.add(new Prestamo(
                    rs.getLong("id_prestamo"),
                    rs.getDate("fecha_solicitud").toLocalDate(),
                    rs.getString("detalle_prestamo"),
                    rs.getString("estado"),
                    rs.getTimestamp("hora_inicio"),
                    rs.getTimestamp("hora_fin"),
                    rs.getLong("cedula_usuario"),
                    rs.getObject("id_sala") != null ? rs.getLong("id_sala") : null,
                    rs.getObject("id_audiovisual") != null ? rs.getLong("id_audiovisual") : null
                ));
            }
        }
        return prestamos;
    }

    public boolean actualizarPrestamo(Prestamo prestamo) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "UPDATE prestamo SET fecha_solicitud = ?, detalle_prestamo = ?, estado = ?, hora_inicio = ?, hora_fin = ?, " +
                      "cedula_usuario = ?, id_sala = ?, id_audiovisual = ? WHERE id_prestamo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(prestamo.getFecha_solicitud()));
            stmt.setString(2, prestamo.getDetalle_prestamo());
            stmt.setString(3, prestamo.getEstado());
            stmt.setTimestamp(4, prestamo.getHora_inicio());
            stmt.setTimestamp(5, prestamo.getHora_fin());
            stmt.setLong(6, prestamo.getCedula_usuario());
            stmt.setObject(7, prestamo.getId_sala(), java.sql.Types.NUMERIC);
            stmt.setObject(8, prestamo.getId_audiovisual(), java.sql.Types.NUMERIC);
            stmt.setLong(9, prestamo.getId_prestamo());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean actualizarEstado(long id_prestamo, String nuevoEstado) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nuevoEstado);
            stmt.setLong(2, id_prestamo);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean eliminarPrestamo(long id_prestamo) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "DELETE FROM prestamo WHERE id_prestamo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_prestamo);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean existeId(long id_prestamo) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT COUNT(*) FROM prestamo WHERE id_prestamo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_prestamo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    public List<Long> obtenerCedulasUsuariosDisponibles() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT cedula_usuario FROM usuario WHERE estado = 'Aprobado'";
        List<Long> cedulas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cedulas.add(rs.getLong("cedula_usuario"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cédulas: " + e.getMessage()); // Depuración
            throw e;
        }
        return cedulas;
    }

    public List<Long> obtenerIdsSalasDisponibles() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_sala FROM sala WHERE estado = 'Disponible'";
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getLong("id_sala"));
            }
        }
        return ids;
    }

    public List<Long> obtenerIdsAudiovisualesDisponibles() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_audiovisual FROM audiovisual WHERE estado = 'Disponible'";
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getLong("id_audiovisual"));
            }
        }
        return ids;
    }
}