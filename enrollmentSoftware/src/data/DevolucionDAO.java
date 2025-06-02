package data;

import java.sql.Connection;
import java.sql.CallableStatement;
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

    // Guardar una nueva devolución usando procedimiento almacenado
    public void guardar(Devolucion devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String procedureCall = "{call registrar_devolucion(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(procedureCall)) {
            stmt.setDate(1, Date.valueOf(devolucion.getFecha_devolucion()));
            stmt.setString(2, devolucion.getEntrega());
            stmt.setString(3, devolucion.getDescripcion());
            stmt.setString(4, devolucion.getEstado_equipo());
            stmt.setObject(5, devolucion.getId_prestamo());
            stmt.setObject(6, devolucion.getId_mantenimiento());
            stmt.registerOutParameter(7, java.sql.Types.VARCHAR);
            stmt.execute();

            String resultado = stmt.getString(7);
            if (!"0".equals(resultado)) {
                throw new SQLException(resultado);
            }
        }
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

    // Actualizar una devolución y marcar préstamo/mantenimiento como Finalizado
    public void actualizar(Devolucion devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

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
}