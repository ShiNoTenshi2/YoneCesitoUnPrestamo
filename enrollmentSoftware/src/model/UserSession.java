package model;

public class UserSession {
    private static UserSession instance;

    private long cedula_usuario;
    private String rol;

    // Private constructor to prevent instantiation
    private UserSession(long cedula_usuario, String rol) {
        this.cedula_usuario = cedula_usuario;
        this.rol = rol;
    }

    // Static method to initialize or get the instance
    public static UserSession getInstance(long cedula_usuario, String rol) {
        if (instance == null) {
            instance = new UserSession(cedula_usuario, rol);
        }
        return instance;
    }

    // Overload for just accessing the session
    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("User session has not been initialized.");
        }
        return instance;
    }

    public long getCedula_usuario() {
        return cedula_usuario;
    }

    public String getRol() {
        return rol;
    }

    // Method to destroy session (e.g., on logout)
    public void destroy() {
        instance = null;
    }
}