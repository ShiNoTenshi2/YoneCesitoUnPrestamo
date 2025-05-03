package application;

public class SessionManager {
    private static String usuarioActual;

    public static void setUsuarioActual(String usuario) {
        usuarioActual = usuario;
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }
}

