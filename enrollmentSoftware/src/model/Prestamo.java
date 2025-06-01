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

    // Constructor para registrar (id_prestamo se genera automáticamente)
    public Prestamo(LocalDate fecha_solicitud, String detalle_prestamo, String estado,
                   Timestamp hora_inicio, Timestamp hora_fin, Long cedula_usuario,
                   Long id_sala, Long id_audiovisual) {
        this.id_prestamo = 0; // Valor placeholder, la secuencia lo generará
        this.fecha_solicitud = fecha_solicitud;
        this.detalle_prestamo = detalle_prestamo;
        this.estado = estado;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cedula_usuario = cedula_usuario;
        this.id_sala = id_sala;
        this.id_audiovisual = id_audiovisual;
    }

    // Constructor para actualizar (con id_prestamo explícito)
    public Prestamo(long id_prestamo, LocalDate fecha_solicitud, String detalle_prestamo, String estado,
                   Timestamp hora_inicio, Timestamp hora_fin, Long cedula_usuario,
                   Long id_sala, Long id_audiovisual) {
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

    // Getters
    public long getId_prestamo() { return id_prestamo; }
    public LocalDate getFecha_solicitud() { return fecha_solicitud; }
    public String getDetalle_prestamo() { return detalle_prestamo; }
    public String getEstado() { return estado; }
    public Timestamp getHora_inicio() { return hora_inicio; }
    public Timestamp getHora_fin() { return hora_fin; }
    public Long getCedula_usuario() { return cedula_usuario; }
    public Long getId_sala() { return id_sala; }
    public Long getId_audiovisual() { return id_audiovisual; }

    // Añadir toString() para mostrar los detalles del préstamo
    @Override
    public String toString() {
        return "Préstamo{" +
               "ID=" + id_prestamo +
               ", Fecha=" + fecha_solicitud +
               ", Detalle='" + detalle_prestamo + '\'' +
               ", Estado='" + estado + '\'' +
               ", Hora Inicio=" + hora_inicio +
               ", Hora Fin=" + hora_fin +
               ", Cédula Usuario=" + cedula_usuario +
               ", ID Sala=" + (id_sala != null ? id_sala : "N/A") +
               ", ID Audiovisual=" + (id_audiovisual != null ? id_audiovisual : "N/A") +
               '}';
    }
}