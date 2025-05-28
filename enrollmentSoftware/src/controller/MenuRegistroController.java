package controller;

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
import java.io.IOException;
import java.sql.SQLException;

public class MenuRegistroController {
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtCedulaUsuario;
    @FXML private PasswordField txtContraseñaUsuario;
    @FXML private ComboBox<String> ComboBoxRolUsuarioRegistro;
    @FXML private TextField txtCorreoUsuario;
    @FXML private Button btnRegistrarUsuario;
    @FXML private Button btnMenuInicial;

    @FXML
    public void initialize() {
        ComboBoxRolUsuarioRegistro.setItems(FXCollections.observableArrayList("Coordinador", "Profesor", "Estudiante"));
        ComboBoxRolUsuarioRegistro.setEditable(false);
        ComboBoxRolUsuarioRegistro.getSelectionModel().selectFirst();
    }

    @FXML
    private void RegistrarUsuario() {
        String nombreCompleto = txtNombreUsuario.getText().trim();
        String cedulaStr = txtCedulaUsuario.getText().trim();
        String contrasena = txtContraseñaUsuario.getText().trim();
        String rol = ComboBoxRolUsuarioRegistro.getValue();
        String correo = txtCorreoUsuario.getText().trim();

        if (nombreCompleto.isEmpty() || cedulaStr.isEmpty() || contrasena.isEmpty() || rol == null || correo.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Por favor, complete todos los campos.");
            return;
        }

        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Ingrese un correo electrónico válido.");
            return;
        }

        try {
            long cedulaUsuario = Long.parseLong(cedulaStr);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(); // Cambiado
            boolean registrado = usuarioDAO.registrarUsuario(cedulaUsuario, nombreCompleto, contrasena, rol, "EnRevision", correo);

            if (registrado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado exitosamente. En espera de aprobación.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar el usuario. Verifique que la cédula no esté registrada.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "La cédula debe ser un número válido.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenuInicial() {
        try {
            loadView("/view/MenuInicial.fxml", "Sistema de Gestión");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista inicial: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtNombreUsuario.clear();
        txtCedulaUsuario.clear();
        txtContraseñaUsuario.clear();
        txtCorreoUsuario.clear();
        ComboBoxRolUsuarioRegistro.getSelectionModel().selectFirst();
    }

    private void loadView(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) btnMenuInicial.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}