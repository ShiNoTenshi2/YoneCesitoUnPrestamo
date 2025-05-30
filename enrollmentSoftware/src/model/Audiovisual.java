package model;

public class Audiovisual {
    private long id_audiovisual;
    private String nombre_audiov;
    private String detalle_audiovisual;
    private String estado;

    public Audiovisual(long id_audiovisual, String nombre_audiov, String detalle_audiovisual, String estado) {
        this.id_audiovisual = id_audiovisual;
        this.nombre_audiov = nombre_audiov;
        this.detalle_audiovisual = detalle_audiovisual;
        this.estado = estado;
    }

    // Getters y Setters
    public long getId_audiovisual() {
        return id_audiovisual;
    }

    public void setId_audiovisual(long id_audiovisual) {
        this.id_audiovisual = id_audiovisual;
    }

    public String getNombre_audiov() {
        return nombre_audiov;
    }

    public void setNombre_audiov(String nombre_audiov) {
        this.nombre_audiov = nombre_audiov;
    }

    public String getDetalle_audiovisual() {
        return detalle_audiovisual;
    }

    public void setDetalle_audiovisual(String detalle_audiovisual) {
        this.detalle_audiovisual = detalle_audiovisual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Id='" + id_audiovisual + "', Nombre='" + nombre_audiov + 
               "', Detalles='" + detalle_audiovisual + "', Estado='" + estado + "'}";
    }
}