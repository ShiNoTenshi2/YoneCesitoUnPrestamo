package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Course;
import application.Main;
import data.CourseDAO;
import data.DBConnection;
import data.EnrollmentDAO;

public class LibraryCoursesController {

    @FXML private TableView<Course> tableview;
    @FXML private TableColumn<Course, String> colCodigo;
    @FXML private TableColumn<Course, String> colNombre;
    @FXML private TableColumn<Course, Integer> colCreditos;
    @FXML private Button btnBack;
    @FXML private Button btnDelete;
    @FXML private Button btnMenu;

    private CourseDAO courseDAO = new CourseDAO(DBConnection.getInstance().getConnection());
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO(DBConnection.getInstance().getConnection());

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarCursos();
    }

    private void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCreditos.setCellValueFactory(new PropertyValueFactory<>("credits"));
    }

    private void cargarCursos() {
        ObservableList<Course> cursos = courseDAO.fetchObservable();
        tableview.setItems(cursos);
    }

    @FXML
    void deleteCourse(ActionEvent event) {
        Course cursoSeleccionado = tableview.getSelectionModel().getSelectedItem();

        if (cursoSeleccionado == null) {
            mostrarAlerta("Error", "Selección requerida", "Por favor seleccione un curso de la tabla.");
            return;
        }

        try {
            // Validar si hay inscripciones activas
            if (tieneInscripcionesActivas(cursoSeleccionado.getCode())) {
                mostrarAlerta("Error", "No se puede eliminar", 
                    "El curso tiene estudiantes inscritos. Elimine las inscripciones primero.");
                return;
            }

            // Eliminar el curso
            courseDAO.delete(cursoSeleccionado.getCode());
            mostrarAlerta("Éxito", "Eliminación exitosa", "Curso eliminado correctamente.");
            cargarCursos(); // Actualizar la tabla
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error en la base de datos", e.getMessage());
        }
    }

    private boolean tieneInscripcionesActivas(String codigoCurso) {
        return enrollmentDAO.countEnrollmentsByCourse(codigoCurso) > 0;
    }

    @FXML
    void goToBack(ActionEvent event) {
        Main.loadScene("/view/Courses.fxml");
    }

    @FXML
    void goToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
