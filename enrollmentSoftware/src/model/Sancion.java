package model;

public class Sancion {
    private int id_sancion;
    private long cedula_usuario;
    private String motivo;
    private int monto;
    private String estado;
    private int id_devolucion;

    public Sancion(int id_sancion, long cedula_usuario, String motivo, int monto, String estado, int id_devolucion) {
        this.id_sancion = id_sancion;
        this.cedula_usuario = cedula_usuario;
        this.motivo = motivo;
        this.monto = monto;
        this.estado = estado;
        this.id_devolucion = id_devolucion;
    }

    // Getters
    public int getId_sancion() {
        return id_sancion;
    }

    public long getCedula_usuario() {
        return cedula_usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public int getId_devolucion() {
        return id_devolucion;
    }

    // Setters
    public void setId_sancion(int id_sancion) {
        this.id_sancion = id_sancion;
    }

    public void setCedula_usuario(long cedula_usuario) {
        this.cedula_usuario = cedula_usuario;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    @Override
    public String toString() {
        return String.format(
            "Sancion [id_sancion: %d, cedula_usuario: %d, motivo: %s, monto: %d, estado: %s, id_devolucion: %d]",
            id_sancion, cedula_usuario, motivo, monto, estado, id_devolucion
        );
    }
}