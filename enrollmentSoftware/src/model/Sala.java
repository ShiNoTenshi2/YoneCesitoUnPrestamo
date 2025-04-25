package model;

public class Sala {
    private int id_solicitante;
    private String nombre;
    private String correo;
    private String telefono;

    public Sala(int id_solicitante, String nombre, String correo, String telefono) {
        this.id_solicitante = id_solicitante;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

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
    
    @Override
    public String toString() {
        return String.format(
            "Solicitante [ID: %d, Nombre: %s, Correo: %s, Teléfono: %s]",
            id_solicitante, nombre, correo, telefono
        );
    }
}