package model;

public class Sala {
    private int id_sala;
    private int capacidad;
    private String detalles_sala;
    private String estado;

    public Sala(int id_sala, int capacidad, String detalles_sala, String estado) {
        this.id_sala = id_sala;
        this.capacidad = capacidad;
        this.detalles_sala = detalles_sala;
        this.estado = estado;
    }

    // Getters
    public int getid_sala() {
        return id_sala;
    }

    public int getcapacidad() {
        return capacidad;
    }

    public String getdetalles_sala() {
        return detalles_sala;
    }

    public String getestado() {
        return estado;
    }

    // Setters
    public void setid_sala(int id_sala) {
        this.id_sala = id_sala;
    }

    public void setcapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setdetalles_sala(String detalles_sala) {
        this.detalles_sala = detalles_sala;
    }

    public void setestado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Sala [id_sala: %d, capacidad: %d, detalles_sala: %s, estado: %s]",
            id_sala, capacidad, detalles_sala, estado
        );
    }
}