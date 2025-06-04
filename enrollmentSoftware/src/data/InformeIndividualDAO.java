package data;

import model.Usuario;
import model.Prestamo;
import model.UsuarioConSancion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InformeIndividualDAO {
    private static InformeIndividualDAO instance;
    private Connection conn;

    private InformeIndividualDAO() {
        conn = CoordinadorConnection.getInstance().getConnection();
    }

    public static InformeIndividualDAO getInstance() {
        if (instance == null) {
            instance = new InformeIndividualDAO();
        }
        return instance;
    }

    public List<Long> obtenerCedulasUsuarios() throws SQLException {
        List<Long> cedulas = new ArrayList<>();
        String query = "SELECT cedula_usuario FROM usuario";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cedulas.add(rs.getLong("cedula_usuario"));
            }
        }
        return cedulas;
    }

    public Usuario obtenerUsuarioPorCedula(long cedulaUsuario) throws SQLException {
        String query = "SELECT * FROM usuario WHERE cedula_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getLong("cedula_usuario"),
                        rs.getString("nombre_completo"),
                        rs.getString("contrasena"),
                        rs.getString("rol"),
                        rs.getString("estado"),
                        rs.getString("correo")
                    );
                }
            }
        }
        return null;
    }

    public List<UsuarioConSancion> obtenerSancionesPorCedula(long cedulaUsuario) throws SQLException {
        List<UsuarioConSancion> sanciones = new ArrayList<>();
        String query = "SELECT u.cedula_usuario, u.nombre_completo, u.estado AS estado_usuario, " +
                      "s.id_sancion, s.motivo, s.monto, s.estado AS estado_sancion " +
                      "FROM usuario u JOIN sancion s ON u.cedula_usuario = s.cedula_usuario " +
                      "WHERE u.cedula_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sanciones.add(new UsuarioConSancion(
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
        }
        return sanciones;
    }

    public List<Prestamo> obtenerPrestamosPorCedula(long cedulaUsuario) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String query = "SELECT * FROM prestamo WHERE cedula_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
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
        }
        return prestamos;
    }
}