package data;

import model.Usuario;
import model.UsuarioConSancion;
import model.Prestamo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InformeDAO {
    private static InformeDAO instance;
    private Connection conn;

    private InformeDAO() {
        // Usamos la conexión existente de CoordinadorConnection
        conn = CoordinadorConnection.getInstance().getConnection();
    }

    public static InformeDAO getInstance() {
        if (instance == null) {
            instance = new InformeDAO();
        }
        return instance;
    }

    public List<Usuario> obtenerUsuariosSinSancion() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuario u WHERE NOT EXISTS (SELECT 1 FROM sancion s WHERE s.cedula_usuario = u.cedula_usuario)";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getLong("cedula_usuario"),
                    rs.getString("nombre_completo"),
                    rs.getString("contrasena"),
                    rs.getString("rol"),
                    rs.getString("estado"),
                    rs.getString("correo")
                ));
            }
        }
        return usuarios;
    }

    public List<UsuarioConSancion> obtenerUsuariosConSancion() throws SQLException {
        List<UsuarioConSancion> usuariosConSancion = new ArrayList<>();
        String query = "SELECT u.cedula_usuario, u.nombre_completo, u.estado AS estado_usuario, " +
                      "s.id_sancion, s.motivo, s.monto, s.estado AS estado_sancion " +
                      "FROM usuario u JOIN sancion s ON u.cedula_usuario = s.cedula_usuario";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuariosConSancion.add(new UsuarioConSancion(
                    rs.getLong("cedula_usuario"),
                    rs.getString("nombre_completo"),
                    rs.getString("estado_usuario"),
                    rs.getInt("id_sancion"),
                    rs.getString("motivo"),
                    rs.getDouble("monto"),
                    rs.getString("estado_sancion")
                ));
            }
        }
        return usuariosConSancion;
    }

    public List<Prestamo> obtenerPrestamosAprobados() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String query = "SELECT * FROM prestamo WHERE estado = 'Aprobado'";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                prestamos.add(new Prestamo(
                    rs.getLong("id_prestamo"),
                    rs.getDate("fecha_solicitud") != null ? rs.getDate("fecha_solicitud").toLocalDate() : null,
                    rs.getString("detalle_prestamo"),
                    rs.getString("estado"),
                    rs.getTimestamp("hora_inicio"),
                    rs.getTimestamp("hora_fin"),
                    rs.getLong("cedula_usuario") != 0 ? rs.getLong("cedula_usuario") : null,
                    rs.getLong("id_sala") != 0 ? rs.getLong("id_sala") : null,
                    rs.getLong("id_audiovisual") != 0 ? rs.getLong("id_audiovisual") : null
                ));
            }
        }
        return prestamos;
    }

    public List<Prestamo> obtenerPrestamosFinalizados() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String query = "SELECT p.* FROM prestamo p JOIN devolucion d ON p.id_prestamo = d.id_prestamo";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                prestamos.add(new Prestamo(
                    rs.getLong("id_prestamo"),
                    rs.getDate("fecha_solicitud") != null ? rs.getDate("fecha_solicitud").toLocalDate() : null,
                    rs.getString("detalle_prestamo"),
                    rs.getString("estado"),
                    rs.getTimestamp("hora_inicio"),
                    rs.getTimestamp("hora_fin"),
                    rs.getLong("cedula_usuario") != 0 ? rs.getLong("cedula_usuario") : null,
                    rs.getLong("id_sala") != 0 ? rs.getLong("id_sala") : null,
                    rs.getLong("id_audiovisual") != 0 ? rs.getLong("id_audiovisual") : null
                ));
            }
        }
        return prestamos;
    }
}