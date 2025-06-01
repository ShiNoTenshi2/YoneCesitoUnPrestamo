package model;

import java.time.LocalDate;

public class Devolucion {
    private int id_devolucion;
    private LocalDate fecha_devolucion;
    private String entrega;
    private String descripcion;
    private String estado_equipo;
    private Integer id_prestamo; // Puede ser null
    private Integer id_mantenimiento; // Puede ser null

    public Devolucion(int id_devolucion, LocalDate fecha_devolucion, String entrega, String descripcion, 
                     String estado_equipo, Integer id_prestamo, Integer id_mantenimiento) {
        this.id_devolucion = id_devolucion;
        this.fecha_devolucion = fecha_devolucion;
        this.entrega = entrega;
        this.descripcion = descripcion;
        this.estado_equipo = estado_equipo;
        this.id_prestamo = id_prestamo;
        this.id_mantenimiento = id_mantenimiento;
    }

    // Getters y setters
    public int getId_devolucion() {
        return id_devolucion;
    }

    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    public LocalDate getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(LocalDate fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado_equipo() {
        return estado_equipo;
    }

    public void setEstado_equipo(String estado_equipo) {
        this.estado_equipo = estado_equipo;
    }

    public Integer getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(Integer id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public Integer getId_mantenimiento() {
        return id_mantenimiento;
    }

    public void setId_mantenimiento(Integer id_mantenimiento) {
        this.id_mantenimiento = id_mantenimiento;
    }

    @Override
    public String toString() {
        return "Devolucion{" +
               "id_devolucion=" + id_devolucion +
               ", fecha_devolucion=" + fecha_devolucion +
               ", entrega='" + entrega + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", estado_equipo='" + estado_equipo + '\'' +
               ", id_prestamo=" + (id_prestamo != null ? id_prestamo : "N/A") +
               ", id_mantenimiento=" + (id_mantenimiento != null ? id_mantenimiento : "N/A") +
               '}';
    }
}