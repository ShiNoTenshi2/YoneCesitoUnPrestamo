package model;

import java.time.LocalDate;

public class Devolucion {
    private int id_devolucion;
    private LocalDate fecha_devolucion;
    private String entrega;
    private String descripcion;
    private String estado_equipo;
    private int id_prestamo;

    public Devolucion(int id_devolucion, LocalDate fecha_devolucion, String entrega, String descripcion, String estado_equipo, int id_prestamo) {
        this.id_devolucion = id_devolucion;
        this.fecha_devolucion = fecha_devolucion;
        this.entrega = entrega;
        this.descripcion = descripcion;
        this.estado_equipo = estado_equipo;
        this.id_prestamo = id_prestamo;
    }

    // Getters
    public int getId_devolucion() {
        return id_devolucion;
    }

    public LocalDate getFecha_devolucion() {
        return fecha_devolucion;
    }

    public String getEntrega() {
        return entrega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado_equipo() {
        return estado_equipo;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    // Setters
    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    public void setFecha_devolucion(LocalDate fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado_equipo(String estado_equipo) {
        this.estado_equipo = estado_equipo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    @Override
    public String toString() {
        return String.format(
            "Devolucion [id_devolucion: %d, fecha_devolucion: %s, entrega: %s, descripcion: %s, estado_equipo: %s, id_prestamo: %d]",
            id_devolucion, fecha_devolucion, entrega, descripcion != null ? descripcion : "null", estado_equipo, id_prestamo
        );
    }
}