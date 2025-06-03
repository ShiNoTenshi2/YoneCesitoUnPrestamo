package model;

public class Sancion {
    private int idSancion;
    private String motivo;
    private double monto;
    private String estado;
    private int idDevolucion;
    private long cedulaUsuario;

    public Sancion(int idSancion, String motivo, double monto, String estado, int idDevolucion, long cedulaUsuario) {
        this.idSancion = idSancion;
        this.motivo = motivo;
        this.monto = monto;
        this.estado = estado;
        this.idDevolucion = idDevolucion;
        this.cedulaUsuario = cedulaUsuario;
    }

    public int getIdSancion() {
        return idSancion;
    }

    public void setIdSancion(int idSancion) {
        this.idSancion = idSancion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public long getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(long cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    @Override
    public String toString() {
        return "Sancion{" +
               "idSancion=" + idSancion +
               ", motivo='" + motivo + '\'' +
               ", monto=" + monto +
               ", estado='" + estado + '\'' +
               ", idDevolucion=" + idDevolucion +
               ", cedulaUsuario=" + cedulaUsuario +
               '}';
    }
}