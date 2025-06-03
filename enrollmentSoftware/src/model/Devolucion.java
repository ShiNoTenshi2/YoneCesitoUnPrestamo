package model;

import java.time.LocalDate;

public class Devolucion {
    private int idDevolucion;
    private LocalDate fechaDevolucion;
    private String entrega;
    private String descripcion;
    private String estadoEquipo;
    private Integer idPrestamo; // Puede ser null
    private Integer idMantenimiento; // Puede ser null

    public Devolucion(int idDevolucion, LocalDate fechaDevolucion, String entrega, String descripcion, 
                     String estadoEquipo, Integer idPrestamo, Integer idMantenimiento) {
        this.idDevolucion = idDevolucion;
        this.fechaDevolucion = fechaDevolucion;
        this.entrega = entrega;
        this.descripcion = descripcion;
        this.estadoEquipo = estadoEquipo;
        this.idPrestamo = idPrestamo;
        this.idMantenimiento = idMantenimiento;
    }

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
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

    public String getEstadoEquipo() {
        return estadoEquipo;
    }

    public void setEstadoEquipo(String estadoEquipo) {
        this.estadoEquipo = estadoEquipo;
    }

    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Integer getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Integer idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    @Override
    public String toString() {
        return "Devolucion{" +
               "idDevolucion=" + idDevolucion +
               ", fechaDevolucion=" + fechaDevolucion +
               ", entrega='" + entrega + '\'' +
               ", descripcion='" + descripcion + '\'' +
               ", estadoEquipo='" + estadoEquipo + '\'' +
               ", idPrestamo=" + (idPrestamo != null ? idPrestamo : "N/A") +
               ", idMantenimiento=" + (idMantenimiento != null ? idMantenimiento : "N/A") +
               '}';
    }
}