package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prestamo {
    private int id_prestamo;
    private long cedula_usuario;
    private LocalDate fecha_solicitud;
    private String detalle_prestamo;
    private String estado;
    private LocalDateTime hora_inicio;
    private LocalDateTime hora_fin;
    private Integer id_sala;
    private Integer id_audiovisual;

    public Prestamo(int id_prestamo, long cedula_usuario, LocalDate fecha_solicitud, String detalle_prestamo, 
                    String estado, LocalDateTime hora_inicio, LocalDateTime hora_fin, Integer id_sala, Integer id_audiovisual) {
        this.id_prestamo = id_prestamo;
        this.cedula_usuario = cedula_usuario;
        this.fecha_solicitud = fecha_solicitud;
        this.detalle_prestamo = detalle_prestamo;
        this.estado = estado;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.id_sala = id_sala;
        this.id_audiovisual = id_audiovisual;
    }

    // Getters
    public int getId_prestamo() {
        return id_prestamo;
    }

    public long getCedula_usuario() {
        return cedula_usuario;
    }

    public LocalDate getFecha_solicitud() {
        return fecha_solicitud;
    }

    public String getDetalle_prestamo() {
        return detalle_prestamo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getHora_inicio() {
        return hora_inicio;
    }

    public LocalDateTime getHora_fin() {
        return hora_fin;
    }

    public Integer getId_sala() {
        return id_sala;
    }

    public Integer getId_audiovisual() {
        return id_audiovisual;
    }

    // Setters
    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public void setCedula_usuario(long cedula_usuario) {
        this.cedula_usuario = cedula_usuario;
    }

    public void setFecha_solicitud(LocalDate fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public void setDetalle_prestamo(String detalle_prestamo) {
        this.detalle_prestamo = detalle_prestamo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setHora_inicio(LocalDateTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public void setHora_fin(LocalDateTime hora_fin) {
        this.hora_fin = hora_fin;
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
            "Prestamo [id_prestamo: %d, cedula_usuario: %d, fecha_solicitud: %s, detalle_prestamo: %s, estado: %s, hora_inicio: %s, hora_fin: %s, id_sala: %s, id_audiovisual: %s]",
            id_prestamo, cedula_usuario, fecha_solicitud, detalle_prestamo, estado, hora_inicio, hora_fin,
            id_sala != null ? id_sala : "null", id_audiovisual != null ? id_audiovisual : "null"
        );
    }
}