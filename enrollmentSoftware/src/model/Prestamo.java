package model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Prestamo {
    private long id_prestamo;
    private LocalDate fecha_solicitud;
    private String detalle_prestamo;
    private String estado;
    private Timestamp hora_inicio;
    private Timestamp hora_fin;
    private Long cedula_usuario;
    private Long id_sala;
    private Long id_audiovisual;

    public Prestamo(long id_prestamo, LocalDate fecha_solicitud, String detalle_prestamo, String estado,
                    Timestamp hora_inicio, Timestamp hora_fin, Long cedula_usuario, Long id_sala, Long id_audiovisual) {
        this.id_prestamo = id_prestamo;
        this.fecha_solicitud = fecha_solicitud;
        this.detalle_prestamo = detalle_prestamo;
        this.estado = estado;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cedula_usuario = cedula_usuario;
        this.id_sala = id_sala;
        this.id_audiovisual = id_audiovisual;
    }

    // Getters y Setters
    public long getId_prestamo() { return id_prestamo; }
    public void setId_prestamo(long id_prestamo) { this.id_prestamo = id_prestamo; }

    public LocalDate getFecha_solicitud() { return fecha_solicitud; }
    public void setFecha_solicitud(LocalDate fecha_solicitud) { this.fecha_solicitud = fecha_solicitud; }

    public String getDetalle_prestamo() { return detalle_prestamo; }
    public void setDetalle_prestamo(String detalle_prestamo) { this.detalle_prestamo = detalle_prestamo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getHora_inicio() { return hora_inicio; }
    public void setHora_inicio(Timestamp hora_inicio) { this.hora_inicio = hora_inicio; }

    public Timestamp getHora_fin() { return hora_fin; }
    public void setHora_fin(Timestamp hora_fin) { this.hora_fin = hora_fin; }

    public Long getCedula_usuario() { return cedula_usuario; }
    public void setCedula_usuario(Long cedula_usuario) { this.cedula_usuario = cedula_usuario; }

    public Long getId_sala() { return id_sala; }
    public void setId_sala(Long id_sala) { this.id_sala = id_sala; }

    public Long getId_audiovisual() { return id_audiovisual; }
    public void setId_audiovisual(Long id_audiovisual) { this.id_audiovisual = id_audiovisual; }

    @Override
    public String toString() {
        return "Prestamo{id_prestamo=" + id_prestamo + ", fecha_solicitud=" + fecha_solicitud +
               ", detalle_prestamo='" + detalle_prestamo + "', estado='" + estado +
               "', hora_inicio=" + hora_inicio + ", hora_fin=" + hora_fin +
               ", cedula_usuario=" + cedula_usuario + ", id_sala=" + id_sala +
               ", id_audiovisual=" + id_audiovisual + "}";
    }
}