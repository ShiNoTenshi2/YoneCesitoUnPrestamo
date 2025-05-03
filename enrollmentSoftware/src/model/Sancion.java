package model;

public class Sancion {
    private int id_sancion;
    private int id_solicitante;
    private String motivo;
    private int monto;
    private String estado;
    private Integer id_devolucion; // Nuevo campo, Integer para permitir null

    public Sancion(int id_sancion, int id_solicitante, String motivo, int monto, String estado, Integer id_devolucion) {
        this.id_sancion = id_sancion;
        this.id_solicitante = id_solicitante;
        this.motivo = motivo;
        this.monto = monto;
        this.estado = estado;
        this.id_devolucion = id_devolucion;
    }

    // Getters
    public int getid_sancion() {
        return id_sancion;
    }

    public int getid_solicitante() {
        return id_solicitante;
    }

    public String getmotivo() {
        return motivo;
    }

    public int getmonto() {
        return monto;
    }

    public String getestado() {
        return estado;
    }

    public Integer getId_devolucion() {
        return id_devolucion;
    }

    // Setters
    public void setid_sancion(int id_sancion) {
        this.id_sancion = id_sancion;
    }

    public void setid_solicitante(int id_solicitante) {
        this.id_solicitante = id_solicitante;
    }

    public void setmotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setmonto(int monto) {
        this.monto = monto;
    }

    public void setestado(String estado) {
        this.estado = estado;
    }

    public void setId_devolucion(Integer id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    @Override
    public String toString() {
        return String.format(
            "Sancion [id_sancion: %d, id_solicitante: %d, motivo: %s, monto: %d, estado: %s, id_devolucion: %s]",
            id_sancion, id_solicitante, motivo, monto, estado, id_devolucion != null ? id_devolucion : "null"
        );
    }
}