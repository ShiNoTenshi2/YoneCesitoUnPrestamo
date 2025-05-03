package model;

import java.time.LocalDate;

public class Prestamo {
    private int id_prestamo;
    private int id_solicitante;
    private LocalDate fecha_solicitud;
    private String estado; // Mapeado a txtMontoSancion
    private String nombre_usuario; // Mapeado a comboBoxIdUsuarioPrestamo
    private Integer id_audiovisual; // Usamos Integer para permitir null
    private Integer id_sala; // Usamos Integer para permitir null
    private String detalle_prestamo; // Mapeado a txtEstadoSancion
    private String hora_inicio; // Nuevo campo
    private String hora_fin; // Nuevo campo

    public Prestamo(int id_prestamo, int id_solicitante, LocalDate fecha_solicitud, String estado, 
                    String nombre_usuario, Integer id_audiovisual, Integer id_sala, String detalle_prestamo,
                    String hora_inicio, String hora_fin) {
        this.id_prestamo = id_prestamo;
        this.id_solicitante = id_solicitante;
        this.fecha_solicitud = fecha_solicitud;
        this.estado = estado;
        this.nombre_usuario = nombre_usuario;
        this.id_audiovisual = id_audiovisual;
        this.id_sala = id_sala;
        this.detalle_prestamo = detalle_prestamo;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    // Getters
    public int getId_prestamo() {
        return id_prestamo;
    }

    public int getId_solicitante() {
        return id_solicitante;
    }

    public LocalDate getFecha_solicitud() {
        return fecha_solicitud;
    }

    public String getEstado() {
        return estado;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public Integer getId_audiovisual() {
        return id_audiovisual;
    }

    public Integer getId_sala() {
        return id_sala;
    }

    public String getDetalle_prestamo() {
        return detalle_prestamo;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "Id Prestamo=" + id_prestamo +
                ", Id Solicitante=" + id_solicitante +
                ", Fecha Solicitud=" + fecha_solicitud +
                ", Estado='" + estado + '\'' +
                ", Nombre Ssuario='" + nombre_usuario + '\'' +
                ", Id Audiovisual=" + id_audiovisual +
                ", Id Sala=" + id_sala +
                ", Detalle Prestamo='" + detalle_prestamo + '\'' +
                ", Hora Inicio='" + hora_inicio + '\'' +
                ", Hora Fin='" + hora_fin + '\'' +
                '}';
    }
}