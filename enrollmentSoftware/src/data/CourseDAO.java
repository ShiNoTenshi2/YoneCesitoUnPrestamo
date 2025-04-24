package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Course;

public class CourseDAO implements CRUD_Operation<Course, String> {
    private Connection connection;

    public CourseDAO(Connection connection) {
        this.connection = connection;
    }

    /* ======================= CRUD Básico ======================= */
    @Override
    public void save(Course course) {
        String sql = "INSERT INTO COURSE (CODE, NAME, CREDITS) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCode());
            stmt.setString(2, course.getName());
            stmt.setInt(3, course.getCredits());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar curso: " + e.getMessage(), e);
        }
    }

    // Para JavaFX (ObservableList)
    public ObservableList<Course> fetchObservable() {
        ObservableList<Course> cursos = FXCollections.observableArrayList();
        String sql = "SELECT CODE, NAME, CREDITS FROM COURSE";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cursos.add(new Course(
                    rs.getString("CODE"),
                    rs.getString("NAME"),
                    rs.getInt("CREDITS")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar cursos: " + e.getMessage(), e);
        }
        return cursos;
    }

    // Para uso general (ArrayList)
    @Override
    public ArrayList<Course> fetch() {
        ArrayList<Course> cursos = new ArrayList<>();
        String sql = "SELECT CODE, NAME, CREDITS FROM COURSE";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                cursos.add(new Course(
                    rs.getString("CODE"),
                    rs.getString("NAME"),
                    rs.getInt("CREDITS")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar cursos: " + e.getMessage(), e);
        }
        return cursos;
    }

    @Override
    public void update(Course course) {
        String sql = "UPDATE COURSE SET NAME = ?, CREDITS = ? WHERE CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getCredits());
            stmt.setString(3, course.getCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar curso: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String code) {
        String sql = "DELETE FROM COURSE WHERE CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar curso: " + e.getMessage(), e);
        }
    }

    /* ======================= Validaciones ======================= */
    @Override
    public boolean authenticate(String code) {
        return existsCode(code);
    }

    public boolean existsCode(String code) {
        String sql = "SELECT COUNT(*) FROM COURSE WHERE CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar código de curso: " + e.getMessage(), e);
        }
    }

    public boolean existsName(String name) {
        String sql = "SELECT COUNT(*) FROM COURSE WHERE NAME = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar nombre de curso: " + e.getMessage(), e);
        }
    }

    public boolean hasActiveEnrollments(String courseCode) {
        String sql = "SELECT COUNT(*) FROM ENROLLMENT WHERE COURSE_CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar inscripciones: " + e.getMessage(), e);
        }
    }

    /* ======================= Búsquedas ======================= */
    public Course findByCode(String code) {
        String sql = "SELECT CODE, NAME, CREDITS FROM COURSE WHERE CODE = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Course(
                    rs.getString("CODE"),
                    rs.getString("NAME"),
                    rs.getInt("CREDITS")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar curso: " + e.getMessage(), e);
        }
    }
    
    public int countEnrollments(String courseCode) {
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
