package model;

public class Usuario {
    private long cedulaUsuario;
    private String nombreCompleto;
    private String contrasena;
    private String rol;
    private String estado;
    private String correo;

    public Usuario(long cedulaUsuario, String nombreCompleto, String contrasena, String rol, String estado, String correo) {
        this.cedulaUsuario = cedulaUsuario;
        this.nombreCompleto = nombreCompleto;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.correo = correo;
    }

    public Usuario() {} // Constructor vacío

    // Getters y Setters
    public long getCedulaUsuario() { return cedulaUsuario; }
    public void setCedulaUsuario(long cedulaUsuario) { this.cedulaUsuario = cedulaUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override
    public String toString() {
        return "Usuario [cedulaUsuario=" + cedulaUsuario + ", nombreCompleto=" + nombreCompleto +
               ", rol=" + rol + ", estado=" + estado + ", correo=" + correo + "]";
    }
}