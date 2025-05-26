package model;

public class User {
    private long cedula_usuario;
    private String nombre_completo;
    private String contrasena;
    private String rol;
    private String estado;
    private String correo;

    public User(long cedula_usuario, String nombre_completo, String contrasena, String rol, String estado, String correo) {
        this.cedula_usuario = cedula_usuario;
        this.nombre_completo = nombre_completo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.correo = correo;
    }

    // Getters
    public long getCedula_usuario() {
        return cedula_usuario;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public String getEstado() {
        return estado;
    }

    public String getCorreo() {
        return correo;
    }

    // Setters
    public void setCedula_usuario(long cedula_usuario) {
        this.cedula_usuario = cedula_usuario;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return String.format(
            "Usuario [cedula_usuario: %d, nombre_completo: %s, contrasena: %s, rol: %s, estado: %s, correo: %s]",
            cedula_usuario, nombre_completo, contrasena, rol, estado, correo
        );
    }
}