package controller;

import data.AudiovisualDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Audiovisual;
import model.UsuarioSesion;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class AudiovisualController {

    @FXML private TextField txtIdAudiovisual;
    @FXML private TextField txtNombreAudiovisual;
    @FXML private TextField txtDetalleAudiovisual;
    @FXML private ComboBox<String> comboBoxEstadoAudiovisual;
    @FXML private Button btnRegistrarAudiovisual;
    @FXML private Button btnLeerAudiovisual;
    @FXML private Button btnActualizarAudiovisual;
    @FXML private Button btnBorrarAudiovisual;
    @FXML private Button btnMenu;

    private AudiovisualDAO audiovisualDAO;

    @FXML
    public void initialize() {
        try {
            // Verificar que el usuario es Coordinador
            if (!UsuarioSesion.getInstance().getRol().equals("Coordinador")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Esta vista es solo para Coordinadores.");
                GoToMenu();
                return;
            }

            // Inicializar ComboBox con los estados
            comboBoxEstadoAudiovisual.setItems(FXCollections.observableArrayList("Disponible", "Ocupado", "Mantenimiento"));
            comboBoxEstadoAudiovisual.setEditable(false);
            comboBoxEstadoAudiovisual.getSelectionModel().selectFirst();

            // Inicializar DAO
            audiovisualDAO = AudiovisualDAO.getInstance();
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            GoToMenu();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al inicializar: " + e.getMessage());
        }
    }

    @FXML
    private void RegistrarAudiovisual() {
        try {
            // Validar campos obligatorios
            if (txtNombreAudiovisual.getText().isEmpty() || txtDetalleAudiovisual.getText().isEmpty() || 
                comboBoxEstadoAudiovisual.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios.");
                return;
            }

            // Validar nombre y detalles
            String nombreAudiov = txtNombreAudiovisual.getText().trim();
            String detalleAudiovisual = txtDetalleAudiovisual.getText().trim();
            if (nombreAudiov.length() > 20) {
                showAlert(Alert.AlertType.ERROR, "Error", "El nombre no puede exceder 20 caracteres.");
                return;
            }
            if (detalleAudiovisual.length() > 200) {
                showAlert(Alert.AlertType.ERROR, "Error", "Los detalles no pueden exceder 200 caracteres.");
                return;
            }

            // Manejar ID (opcional para registrar)
            long idAudiovisual = 0; // ID se genera automáticamente si está vacío
            if (!txtIdAudiovisual.getText().isEmpty()) {
                try {
                    idAudiovisual = Long.parseLong(txtIdAudiovisual.getText());
                    if (idAudiovisual < 0) {
                        showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID no puede ser negativo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número o dejarlo vacío para registrar.");
                    return;
                }
            }

            Audiovisual audiovisual = new Audiovisual(idAudiovisual, nombreAudiov, detalleAudiovisual, comboBoxEstadoAudiovisual.getValue());
            boolean registrado = audiovisualDAO.registrarAudiovisual(audiovisual);
            if (registrado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Audiovisual registrado exitosamente.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar el audiovisual. Verifique los datos.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al registrar el audiovisual: " + e.getMessage());
        }
    }

    @FXML
    private void LeerAudiovisual() {
        try {
            URL resource = getClass().getResource("/view/AudiovisualTabla.fxml");
            if (resource == null) {
                throw new IOException("No se encontró el archivo /view/AudiovisualTabla.fxml. Verifica la ruta en src/main/resources/view/");
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) btnLeerAudiovisual.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Tabla de Audiovisuales");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de tabla: " + e.getMessage());
        }
    }

    @FXML
    private void ActualizarAudiovisual() {
        try {
            // Validar campos obligatorios
            if (txtIdAudiovisual.getText().isEmpty() || txtNombreAudiovisual.getText().isEmpty() || 
                txtDetalleAudiovisual.getText().isEmpty() || comboBoxEstadoAudiovisual.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios para actualizar.");
                return;
            }

            // Validar ID
            long idAudiovisual;
            try {
                idAudiovisual = Long.parseLong(txtIdAudiovisual.getText());
                if (idAudiovisual <= 0) {
                    showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número válido.");
                return;
            }

            // Verificar que el ID exista
            if (!audiovisualDAO.existeId(idAudiovisual)) {
                showAlert(Alert.AlertType.ERROR, "ID No Encontrado", "No existe un audiovisual con ese ID.");
                return;
            }

            // Validar nombre y detalles
            String nombreAudiov = txtNombreAudiovisual.getText().trim();
            String detalleAudiovisual = txtDetalleAudiovisual.getText().trim();
            if (nombreAudiov.length() > 20) {
                showAlert(Alert.AlertType.ERROR, "Error", "El nombre no puede exceder 20 caracteres.");
                return;
            }
            if (detalleAudiovisual.length() > 200) {
                showAlert(Alert.AlertType.ERROR, "Error", "Los detalles no pueden exceder 200 caracteres.");
                return;
            }

            Audiovisual audiovisual = new Audiovisual(idAudiovisual, nombreAudiov, detalleAudiovisual, comboBoxEstadoAudiovisual.getValue());
            boolean actualizado = audiovisualDAO.actualizarAudiovisual(audiovisual);
            if (actualizado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Audiovisual actualizado exitosamente.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el audiovisual.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al actualizar el audiovisual: " + e.getMessage());
        }
    }

    @FXML
    private void BorrarAudiovisual() {
        try {
            if (txtIdAudiovisual.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Campo Vacío", "Ingrese un ID para borrar.");
                return;
            }

            // Validar ID
            long idAudiovisual;
            try {
                idAudiovisual = Long.parseLong(txtIdAudiovisual.getText());
                if (idAudiovisual <= 0) {
                    showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número válido.");
                return;
            }

            // Verificar que el ID exista
            if (!audiovisualDAO.existeId(idAudiovisual)) {
                showAlert(Alert.AlertType.ERROR, "ID No Encontrado", "No existe un audiovisual con ese ID.");
                return;
            }

            boolean eliminado = audiovisualDAO.eliminarAudiovisual(idAudiovisual);
            if (eliminado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Audiovisual borrado exitosamente.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo borrar el audiovisual.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al borrar el audiovisual: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenu() {
        loadView("/view/MainMenu.fxml", "Menú Coordinador");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                throw new IOException("No se encontró el archivo " + fxmlPath + ". Verifica la ruta en src/main/resources/view/");
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
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

    private void limpiarCampos() {
        txtIdAudiovisual.clear();
        txtNombreAudiovisual.clear();
        txtDetalleAudiovisual.clear();
        comboBoxEstadoAudiovisual.getSelectionModel().selectFirst();
    }
}