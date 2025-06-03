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

public class MainMenuController {

    @FXML
    private Button btnSolicitudes;

    @FXML
    private Button btnSala;

    @FXML
    private Button btnAudiovisual;

    @FXML
    private Button btnPrestamo;

    @FXML
    private Button btnDevolucion;

    @FXML
    private Button btnSancion;

    @FXML
    private Button btnMantenimiento;

    @FXML
    private Button btnGestionPrestamos;

    @FXML
    private Button btnGenerarReporte;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    public void initialize() {
        // Verificar que el usuario tiene rol Coordinador
        try {
            UsuarioSesion session = UsuarioSesion.getInstance();
            if (!session.getRol().equals("Coordinador")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Esta vista es solo para Coordinadores.");
                goToMenuInicial();
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            goToMenuInicial();
        }
    }
        
    @FXML
    private void GoToSolicitudes() {
        loadView("/view/Solicitudes.fxml", "Gestión de Solicitudes");
    }

    @FXML
    private void GoToSala() {
        loadView("/view/Sala.fxml", "Gestión de Salas");
    }

    @FXML
    private void GoToAudiovisual() {
        loadView("/view/Audiovisual.fxml", "Gestión de Audiovisuales");
    }

    @FXML
    private void GoToPrestamo() {
        loadView("/view/Prestamo.fxml", "Gestión de Préstamos");
    }

    @FXML
    private void GoToDevolucion() {
        loadView("/view/Devolucion.fxml", "Gestión de Devoluciones");
    }

    @FXML
    private void GoToSancion() {
        loadView("/view/Sancion.fxml", "Gestión de Sanciones");
    }

    @FXML
    private void GoToMantenimiento() {
        loadView("/view/Mantenimiento.fxml", "Gestión de Mantenimiento");
    }

    @FXML
    private void GenerarReporte() {
        loadView("/view/GenerarReporte.fxml", "Generar Reporte");
    }

    @FXML
    private void GoToMenuInicial() {
        // Destruir la sesión
        try {
            UsuarioSesion.getInstance().destroy();
        } catch (IllegalStateException e) {
            // Sesión ya destruida o no existe
        }
        loadView("/view/MenuInicial.fxml", "Sistema de Gestión");
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

    private void goToMenuInicial() {
        loadView("/view/MenuInicial.fxml", "Sistema de Gestión");
    }
}