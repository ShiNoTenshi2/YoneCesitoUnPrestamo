package controller;

import javafx.event.ActionEvent;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Solicitantes;
import application.Main;
import data.DBConnection;
import data.SolicitantesDAO;

public class SolicitantesController {

    @FXML private TextField txtIdSolicitante;
    @FXML private TextField txtNombreSolicitante;
    @FXML private TextField txtCorreoSolicitante;
    @FXML private TextField txtTelefonoSolicitante;

    @FXML private Button btnRegistrarSolicitante;
    @FXML private Button btnMenu;

    private SolicitantesDAO solicitanteDAO = new SolicitantesDAO(DBConnection.getInstance().getConnection());

    @FXML
    void RegistrarSolicitante(ActionEvent event) {
        try {
            // Validar campos vacíos
            if (txtIdSolicitante.getText().isEmpty() || 
                txtNombreSolicitante.getText().isEmpty() ||
                txtCorreoSolicitante.getText().isEmpty() ||
                txtTelefonoSolicitante.getText().isEmpty()) {
                
                mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios.");
                return;
            }

            // Validar ID
            int id;
            try {
                id = Integer.parseInt(txtIdSolicitante.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID inválido", "El ID debe ser un número.");
                return;
            }

            // Validar teléfono (10 dígitos)
            String telefono = txtTelefonoSolicitante.getText().trim();
            if (!telefono.matches("\\d{10}")) {
                mostrarAlerta("Error", "Teléfono inválido", "Debe tener exactamente 10 dígitos.");
                return;
            }

            // Validar correo
            String correo = txtCorreoSolicitante.getText().trim();
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                mostrarAlerta("Error", "Correo inválido", "Formato: usuario@dominio.com");
                return;
            }

            // Verificar ID único
            if (solicitanteDAO.existeId(id)) {
                mostrarAlerta("Error", "ID duplicado", "Este ID ya está registrado.");
                return;
            }

            // Crear y guardar
            Solicitantes solicitante = new Solicitantes(
                id,
                txtNombreSolicitante.getText().trim(),
                correo,
                telefono
            );
            
            solicitanteDAO.guardar(solicitante);
            mostrarAlerta("Éxito", "Registro exitoso", "Solicitante registrado correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta("Error en BD", "Error al guardar", e.getMessage());
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private void limpiarCampos() {
        txtIdSolicitante.clear();
        txtNombreSolicitante.clear();
        txtCorreoSolicitante.clear();
        txtTelefonoSolicitante.clear();
    }
}