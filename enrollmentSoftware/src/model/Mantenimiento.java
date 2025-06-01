package model;

import java.time.LocalDate;

public class Mantenimiento {
    private int id_mantenimiento;
    private LocalDate fecha;
    private String descripcion;
    private String responsable;
    private String estado;
    private Integer id_sala; // Puede ser null
    private Integer id_audiovisual; // Puede ser null

    public Mantenimiento(int id_mantenimiento, LocalDate fecha, String descripcion, String responsable, String estado, Integer id_sala, Integer id_audiovisual) {
        this.id_mantenimiento = id_mantenimiento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.estado = estado;
        this.id_sala = id_sala;
        this.id_audiovisual = id_audiovisual;
    }

    public int getId_mantenimiento() {
        return id_mantenimiento;
    }

    public void setId_mantenimiento(int id_mantenimiento) {
        this.id_mantenimiento = id_mantenimiento;
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

    public Integer getId_sala() {
        return id_sala;
    }

    public void setId_sala(Integer id_sala) {
        this.id_sala = id_sala;
    }

    public Integer getId_audiovisual() {
        return id_audiovisual;
    }

    public void setId_audiovisual(Integer id_audiovisual) {
        this.id_audiovisual = id_audiovisual;
    }

    @Override
    public String toString() {
        return "Id=" + id_mantenimiento +
               ", Fecha=" + fecha +
               ", Descripcion='" + descripcion + '\'' +
               ", Responsable='" + responsable + '\'' +
               ", Estado='" + estado + '\'' +
               ", Id Sala=" + (id_sala != null ? id_sala : "N/A") +
               ", Id Audiovisual=" + (id_audiovisual != null ? id_audiovisual : "N/A");
    }
}