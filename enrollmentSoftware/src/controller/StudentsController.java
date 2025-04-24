package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Student;
import application.Main;
import data.DBConnection;
import data.StudentDAO;

public class StudentsController {

    // ============ CAMPOS DEL FORMULARIO ============
    @FXML private TextField txtIdEstudiante;
    @FXML private TextField txtNombreEstudiante;
    @FXML private TextField txtEmailEstudiante;

    // ============ BOTONES ============
    @FXML private Button btnRegistrarEstudiante;
    @FXML private Button btnVerEstudiantes;
    @FXML private Button btnMenu;

    // ============ CONEXIÓN A DATOS ============
    private StudentDAO studentDAO = new StudentDAO(DBConnection.getInstance().getConnection());

    // ============ MÉTODO DE REGISTRO ============
    @FXML
    void RegistrarEstudiante(ActionEvent event) {
        String id = txtIdEstudiante.getText().trim();
        String nombre = txtNombreEstudiante.getText().trim();
        String email = txtEmailEstudiante.getText().trim();

        if (!validarCampos(id, nombre, email)) return;
        if (!validarId(id)) return;
        if (!validarEmail(email)) return;

        try {
            if (studentDAO.authenticate(id)) {
                mostrarAlerta("Error", "ID duplicado", "Esta identificación ya está registrada.");
                return;
            }

            if (studentDAO.existsEmail(email)) {
                mostrarAlerta("Error", "Email duplicado", "Este correo ya está registrado.");
                return;
            }

            Student estudiante = new Student(id, nombre, email);
            studentDAO.save(estudiante);
            
            mostrarAlerta("Éxito", "Registro exitoso", "Estudiante registrado correctamente.");
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error en base de datos", e.getMessage());
        }
    }

    // ============ VALIDACIONES ============
    private boolean validarCampos(String id, String nombre, String email) {
        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    private boolean validarId(String id) {
        if (!id.matches("\\d{8}")) {
            mostrarAlerta("Error", "ID inválido", "Debe tener 8 dígitos numéricos.");
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mostrarAlerta("Error", "Email inválido", "Formato: usuario@dominio.com");
            return false;
        }
        return true;
    }

    // ============ NAVEGACIÓN ============
    @FXML
    void VerEstudiantes(ActionEvent event) {
        Main.loadScene("/view/LibraryStudents.fxml");  
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
    
    private void limpiarCampos() {
        txtIdEstudiante.clear();
        txtNombreEstudiante.clear();
        txtEmailEstudiante.clear();
    }
}