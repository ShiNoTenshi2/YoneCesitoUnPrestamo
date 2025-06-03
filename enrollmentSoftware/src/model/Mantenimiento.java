package model;

import java.time.LocalDate;

public class Mantenimiento {
    private int idMantenimiento;
    private LocalDate fecha;
    private String descripcion;
    private String responsable;
    private String estado;
    private Integer idSala;
    private Integer idAudiovisual;

    public Mantenimiento(int idMantenimiento, LocalDate fecha, String descripcion, String responsable, 
                        String estado, Integer idSala, Integer idAudiovisual) {
        this.idMantenimiento = idMantenimiento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.estado = estado;
        this.idSala = idSala;
        this.idAudiovisual = idAudiovisual;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;
    }

    public Integer getIdAudiovisual() {
        return idAudiovisual;
    }

    public void setIdAudiovisual(Integer idAudiovisual) {
        this.idAudiovisual = idAudiovisual;
    }

    @Override
    public String toString() {
        return "Mantenimiento{" +
               "idMantenimiento=" + idMantenimiento +
               ", fecha=" + fecha +
               ", descripcion='" + descripcion + '\'' +
               ", responsable='" + responsable + '\'' +
               ", estado='" + estado + '\'' +
               ", idSala=" + (idSala != null ? idSala : "N/A") +
               ", idAudiovisual=" + (idAudiovisual != null ? idAudiovisual : "N/A") +
               '}';
    }
}