package controller;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.Enrollment;
import application.Main;
import data.*;
import model.Student;
import model.Course;

public class EnrollmentsController {

    // Componentes UI
    @FXML private ComboBox<Student> cbEstudiantes;
    @FXML private ComboBox<Course> cbCursos;
    @FXML private DatePicker dpFecha;
    @FXML private TableView<Enrollment> tblInscripciones;
    @FXML private TableColumn<Enrollment, String> colEstudiante;
    @FXML private TableColumn<Enrollment, String> colCurso;
    @FXML private TableColumn<Enrollment, LocalDate> colFecha;

    // DAOs
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO(DBConnection.getInstance().getConnection());
    private final StudentDAO studentDAO = new StudentDAO(DBConnection.getInstance().getConnection());
    private final CourseDAO courseDAO = new CourseDAO(DBConnection.getInstance().getConnection());

    @FXML
    public void initialize() {
        configurarTabla();
        cargarComboboxes();
        cargarInscripciones();
        dpFecha.setValue(LocalDate.now());
    }

    private void configurarTabla() {
        colEstudiante.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStudentId()));
        
        colCurso.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCourseCode()));
        
        colFecha.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getEnrollmentDate()));
    }

    private void cargarComboboxes() {
        cbEstudiantes.getItems().setAll(studentDAO.fetchObservable());
        cbCursos.getItems().setAll(courseDAO.fetchObservable());
        
        cbEstudiantes.setConverter(new StringConverter<Student>() {
            @Override public String toString(Student s) { 
                return s != null ? s.getId() + " - " + s.getName() : ""; 
            }
            @Override public Student fromString(String s) { return null; }
        });
        
        cbCursos.setConverter(new StringConverter<Course>() {
            @Override public String toString(Course c) { 
                return c != null ? c.getCode() + " - " + c.getName() : ""; 
            }
            @Override public Course fromString(String s) { return null; }
        });
    }

    private void cargarInscripciones() {
        tblInscripciones.getItems().setAll(enrollmentDAO.fetchObservable());
    }

    @FXML
    private void registrarInscripcion() {
        Student estudiante = cbEstudiantes.getValue();
        Course curso = cbCursos.getValue();
        
        if (!validarCampos(estudiante, curso)) return;
        
        try {
            Enrollment nueva = new Enrollment(
                estudiante.getId(),
                curso.getCode(),
                dpFecha.getValue()
            );
            
            if (enrollmentDAO.exists(nueva.getStudentId(), nueva.getCourseCode())) {
                mostrarAlerta("Error", "Inscripción duplicada", "El estudiante ya está inscrito en este curso");
                return;
            }
            
            enrollmentDAO.save(nueva);
            mostrarAlerta("Éxito", "Inscripción registrada", "Registro exitoso");
            cargarInscripciones();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error en BD", e.getMessage());
        }
    }

    @FXML
    private void eliminarInscripcion() {
        Enrollment seleccionada = tblInscripciones.getSelectionModel().getSelectedItem();
        
        if (seleccionada == null) {
            mostrarAlerta("Error", "Selección requerida", "Seleccione una inscripción");
            return;
        }
        
        try {
            enrollmentDAO.delete(seleccionada.getStudentId(), seleccionada.getCourseCode());
            cargarInscripciones();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al eliminar", e.getMessage());
        }
    }

    @FXML
    private void volverAlMenu() {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCampos(Student estudiante, Course curso) {
        if (estudiante == null || curso == null) {
            mostrarAlerta("Error", "Campos vacíos", "Seleccione estudiante y curso");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}