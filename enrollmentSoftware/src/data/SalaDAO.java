package data;

import model.Sala;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    private static SalaDAO instance;
    private DBConnection dbConnection = DBConnectionFactory.getConnectionByRole("Coordinador");

    private SalaDAO() {
        // Constructor privado para Singleton
    }

    public static SalaDAO getInstance() {
        if (instance == null) {
            instance = new SalaDAO();
        }
        return instance;
    }

    public boolean registrarSala(Sala sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String procedureCall = "{call registrar_sala(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Configurar los parámetros de entrada
            stmt.setString(1, sala.getNombre_sala());
            stmt.setInt(2, sala.getCapacidad());
            stmt.setString(3, sala.getDetalles_sala());
            stmt.setString(4, sala.getEstado());
            // Registrar el parámetro de salida
            stmt.registerOutParameter(5, java.sql.Types.VARCHAR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el resultado
            String resultado = stmt.getString(5);
            if ("0".equals(resultado)) {
                return true; // Operación exitosa
            } else {
                // Si hay un error (1 o 2), lanzar excepción con el mensaje
                throw new SQLException("Error al registrar la sala: " + resultado.substring(2));
            }
        }
    }

    public List<Sala> obtenerTodas() throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT id_sala, nombre_sala, capacidad, detalles_sala, estado FROM sala";
        List<Sala> salas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                salas.add(new Sala(
                    rs.getLong("id_sala"),
                    rs.getString("nombre_sala"),
                    rs.getInt("capacidad"),
                    rs.getString("detalles_sala"),
                    rs.getString("estado")
                ));
            }
        }
        return salas;
    }

    public boolean actualizarSala(Sala sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String procedureCall = "{call actualizar_sala(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Configurar los parámetros de entrada
            stmt.setLong(1, sala.getId_sala());
            stmt.setString(2, sala.getNombre_sala());
            stmt.setInt(3, sala.getCapacidad());
            stmt.setString(4, sala.getDetalles_sala());
            stmt.setString(5, sala.getEstado());
            // Registrar el parámetro de salida
            stmt.registerOutParameter(6, java.sql.Types.VARCHAR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el resultado
            String resultado = stmt.getString(6);
            if ("0".equals(resultado)) {
                return true; // Operación exitosa
            } else {
                throw new SQLException("Error al actualizar la sala: " + resultado.substring(2));
            }
        }
    }

    public boolean eliminarSala(long id_sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String procedureCall = "{call eliminar_sala(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(procedureCall)) {
            // Configurar los parámetros de entrada
            stmt.setLong(1, id_sala);
            // Registrar el parámetro de salida
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);

            // Ejecutar el procedimiento
            stmt.execute();

            // Obtener el resultado
            String resultado = stmt.getString(2);
            if ("0".equals(resultado)) {
                return true; // Operación exitosa
            } else {
                throw new SQLException("Error al eliminar la sala: " + resultado.substring(2));
            }
        }
    }

    public boolean existeId(long id_sala) throws SQLException {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT COUNT(*) FROM sala WHERE id_sala = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id_sala);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}