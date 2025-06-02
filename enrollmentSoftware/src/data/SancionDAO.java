package data;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Sancion;

public class SancionDAO {
    private Connection conn;

    public SancionDAO(Connection conn) {
        this.conn = conn;
    }

    // Guardar una nueva sanción usando procedimiento almacenado
    public void guardar(Sancion sancion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String procedureCall = "{call registrar_sancion(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(procedureCall)) {
            stmt.setString(1, sancion.getMotivo());
            stmt.setDouble(2, sancion.getMonto());
            stmt.setInt(3, sancion.getIdDevolucion());
            stmt.setLong(4, sancion.getCedulaUsuario());
            stmt.registerOutParameter(5, java.sql.Types.VARCHAR);
            stmt.execute();

            String resultado = stmt.getString(5);
            if (!"0".equals(resultado)) {
                throw new SQLException(resultado);
            }
        }
    }

    // Obtener todas las sanciones
    public ObservableList<Sancion> obtenerTodas() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Sancion> sanciones = FXCollections.observableArrayList();
        String sql = "SELECT * FROM sancion";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sancion sancion = new Sancion(
                    rs.getInt("id_sancion"),
                    rs.getString("motivo"),
                    rs.getDouble("monto"),
                    rs.getString("estado"),
                    rs.getInt("id_devolucion"),
                    rs.getLong("cedula_usuario")
                );
                sanciones.add(sancion);
            }
        }
        return sanciones;
    }

    // Actualizar una sanción
    public void actualizar(Sancion sancion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sqlUpdate = "UPDATE sancion SET motivo = ?, monto = ?, estado = ?, id_devolucion = ?, cedula_usuario = ? WHERE id_sancion = ?";
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
            stmtUpdate.setString(1, sancion.getMotivo());
            stmtUpdate.setDouble(2, sancion.getMonto());
            stmtUpdate.setString(3, sancion.getEstado());
            stmtUpdate.setInt(4, sancion.getIdDevolucion());
            stmtUpdate.setLong(5, sancion.getCedulaUsuario());
            stmtUpdate.setInt(6, sancion.getIdSancion());
            stmtUpdate.executeUpdate();
        }
    }

    // Eliminar una sanción
    public void eliminar(int idSancion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "DELETE FROM sancion WHERE id_sancion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            stmt.executeUpdate();
        }
    }

    // Verificar si un ID de sanción ya existe
    public boolean existeId(int idSancion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "SELECT COUNT(*) FROM sancion WHERE id_sancion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSancion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Obtener cédulas de usuarios existentes
    public ObservableList<Long> obtenerCedulasUsuarios() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Long> cedulas = FXCollections.observableArrayList();
        String sql = "SELECT cedula_usuario FROM usuario";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cedulas.add(rs.getLong("cedula_usuario"));
            }
        }
        return cedulas;
    }

    // Obtener IDs de devoluciones existentes que estén asociadas a un préstamo
    public ObservableList<Integer> obtenerIdsDevoluciones() throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        String sql = "SELECT id_devolucion FROM devolucion WHERE id_prestamo IS NOT NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("id_devolucion"));
            }
        }
        return ids;
    }

    // Obtener la cédula del usuario asociada a un id_devolucion
    public Long obtenerCedulaUsuarioPorDevolucion(int idDevolucion) throws SQLException {
        if (conn == null) {
            throw new SQLException("No hay conexión a la base de datos.");
        }

        String sql = "SELECT p.cedula_usuario " +
                     "FROM devolucion d " +
                     "JOIN prestamo p ON d.id_prestamo = p.id_prestamo " +
                     "WHERE d.id_devolucion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDevolucion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("cedula_usuario");
                }
            }
        }
        return null; // Si no se encuentra, retornamos null
    }
}