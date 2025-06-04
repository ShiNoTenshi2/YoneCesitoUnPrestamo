package model;

public class UsuarioConSancion {
    private long cedulaUsuario;
    private String nombreCompleto;
    private String estadoUsuario;
    private int idSancion;
    private String motivoSancion;
    private double montoSancion;
    private String estadoSancion;

    public UsuarioConSancion(long cedulaUsuario, String nombreCompleto, String estadoUsuario,
                             int idSancion, String motivoSancion, double montoSancion, String estadoSancion) {
        this.cedulaUsuario = cedulaUsuario;
        this.nombreCompleto = nombreCompleto;
        this.estadoUsuario = estadoUsuario;
        this.idSancion = idSancion;
        this.motivoSancion = motivoSancion;
        this.montoSancion = montoSancion;
        this.estadoSancion = estadoSancion;
    }

    // Getters
    public long getCedulaUsuario() { return cedulaUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEstadoUsuario() { return estadoUsuario; }
    public int getIdSancion() { return idSancion; }
    public String getMotivoSancion() { return motivoSancion; }
    public double getMontoSancion() { return montoSancion; }
    public String getEstadoSancion() { return estadoSancion; }

    @Override
    public String toString() {
        return "UsuarioConSancion{" +
               "cedulaUsuario=" + cedulaUsuario +
               ", nombreCompleto='" + nombreCompleto + '\'' +
               ", estadoUsuario='" + estadoUsuario + '\'' +
               ", idSancion=" + idSancion +
               ", motivoSancion='" + motivoSancion + '\'' +
               ", montoSancion=" + montoSancion +
               ", estadoSancion='" + estadoSancion + '\'' +
               '}';
    }
}