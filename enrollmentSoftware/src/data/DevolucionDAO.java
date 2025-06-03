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

    public void guardar(Devolucion devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String procedureCall = "{call registrar_devolucion(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(procedureCall)) {
            stmt.setDate(1, Date.valueOf(devolucion.getFechaDevolucion()));
            stmt.setString(2, devolucion.getEntrega());
            stmt.setString(3, devolucion.getDescripcion());
            stmt.setString(4, devolucion.getEstadoEquipo());
            stmt.setObject(5, devolucion.getIdPrestamo());
            stmt.setObject(6, devolucion.getIdMantenimiento());
            stmt.registerOutParameter(7, java.sql.Types.VARCHAR);
            stmt.execute();

            String resultado = stmt.getString(7);
            if (!"0".equals(resultado)) {
                throw new SQLException(resultado);
            }
        }
    }

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

    public void actualizar(Devolucion devolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sqlUpdate = "UPDATE devolucion SET fecha_devolucion = ?, entrega = ?, descripcion = ?, estado_equipo = ?, id_prestamo = ?, id_mantenimiento = ? WHERE id_devolucion = ?";
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            stmtUpdate.setDate(1, Date.valueOf(devolucion.getFechaDevolucion()));
            stmtUpdate.setString(2, devolucion.getEntrega());
            stmtUpdate.setString(3, devolucion.getDescripcion());
            stmtUpdate.setString(4, devolucion.getEstadoEquipo());
            stmtUpdate.setObject(5, devolucion.getIdPrestamo());
            stmtUpdate.setObject(6, devolucion.getIdMantenimiento());
            stmtUpdate.setInt(7, devolucion.getIdDevolucion());
            stmtUpdate.executeUpdate();
        }
    }

    public void eliminar(int idDevolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "DELETE FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDevolucion);
            stmt.executeUpdate();
        }
    }

    public boolean existeId(int idDevolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "SELECT COUNT(*) FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDevolucion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

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
}