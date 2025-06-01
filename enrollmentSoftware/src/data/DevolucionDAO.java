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

    // Guardar una nueva devolución, actualizar estados y marcar préstamo/mantenimiento como Finalizado
    public void guardar(Devolucion devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        // Insertar la devolución
        String sqlInsert = "INSERT INTO devolucion (id_devolucion, fecha_devolucion, entrega, descripcion, estado_equipo, id_prestamo, id_mantenimiento) " +
                         "VALUES (seq_devolucion.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setDate(1, Date.valueOf(devolucion.getFecha_devolucion()));
            stmtInsert.setString(2, devolucion.getEntrega());
            stmtInsert.setString(3, devolucion.getDescripcion());
            stmtInsert.setString(4, devolucion.getEstado_equipo());
            stmtInsert.setObject(5, devolucion.getId_prestamo()); // Maneja null
            stmtInsert.setObject(6, devolucion.getId_mantenimiento()); // Maneja null
            stmtInsert.executeUpdate();
        }

        // Actualizar estados de sala/audiovisual según el estado_equipo
        actualizarEstados(devolucion);

        // Actualizar el estado del préstamo o mantenimiento a "Finalizado"
        actualizarEstadoPrestamoOMantenimiento(devolucion);
    }

    // Obtener todas las devoluciones
    public ObservableList<Devolucion> obtenerTodas() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Devolucion> devoluciones = FXCollections.observableArrayList();
        String sql = "SELECT * FROM devolucion";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Devolucion devolucion = new Devolucion(
                    rs.getInt("id_devolucion"),
                    rs.getDate("fecha_devolucion").toLocalDate(),
                    rs.getString("entrega"),
                    rs.getString("descripcion"),
                    rs.getString("estado_equipo"),
                    rs.getObject("id_prestamo") != null ? rs.getInt("id_prestamo") : null,
                    rs.getObject("id_mantenimiento") != null ? rs.getInt("id_mantenimiento") : null
                );
                devoluciones.add(devolucion);
            }
        }
        return devoluciones;
    }

    // Actualizar una devolución, actualizar estados y marcar préstamo/mantenimiento como Finalizado
    public void actualizar(Devolucion devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        // Actualizar estados según el estado_equipo
        actualizarEstados(devolucion);

        // Actualizar la devolución
        String sqlUpdate = "UPDATE devolucion SET fecha_devolucion = ?, entrega = ?, descripcion = ?, estado_equipo = ?, id_prestamo = ?, id_mantenimiento = ? " +
                         "WHERE id_devolucion = ?";
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            stmtUpdate.setDate(1, Date.valueOf(devolucion.getFecha_devolucion()));
            stmtUpdate.setString(2, devolucion.getEntrega());
            stmtUpdate.setString(3, devolucion.getDescripcion());
            stmtUpdate.setString(4, devolucion.getEstado_equipo());
            stmtUpdate.setObject(5, devolucion.getId_prestamo()); // Maneja null
            stmtUpdate.setObject(6, devolucion.getId_mantenimiento()); // Maneja null
            stmtUpdate.setInt(7, devolucion.getId_devolucion());
            stmtUpdate.executeUpdate();
        }

        // Actualizar el estado del préstamo o mantenimiento a "Finalizado"
        actualizarEstadoPrestamoOMantenimiento(devolucion);
    }

    // Eliminar una devolución
    public void eliminar(int id_devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "DELETE FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_devolucion);
            stmt.executeUpdate();
        }
    }

    // Verificar si un ID de devolución ya existe
    public boolean existeId(int id_devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

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

    // Obtener IDs de préstamos existentes
    public ObservableList<Integer> obtenerIdsPrestamos() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_prestamo FROM prestamo WHERE estado != 'Finalizado'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_prestamo"));
            }
        }
        return ids;
    }

    // Obtener IDs de mantenimientos existentes
    public ObservableList<Integer> obtenerIdsMantenimientos() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_mantenimiento FROM mantenimiento WHERE estado != 'Finalizado'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_mantenimiento"));
            }
        }
        return ids;
    }

    // Actualizar estados de sala/audiovisual según el estado_equipo
    private void actualizarEstados(Devolucion devolucion) throws SQLException {
        String estadoSalaAudiovisual;

        // Determinar el estado de la sala/audiovisual según el estado_equipo
        if ("BuenEstado".equals(devolucion.getEstado_equipo())) {
            estadoSalaAudiovisual = "Disponible";
        } else if ("MalEstado".equals(devolucion.getEstado_equipo())) {
            estadoSalaAudiovisual = "Mantenimiento";
        } else {
            return; // No hacer nada si estado_equipo no es válido
        }

        // Si hay id_prestamo, actualizar el estado de la sala/audiovisual del préstamo
        if (devolucion.getId_prestamo() != null) {
            // Obtener id_sala e id_audiovisual del préstamo
            String sqlPrestamo = "SELECT id_sala, id_audiovisual FROM prestamo WHERE id_prestamo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPrestamo)) {
                stmt.setInt(1, devolucion.getId_prestamo());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Integer idSala = rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null;
                        Integer idAudiovisual = rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null;

                        // Actualizar estado de la sala si existe
                        if (idSala != null) {
                            actualizarEstadoSala(idSala, estadoSalaAudiovisual);
                        }
                        // Actualizar estado del audiovisual si existe
                        if (idAudiovisual != null) {
                            actualizarEstadoAudiovisual(idAudiovisual, estadoSalaAudiovisual);
                        }
                    }
                }
            }
        }

        // Si hay id_mantenimiento, actualizar el estado de la sala/audiovisual del mantenimiento
        if (devolucion.getId_mantenimiento() != null) {
            // Obtener id_sala e id_audiovisual del mantenimiento
            String sqlMantenimiento = "SELECT id_sala, id_audiovisual FROM mantenimiento WHERE id_mantenimiento = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlMantenimiento)) {
                stmt.setInt(1, devolucion.getId_mantenimiento());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Integer idSala = rs.getObject("id_sala") != null ? rs.getInt("id_sala") : null;
                        Integer idAudiovisual = rs.getObject("id_audiovisual") != null ? rs.getInt("id_audiovisual") : null;

                        // Actualizar estado de la sala si existe (solo para "BuenEstado")
                        if (idSala != null && "BuenEstado".equals(devolucion.getEstado_equipo())) {
                            actualizarEstadoSala(idSala, "Disponible");
                        }
                        // Actualizar estado del audiovisual si existe (solo para "BuenEstado")
                        if (idAudiovisual != null && "BuenEstado".equals(devolucion.getEstado_equipo())) {
                            actualizarEstadoAudiovisual(idAudiovisual, "Disponible");
                        }
                    }
                }
            }
        }
    }

    // Actualizar el estado del préstamo o mantenimiento a "Finalizado"
    private void actualizarEstadoPrestamoOMantenimiento(Devolucion devolucion) throws SQLException {
        // Actualizar el estado del préstamo si existe
        if (devolucion.getId_prestamo() != null) {
            String sqlUpdatePrestamo = "UPDATE prestamo SET estado = 'Finalizado' WHERE id_prestamo = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdatePrestamo)) {
                stmtUpdate.setInt(1, devolucion.getId_prestamo());
                stmtUpdate.executeUpdate();
            }
        }

        // Actualizar el estado del mantenimiento si existe
        if (devolucion.getId_mantenimiento() != null) {
            String sqlUpdateMantenimiento = "UPDATE mantenimiento SET estado = 'Finalizado' WHERE id_mantenimiento = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateMantenimiento)) {
                stmtUpdate.setInt(1, devolucion.getId_mantenimiento());
                stmtUpdate.executeUpdate();
            }
        }
    }

    // Actualizar estado de una sala
    private void actualizarEstadoSala(int id_sala, String estado) throws SQLException {
        String sql = "UPDATE sala SET estado = ? WHERE id_sala = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, id_sala);
            stmt.executeUpdate();
        }
    }

    // Actualizar estado de un audiovisual
    private void actualizarEstadoAudiovisual(int id_audiovisual, String estado) throws SQLException {
        String sql = "UPDATE audiovisual SET estado = ? WHERE id_audiovisual = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, id_audiovisual);
            stmt.executeUpdate();
        }
    }
}