package data;

public class DBConnectionFactory {
    public static DBConnection getConnectionByRole(String role) {
        switch (role.toLowerCase()) {
            case "coordinador":
                return CoordinadorConnection.getInstance();
            case "profesor":
                return ProfesorConnection.getInstance();
            case "estudiante":
                return EstudianteConnection.getInstance();
            default:
                throw new IllegalArgumentException("Rol no válido: " + role);
        }
    }
}