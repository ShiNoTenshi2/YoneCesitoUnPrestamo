package model;

import java.time.LocalDate;

public class Devolucion {
    private int id_devolucion;
    private LocalDate fecha_devolucion;
    private String estado_equipo;
    private Integer id_prestamo; // Usamos Integer para permitir null

    public Devolucion(int id_devolucion, LocalDate fecha_devolucion, String estado_equipo, Integer id_prestamo) {
        this.id_devolucion = id_devolucion;
        this.fecha_devolucion = fecha_devolucion;
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

    public String getEstado_equipo() {
        return estado_equipo;
    }

    public Integer getId_prestamo() {
        return id_prestamo;
    }

    @Override
    public String toString() {
        return "Devolucion{" +
                "id_devolucion=" + id_devolucion +
                ", fecha_devolucion=" + fecha_devolucion +
                ", estado_equipo='" + estado_equipo + '\'' +
                ", id_prestamo=" + id_prestamo +
                '}';
    }
}
