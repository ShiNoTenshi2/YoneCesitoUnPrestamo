package model;

public class Solicitantes {
    private int id_solicitante;
    private String nombre;
    private String correo;
    private String telefono;  // Cambiado a String

    public Solicitantes(int id_solicitante, String nombre, String correo, String telefono) {
        this.id_solicitante = id_solicitante;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    // ============ GETTERS ============
    public int getid_solicitante() {
        return id_solicitante;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    // ============ SETTERS ============
    public void setid_solicitante(int id_solicitante) {
        this.id_solicitante = id_solicitante;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    // Método toString() para representación como cadena
    @Override
    public String toString() {
        return "Solicitante{" +
                "id_solicitante=" + id_solicitante +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono=" + telefono +
                '}';
    }
}