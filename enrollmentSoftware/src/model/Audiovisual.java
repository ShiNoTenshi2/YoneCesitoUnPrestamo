package model;

public class Audiovisual {
    private int id_audiovisual;
    private String nombre_audiov;
    private String detalle_audiovisual;
    private String estado;

    public Audiovisual(int id_audiovisual, String nombre_audiov, String detalle_audiovisual, String estado) {
        this.id_audiovisual = id_audiovisual;
        this.nombre_audiov = nombre_audiov;
        this.detalle_audiovisual = detalle_audiovisual;
        this.estado = estado;
    }

    // Getters
    public int getId_audiovisual() {
        return id_audiovisual;
    }

    public String getNombre_audiov() {
        return nombre_audiov;
    }

    public String getDetalle_audiovisual() {
        return detalle_audiovisual;
    }

    public String getEstado() {
        return estado;
    }

    // Setters
    public void setId_audiovisual(int id_audiovisual) {
        this.id_audiovisual = id_audiovisual;
    }

    public void setNombre_audiov(String nombre_audiov) {
        this.nombre_audiov = nombre_audiov;
    }

    public void setDetalle_audiovisual(String detalle_audiovisual) {
        this.detalle_audiovisual = detalle_audiovisual;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format(
            "Audiovisual [ID: %d, Nombre: %s, Detalle: %s, Estado: %s]",
            id_audiovisual, nombre_audiov, detalle_audiovisual, estado
        );
    }
}