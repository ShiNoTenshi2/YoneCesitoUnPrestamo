package controller;

import data.DBConnectionFactory;
import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.UsuarioSesion;
import java.io.IOException;
import java.sql.SQLException;

public class MenuUsuariosController {
    @FXML private TextField txtCedulaU;
    @FXML private PasswordField txtContraseñaU;
    @FXML private ComboBox<String> ComboBoxRolUsuario;
    @FXML private Button btnIngresoUsuario;
    @FXML private Button btnMenuInicial;

    @FXML
    public void initialize() {
        ComboBoxRolUsuario.setItems(FXCollections.observableArrayList("Coordinador", "Profesor", "Estudiante"));
        ComboBoxRolUsuario.setEditable(false);
        ComboBoxRolUsuario.getSelectionModel().selectFirst();
    }

    @FXML
    private void GoToIngresoUsuario() {
        String cedulaStr = txtCedulaU.getText().trim();
        String contrasena = txtContraseñaU.getText().trim();
        String rol = ComboBoxRolUsuario.getValue();

        if (cedulaStr.isEmpty() || contrasena.isEmpty() || rol == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Por favor, complete todos los campos.");
            return;
        }

        try {
            long cedulaUsuario = Long.parseLong(cedulaStr);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(); // Cambiado
            boolean autenticado = usuarioDAO.autenticarUsuario(cedulaUsuario, contrasena, rol);

            if (autenticado) {
                UsuarioSesion.getInstance(cedulaUsuario, rol);
                String fxmlFile;
                String title;
                switch (rol) {
                    case "Coordinador":
                        fxmlFile = "/view/MainMenu.fxml";
                        title = "Menú Coordinador";
                        break;
                    case "Profesor":
                    case "Estudiante":
                        fxmlFile = "/view/MainMenuProfeEstud.fxml";
                        title = "Menú " + rol;
                        break;
                    default:
                        showAlert(Alert.AlertType.ERROR, "Error", "Rol no válido.");
                        return;
                }
                loadView(fxmlFile, title);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Cédula, contraseña o rol incorrectos, o cuenta no aprobada.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "La cédula debe ser un número válido.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenuInicial() {
        loadView("/view/MenuInicial.fxml", "Sistema de Gestión");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnIngresoUsuario.getScene().getWindow();
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