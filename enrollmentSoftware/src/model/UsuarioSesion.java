package model;

public class UsuarioSesion {
    private static UsuarioSesion instance;
    private long cedulaUsuario;
    private String rol;

    private UsuarioSesion(long cedulaUsuario, String rol) {
        this.cedulaUsuario = cedulaUsuario;
        this.rol = rol;
    }

    public static UsuarioSesion getInstance(long cedulaUsuario, String rol) {
        if (instance == null) {
            instance = new UsuarioSesion(cedulaUsuario, rol);
        }
        return instance;
    }

    public static UsuarioSesion getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UsuarioSesion no ha sido inicializada.");
        }
        return instance;
    }

    public long getCedulaUsuario() { return cedulaUsuario; }
    public String getRol() { return rol; }
    public void destroy() { instance = null; }
}