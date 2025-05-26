package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.User;

public class MenuRegistroDAO {
    private static MenuRegistroDAO instance;
    private ArrayList<User> userList = new ArrayList<>();
    private DBConnection dbConnection = DBConnection.getInstance();

    private MenuRegistroDAO() {
        // Constructor privado para singleton
    }

    public static MenuRegistroDAO getInstance() {
        if (instance == null) {
            instance = new MenuRegistroDAO();
            instance.loadUsersFromDatabase();
        }
        return instance;
    }

    public User getUserByNombre(String nombre) {
        for (User user : userList) {
            if (user.getNombre().equals(nombre)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        try {
            insertUser(user);
            userList.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar usuario en la base de datos: " + e.getMessage());
        }
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(userList);
    }

    private void loadUsersFromDatabase() {
        try {
            userList.clear();
            userList.addAll(fetchUsers());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar usuarios desde la base de datos: " + e.getMessage());
        }
    }

    private ArrayList<User> fetchUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT nombre, contrasena FROM usuario"; // Cambiado de USERS a usuario
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String contrasena = rs.getString("contrasena");
                users.add(new User(nombre, contrasena));
            }
        }
        return users;
    }

    private void insertUser(User user) throws SQLException {
        String query = "INSERT INTO usuario (nombre, contrasena) VALUES (?, ?)"; // Cambiado de USERS a usuario
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getContrasena());
            stmt.executeUpdate();
        }
    }
}