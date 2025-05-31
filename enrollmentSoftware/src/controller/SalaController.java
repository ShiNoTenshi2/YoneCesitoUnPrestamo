package controller;

import data.SalaDAO;
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
import model.Sala;
import model.UsuarioSesion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List; // Importación añadida para List

public class SalaController {

    @FXML private TextField txtIdSala;
    @FXML private TextField txtNombreSala;
    @FXML private TextField txtCapacidad;
    @FXML private TextField txtDetallesSala;
    @FXML private ComboBox<String> comboBoxEstadoSala;
    @FXML private Button btnRegistrarSala;
    @FXML private Button btnLeerSala;
    @FXML private Button btnActualizarSala;
    @FXML private Button btnBorrarSala;
    @FXML private Button btnMenu;

    @FXML
    public void initialize() {
        // Verificar que el usuario es Coordinador
        try {
            if (!UsuarioSesion.getInstance().getRol().equals("Coordinador")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Esta vista es solo para Coordinadores.");
                GoToMenu();
                return;
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            GoToMenu();
            return;
        }

        // Inicializar ComboBox con los estados
        comboBoxEstadoSala.setItems(FXCollections.observableArrayList("Disponible", "Ocupado", "Mantenimiento"));
        comboBoxEstadoSala.setEditable(false);
        comboBoxEstadoSala.getSelectionModel().selectFirst();
    }

    @FXML
    private void RegistrarSala() {
        try {
            // Validar campos vacíos
            if (txtIdSala.getText().isEmpty() || txtNombreSala.getText().isEmpty() || 
                txtCapacidad.getText().isEmpty() || txtDetallesSala.getText().isEmpty() || 
                comboBoxEstadoSala.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios.");
                return;
            }

            // Validar ID y Capacidad
            long idSala;
            int capacidad;
            try {
                idSala = Long.parseLong(txtIdSala.getText());
                if (idSala <= 0) {
                    showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número positivo.");
                    return;
                }
                capacidad = Integer.parseInt(txtCapacidad.getText());
                if (capacidad <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Capacidad Inválida", "La capacidad debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Datos Inválidos", "ID y Capacidad deben ser números.");
                return;
            }

            // Validar longitud de nombre_sala y detalles_sala
            String nombreSala = txtNombreSala.getText().trim();
            String detallesSala = txtDetallesSala.getText().trim();
            if (nombreSala.length() > 20) {
                showAlert(Alert.AlertType.ERROR, "Error", "El nombre de la sala no puede exceder 20 caracteres.");
                return;
            }
            if (detallesSala.length() > 200) {
                showAlert(Alert.AlertType.ERROR, "Error", "Los detalles no pueden exceder 200 caracteres.");
                return;
            }

            // Crear objeto Sala
            Sala sala = new Sala(idSala, nombreSala, capacidad, detallesSala, comboBoxEstadoSala.getValue());

            // Registrar sala usando Singleton
            SalaDAO salaDAO = SalaDAO.getInstance();
            boolean registrado = salaDAO.registrarSala(sala);
            if (registrado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Sala registrada exitosamente.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar la sala. Verifique que el ID no esté registrado.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al registrar la sala: " + e.getMessage());
        }
    }

    @FXML
    private void LeerSala() {
        try {
            SalaDAO salaDAO = SalaDAO.getInstance();
            List<Sala> salas = salaDAO.obtenerTodas();
            if (salas.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Información", "No hay salas registradas.");
            } else {
                StringBuilder sb = new StringBuilder("=== LISTADO DE SALAS ===\n");
                for (Sala sala : salas) {
                    sb.append(sala.toString()).append("\n");
                }
                sb.append("=======================");
                showAlert(Alert.AlertType.INFORMATION, "Salas", sb.toString());
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al leer las salas: " + e.getMessage());
        }
    }

    @FXML
    private void ActualizarSala() {
        try {
            // Validar campos vacíos
            if (txtIdSala.getText().isEmpty() || txtNombreSala.getText().isEmpty() || 
                txtCapacidad.getText().isEmpty() || txtDetallesSala.getText().isEmpty() || 
                comboBoxEstadoSala.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos son obligatorios para actualizar.");
                return;
            }

            // Validar ID y Capacidad
            long idSala;
            int capacidad;
            try {
                idSala = Long.parseLong(txtIdSala.getText());
                if (idSala <= 0) {
                    showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número positivo.");
                    return;
                }
                capacidad = Integer.parseInt(txtCapacidad.getText());
                if (capacidad <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Capacidad Inválida", "La capacidad debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Datos Inválidos", "ID y Capacidad deben ser números.");
                return;
            }

            // Verificar que el ID exista
            SalaDAO salaDAO = SalaDAO.getInstance();
            if (!salaDAO.existeId(idSala)) {
                showAlert(Alert.AlertType.ERROR, "ID No Encontrado", "No existe una sala con ese ID.");
                return;
            }

            // Validar longitud de nombre_sala y detalles_sala
            String nombreSala = txtNombreSala.getText().trim();
            String detallesSala = txtDetallesSala.getText().trim();
            if (nombreSala.length() > 20) {
                showAlert(Alert.AlertType.ERROR, "Error", "El nombre de la sala no puede exceder 20 caracteres.");
                return;
            }
            if (detallesSala.length() > 200) {
                showAlert(Alert.AlertType.ERROR, "Error", "Los detalles no pueden exceder 200 caracteres.");
                return;
            }

            // Crear objeto Sala con los datos actualizados
            Sala sala = new Sala(idSala, nombreSala, capacidad, detallesSala, comboBoxEstadoSala.getValue());

            // Actualizar sala
            boolean actualizado = salaDAO.actualizarSala(sala);
            if (actualizado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Sala actualizada exitosamente.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la sala.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al actualizar la sala: " + e.getMessage());
        }
    }

    @FXML
    private void BorrarSala() {
        try {
            // Validar campo ID
            if (txtIdSala.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Campo Vacío", "Ingrese un ID para borrar.");
                return;
            }

            // Validar ID
            long idSala;
            try {
                idSala = Long.parseLong(txtIdSala.getText());
                if (idSala <= 0) {
                    showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número.");
                return;
            }

            // Verificar que el ID exista
            SalaDAO salaDAO = SalaDAO.getInstance();
            if (!salaDAO.existeId(idSala)) {
                showAlert(Alert.AlertType.ERROR, "ID No Encontrado", "No existe una sala con ese ID.");
                return;
            }

            // Borrar sala
            boolean eliminado = salaDAO.eliminarSala(idSala);
            if (eliminado) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Sala borrada exitosamente.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo borrar la sala.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al borrar la sala: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenu() {
        loadView("/view/MainMenu.fxml", "Menú Coordinador");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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
        txtIdSala.clear();
        txtNombreSala.clear();
        txtCapacidad.clear();
        txtDetallesSala.clear();
        comboBoxEstadoSala.getSelectionModel().selectFirst();
    }
}