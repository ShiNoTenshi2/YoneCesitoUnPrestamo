package data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Enrollment;

public class EnrollmentDAO {
    private Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Enrollment enrollment) {
        String sql = "INSERT INTO ENROLLMENT (STUDENT_ID, COURSE_CODE, ENROLLMENT_DATE) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, enrollment.getStudentId());
            stmt.setString(2, enrollment.getCourseCode());
            stmt.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar inscripción: " + e.getMessage(), e);
        }
    }

    public ObservableList<Enrollment> fetchObservable() {
        ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();
        String sql = "SELECT STUDENT_ID, COURSE_CODE, ENROLLMENT_DATE FROM ENROLLMENT";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                enrollments.add(new Enrollment(
                    rs.getString("STUDENT_ID"),
                    rs.getString("COURSE_CODE"),
                    rs.getDate("ENROLLMENT_DATE").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar inscripciones: " + e.getMessage(), e);
        }
        return enrollments;
    }

    public ArrayList<Enrollment> fetch() {
        return new ArrayList<>(fetchObservable());
    }

    public void update(Enrollment enrollment) {
        String sql = "UPDATE ENROLLMENT SET ENROLLMENT_DATE = ? WHERE STUDENT_ID = ? AND COURSE_CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(enrollment.getEnrollmentDate()));
            stmt.setString(2, enrollment.getStudentId());
            stmt.setString(3, enrollment.getCourseCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar inscripción: " + e.getMessage(), e);
        }
    }

    public void delete(String studentId, String courseCode) {
        String sql = "DELETE FROM ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, courseCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar inscripción: " + e.getMessage(), e);
        }
    }

    public boolean authenticate(String studentId, String courseCode) {
        return exists(studentId, courseCode);
    }

    public boolean exists(String studentId, String courseCode) {
        String sql = "SELECT COUNT(*) FROM ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, courseCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar inscripción: " + e.getMessage(), e);
        }
    }

    public int countEnrollmentsByCourse(String courseCode) {
        String sql = "SELECT COUNT(*) FROM ENROLLMENT WHERE COURSE_CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar inscripciones: " + e.getMessage(), e);
        }
    }
}