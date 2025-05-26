package model;

import java.time.LocalDate;

public class Mantenimiento {
    private int id_mantenimiento;
    private LocalDate fecha;
    private String descripcion;
    private String responsable;
    private Integer id_sala;
    private Integer id_audiovisual;

    public Mantenimiento(int id_mantenimiento, LocalDate fecha, String descripcion, String responsable, Integer id_sala, Integer id_audiovisual) {
        this.id_mantenimiento = id_mantenimiento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.id_sala = id_sala;
        this.id_audiovisual = id_audiovisual;
    }

    // Getters
    public int getId_mantenimiento() {
        return id_mantenimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public Integer getId_sala() {
        return id_sala;
    }

    public Integer getId_audiovisual() {
        return id_audiovisual;
    }

    // Setters
    public void setId_mantenimiento(int id_mantenimiento) {
        this.id_mantenimiento = id_mantenimiento;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public void setId_sala(Integer id_sala) {
        this.id_sala = id_sala;
    }

    public void setId_audiovisual(Integer id_audiovisual) {
        this.id_audiovisual = id_audiovisual;
    }

    @Override
    public String toString() {
        return String.format(
            "Mantenimiento [id_mantenimiento: %d, fecha: %s, descripcion: %s, responsable: %s, id_sala: %s, id_audiovisual: %s]",
            id_mantenimiento, fecha, descripcion, responsable, 
            id_sala != null ? id_sala : "null", id_audiovisual != null ? id_audiovisual : "null"
        );
    }
}