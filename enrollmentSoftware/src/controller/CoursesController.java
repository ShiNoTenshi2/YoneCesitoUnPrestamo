package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Course;
import application.Main;
import data.CourseDAO;
import data.DBConnection;

public class CoursesController {

    // ============ CAMPOS DEL FORMULARIO ============
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCreditos;

    // ============ BOTONES ============
    @FXML private Button btnRegistrar;
    @FXML private Button btnVerCursos;
    @FXML private Button btnMenu;

    // ============ CONEXIÓN A DATOS ============
    private CourseDAO courseDAO = new CourseDAO(DBConnection.getInstance().getConnection());

    // ============ MÉTODOS DE ACCIÓN ============
    @FXML
    void registrarCurso(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String creditosStr = txtCreditos.getText().trim();

        if (!validarCampos(codigo, nombre, creditosStr)) return;
        if (!validarCreditos(creditosStr)) return;

        try {
            if (courseDAO.existsCode(codigo)) {
                mostrarAlerta("Error", "Código duplicado", "Este código ya está registrado.");
                return;
            }

            int creditos = Integer.parseInt(creditosStr);
            Course curso = new Course(codigo, nombre, creditos);
            courseDAO.save(curso);

            mostrarAlerta("Éxito", "Curso registrado", "¡Curso guardado correctamente!");
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error en BD", e.getMessage());
        }
    }

    @FXML
    void verCursos(ActionEvent event) {
        Main.loadScene("/view/LibraryCourses.fxml");  // Redirige a la tabla
    }

    @FXML
    void irAlMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    // ============ VALIDACIONES ============
    private boolean validarCampos(String codigo, String nombre, String creditos) {
        if (codigo.isEmpty() || nombre.isEmpty() || creditos.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    private boolean validarCreditos(String creditos) {
        try {
            int cred = Integer.parseInt(creditos);
            if (cred <= 0) {
                mostrarAlerta("Error", "Créditos inválidos", "Deben ser mayores a 0.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Créditos inválidos", "Debe ser un número entero.");
            return false;
        }
    }

    // ============ MÉTODOS AUXILIARES ============
    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtCreditos.clear();
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}