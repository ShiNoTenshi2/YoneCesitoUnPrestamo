package model;

public class Sala {
    private long id_sala;
    private String nombre_sala;
    private int capacidad;
    private String detalles_sala;
    private String estado;

    public Sala(long id_sala, String nombre_sala, int capacidad, String detalles_sala, String estado) {
        this.id_sala = id_sala;
        this.nombre_sala = nombre_sala;
        this.capacidad = capacidad;
        this.detalles_sala = detalles_sala;
        this.estado = estado;
    }

    // Getters y Setters
    public long getId_sala() {
        return id_sala;
    }

    public void setId_sala(long id_sala) {
        this.id_sala = id_sala;
    }

    public String getNombre_sala() {
        return nombre_sala;
    }

    public void setNombre_sala(String nombre_sala) {
        this.nombre_sala = nombre_sala;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getDetalles_sala() {
        return detalles_sala;
    }

    public void setDetalles_sala(String detalles_sala) {
        this.detalles_sala = detalles_sala;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Id='" + id_sala + "', Nombre='" + nombre_sala + "', Capacidad='" + capacidad +
               "', Detalles='" + detalles_sala + "', Estado='" + estado + "'}";
    }
}