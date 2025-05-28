package data;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static UsuarioDAO instance;
    private ArrayList<Usuario> userList = new ArrayList<>(); // Lista en memoria para optimización
    private DBConnection dbConnection = DBConnectionFactory.getConnectionByRole("Coordinador");

    private UsuarioDAO() {
        try {
            loadUsersFromDatabase(); // Cargar usuarios al inicializar
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar UsuarioDAO: " + e.getMessage());
        }
    }

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(long cedulaUsuario, String nombreCompleto, String contrasena, String rol, String estado, String correo) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "INSERT INTO usuario (cedula_usuario, nombre_completo, contrasena, rol, estado, correo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            stmt.setString(2, nombreCompleto);
            stmt.setString(3, contrasena);
            stmt.setString(4, rol);
            stmt.setString(5, estado);
            stmt.setString(6, correo);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Actualizar lista en memoria
                userList.add(new Usuario(cedulaUsuario, nombreCompleto, contrasena, rol, estado, correo));
                return true;
            }
            return false;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001: violación de restricción única
                return false;
            }
            throw e;
        }
    }

    // Método para autenticar un usuario
    public boolean autenticarUsuario(long cedulaUsuario, String contrasena, String rol) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT COUNT(*) FROM usuario WHERE cedula_usuario = ? AND contrasena = ? AND rol = ? AND estado = 'Aprobado'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            stmt.setString(2, contrasena);
            stmt.setString(3, rol);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    // Método para obtener solicitudes (usuarios en "EnRevision")
    public List<Usuario> obtenerSolicitudes() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT cedula_usuario, nombre_completo, rol, estado, correo FROM usuario WHERE estado = 'EnRevision'";
        List<Usuario> solicitudes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                solicitudes.add(new Usuario(
                    rs.getLong("cedula_usuario"),
                    rs.getString("nombre_completo"),
                    "", // Contraseña no se recupera por seguridad
                    rs.getString("rol"),
                    rs.getString("estado"),
                    rs.getString("correo")
                ));
            }
        }
        return solicitudes;
    }

    // Método para confirmar un usuario
    public boolean confirmarUsuario(long cedulaUsuario) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "UPDATE usuario SET estado = 'Aprobado' WHERE cedula_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Actualizar lista en memoria
                for (Usuario user : userList) {
                    if (user.getCedulaUsuario() == cedulaUsuario) {
                        user.setEstado("Aprobado");
                        break;
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Método para denegar un usuario (eliminar)
    public boolean denegarUsuario(long cedulaUsuario) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "DELETE FROM usuario WHERE cedula_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, cedulaUsuario);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Actualizar lista en memoria
                userList.removeIf(user -> user.getCedulaUsuario() == cedulaUsuario);
                return true;
            }
            return false;
        }
    }

    // Método para buscar usuario por nombre (de MenuUsuariosDAO)
    public Usuario getUserByNombre(String nombreCompleto) {
        for (Usuario user : userList) {
            if (user.getNombreCompleto().equals(nombreCompleto)) {
                return user;
            }
        }
        return null;
    }

    // Método para obtener todos los usuarios (de MenuUsuariosDAO)
    public ArrayList<Usuario> getUsers() {
        return new ArrayList<>(userList);
    }

    // Método privado para cargar usuarios desde la base de datos (de MenuUsuariosDAO)
    private void loadUsersFromDatabase() throws SQLException {
        userList.clear();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT cedula_usuario, nombre_completo, contrasena, rol, estado, correo FROM usuario";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                userList.add(new Usuario(
                    rs.getLong("cedula_usuario"),
                    rs.getString("nombre_completo"),
                    rs.getString("contrasena"),
                    rs.getString("rol"),
                    rs.getString("estado"),
                    rs.getString("correo")
                ));
            }
        }
    }

    // Método para agregar un usuario manualmente y sincronizar con la BD (de MenuUsuariosDAO)
    public void addUser(Usuario user) throws SQLException {
        if (registrarUsuario(user.getCedulaUsuario(), user.getNombreCompleto(), user.getContrasena(),
                user.getRol(), user.getEstado(), user.getCorreo())) {
            userList.add(user);
        }
    }
}