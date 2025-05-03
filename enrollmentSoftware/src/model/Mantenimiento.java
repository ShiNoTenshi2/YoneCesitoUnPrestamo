package model;

import java.time.LocalDate;

public class Mantenimiento {
    private int id_mantenimiento;
    private LocalDate fecha;
    private String descripcion;
    private String responsable;
    private Integer id_sala; // Usamos Integer para permitir null
    private Integer id_audiovisual; // Usamos Integer para permitir null

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

    @Override
    public String toString() {
        return "Mantenimiento{" +
                "id_mantenimiento=" + id_mantenimiento +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", responsable='" + responsable + '\'' +
                ", id_sala=" + id_sala +
                ", id_audiovisual=" + id_audiovisual +
                '}';
    }
}
