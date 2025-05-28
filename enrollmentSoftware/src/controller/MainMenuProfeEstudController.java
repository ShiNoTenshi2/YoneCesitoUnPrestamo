package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.UsuarioSesion;
import java.io.IOException;

public class MainMenuProfeEstudController {

    @FXML
    private Button btnPrestamoProfeEstu;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    public void initialize() {
        try {
            UsuarioSesion session = UsuarioSesion.getInstance();
            String rol = session.getRol();
            if (!rol.equals("Estudiante") && !rol.equals("Profesor")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Esta vista es solo para Estudiantes y Profesores.");
                GoToMenuInicial();
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            GoToMenuInicial();
        }
    }

    @FXML
    private void GoToPrestamo() {
        loadView("/view/Prestamo.fxml", "Gestión de Préstamos");
    }

    @FXML
    private void GoToMenuInicial() {
            UsuarioSesion.getInstance().destroy(); 
            loadView("/view/MenuInicial.fxml", "Menú Inicial");
            
        }
       
        
    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}