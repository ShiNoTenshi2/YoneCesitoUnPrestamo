package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import application.Main;
import data.DBConnection;
import data.StudentDAO;

public class LibraryStudentsController {

    // ============ COMPONENTES FXML ============
    @FXML private TableView<Student> tableview;
    @FXML private TableColumn<Student, String> colIdEstudiante;
    @FXML private TableColumn<Student, String> colNombreEstudiante;
    @FXML private TableColumn<Student, String> colEmailEstudiante;
    @FXML private Button btnBack;
    @FXML private Button btnDelete;
    @FXML private Button btnMenu;

    private StudentDAO studentDAO = new StudentDAO(DBConnection.getInstance().getConnection());

    // ============ INICIALIZACIÓN ============
    @FXML
    public void initialize() {
        configurarColumnas();
        cargarEstudiantes();
    }

    private void configurarColumnas() {
        colIdEstudiante.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmailEstudiante.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void cargarEstudiantes() {
        ObservableList<Student> estudiantes = studentDAO.fetchObservable(); // Usamos el método correcto
        tableview.setItems(estudiantes);
    }

    // ============ MANEJADORES DE EVENTOS ============
    @FXML
    void DeleteStudent(ActionEvent event) {
        Student estudianteSeleccionado = tableview.getSelectionModel().getSelectedItem();

        if (estudianteSeleccionado == null) {
            mostrarAlerta("Error", "Selección requerida", "Por favor selecciona un estudiante de la tabla.");
            return;
        }

        try {
            if (studentDAO.hasActiveRegistrations(estudianteSeleccionado.getId())) {
                mostrarAlerta("Error", "No se puede eliminar", "El estudiante tiene matrículas activas.");
                return;
            }

            studentDAO.delete(estudianteSeleccionado.getId());
            mostrarAlerta("Éxito", "Eliminado", "Estudiante eliminado correctamente.");
            cargarEstudiantes(); // Actualizar la tabla

        } catch (Exception e) {
            mostrarAlerta("Error", "Error en BD", e.getMessage());
        }
    }

    @FXML
    void GoToBack(ActionEvent event) {
        Main.loadScene("/view/Students.fxml");
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    // ============ MÉTODOS AUXILIARES ============
    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}