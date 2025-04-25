package model;

public class Sancion {
    private int id_sancion;
    private int id_solicitante;
    private String motivo;
    private int monto;
    private String estado;

    public Sancion(int id_sancion, int id_solicitante, String motivo, int monto, String estado) {
        this.id_sancion = id_sancion;
        this.id_solicitante = id_solicitante;
        this.motivo = motivo;
        this.monto = monto;
        this.estado = estado;
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
    
    @Override
    public String toString() {
        return String.format(
            "Sancion [id_sancion: %d, id_solicitante: %d, motivo: %s, monto: %d, estado: %s]",
            id_sancion, id_solicitante, motivo, monto, estado
        );
    }
}