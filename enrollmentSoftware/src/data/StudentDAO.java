package data;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Student;

public class StudentDAO implements CRUD_Operation<Student, String> {

    private Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    /* ======================= CRUD Básico ======================= */
    @Override
    public void save(Student student) {
        String sql = "INSERT INTO STUDENT (ID, NAME, EMAIL) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar estudiante: " + e.getMessage(), e);
        }
    }

    // Para JavaFX (ObservableList)
    public ObservableList<Student> fetchObservable() {
        ObservableList<Student> estudiantes = FXCollections.observableArrayList();
        String sql = "SELECT ID, NAME, EMAIL FROM STUDENT";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                estudiantes.add(new Student(
                    rs.getString("ID"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar estudiantes: " + e.getMessage(), e);
        }
        return estudiantes;
    }

    // Para uso general (ArrayList)
    @Override
    public ArrayList<Student> fetch() {
        ArrayList<Student> estudiantes = new ArrayList<>();
        String sql = "SELECT ID, NAME, EMAIL FROM STUDENT";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                estudiantes.add(new Student(
                    rs.getString("ID"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar estudiantes: " + e.getMessage(), e);
        }
        return estudiantes;
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE STUDENT SET NAME = ?, EMAIL = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estudiante: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM STUDENT WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar estudiante: " + e.getMessage(), e);
        }
    }

    /* ======================= Validaciones ======================= */
    @Override
    public boolean authenticate(String id) {
        return existsId(id);
    }

    public boolean existsId(String id) {
        String sql = "SELECT COUNT(*) FROM STUDENT WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar ID: " + e.getMessage(), e);
        }
    }

    public boolean existsEmail(String email) {
        String sql = "SELECT COUNT(*) FROM STUDENT WHERE EMAIL = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar email: " + e.getMessage(), e);
        }
    }

    public boolean hasActiveRegistrations(String studentId) {
        // Versión simplificada - solo verifica si existe alguna matrícula
        String sql = "SELECT COUNT(*) FROM ENROLLMENT WHERE STUDENT_ID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar matrículas: " + e.getMessage(), e);
        }
    }

    /* ======================= Búsquedas ======================= */
    public Student findById(String id) {
        String sql = "SELECT ID, NAME, EMAIL FROM STUDENT WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Student(
                    rs.getString("ID"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar estudiante: " + e.getMessage(), e);
        }
    }
}