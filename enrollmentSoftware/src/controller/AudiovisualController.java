package controller;

import javafx.event.ActionEvent;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Audiovisual;
import application.Main;
import data.DBConnection;
import data.AudiovisualDAO;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class AudiovisualController {

    @FXML private TextField txtIdAudiovisual;
    @FXML private TextField txtNombreAudiovisual;
    @FXML private TextField txtDetalleAudiovisual;
    @FXML private TextField txtEstadoAudiovisual;

    @FXML private Button btnRegistrarAudiovisual;
    @FXML private Button btnLeerAudiovisual;
    @FXML private Button btnActualizarAudiovisual;
    @FXML private Button btnBorrarAudiovisual;
    @FXML private Button btnMenu;

    private AudiovisualDAO audiovisualDAO = new AudiovisualDAO(DBConnection.getInstance().getConnection());

    @FXML
    public void RegistrarAudiovisual(ActionEvent event) {
        try {
            // Validar campos vacíos
            if (txtIdAudiovisual.getText().isEmpty() || 
                txtNombreAudiovisual.getText().isEmpty() ||
                txtDetalleAudiovisual.getText().isEmpty() ||
                txtEstadoAudiovisual.getText().isEmpty()) {
                
                mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios.");
                return;
            }

            // Validar ID
            int id;
            try {
                id = Integer.parseInt(txtIdAudiovisual.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID inválido", "El ID debe ser un número.");
                return;
            }

            // Verificar ID único
            if (audiovisualDAO.existeId(id)) {
                mostrarAlerta("Error", "ID duplicado", "Este ID de audiovisual ya está registrado.");
                return;
            }

            Audiovisual audiovisual = new Audiovisual(
                id,
                txtNombreAudiovisual.getText().trim(),
                txtDetalleAudiovisual.getText().trim(),
                txtEstadoAudiovisual.getText().trim()
            );
            
            audiovisualDAO.guardar(audiovisual);
            mostrarAlerta("Éxito", "Registro exitoso", "Audiovisual registrado correctamente.");
            limpiarCampos();

        } catch (SQLException e) {
            mostrarAlerta("Error en BD", "Error al guardar", e.getMessage());
        }
    }

    @FXML
    public void LeerAudiovisual(ActionEvent event) {
        try {
            System.out.println("=== LISTADO DE AUDIOVISUALES ===");
            for (Audiovisual a : audiovisualDAO.obtenerTodos()) {
                System.out.println(a.toString());
            }
            System.out.println("===============================");
            mostrarAlerta("Información", "Datos en consola", "Se mostraron todos los audiovisuales en la consola");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void ActualizarAudiovisual(ActionEvent event) {
        try {
            if (txtIdAudiovisual.getText().isEmpty()) {
                mostrarAlerta("Error", "Campo vacío", "Ingrese un ID para actualizar");
                return;
            }
            
            int id = Integer.parseInt(txtIdAudiovisual.getText());
            
            // Validar campos
            if (txtNombreAudiovisual.getText().isEmpty() || 
                txtDetalleAudiovisual.getText().isEmpty() ||
                txtEstadoAudiovisual.getText().isEmpty()) {
                mostrarAlerta("Error", "Campos vacíos", "Todos los campos son obligatorios para actualizar");
                return;
            }
            
            // Verificar que el ID existe
            if (!audiovisualDAO.existeId(id)) {
                mostrarAlerta("Error", "ID no encontrado", "No existe un audiovisual con ese ID");
                return;
            }
            
            Audiovisual audiovisual = new Audiovisual(
                id,
                txtNombreAudiovisual.getText().trim(),
                txtDetalleAudiovisual.getText().trim(),
                txtEstadoAudiovisual.getText().trim()
            );
            
            audiovisualDAO.actualizar(audiovisual);
            mostrarAlerta("Éxito", "Actualización exitosa", "Audiovisual actualizado correctamente");
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID inválido", "El ID debe ser un número");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void BorrarAudiovisual(ActionEvent event) {
        try {
            if (txtIdAudiovisual.getText().isEmpty()) {
                mostrarAlerta("Error", "Campo vacío", "Ingrese un ID para borrar");
                return;
            }
            
            int id = Integer.parseInt(txtIdAudiovisual.getText());
            
            // Verificar que el ID existe
            if (!audiovisualDAO.existeId(id)) {
                mostrarAlerta("Error", "ID no encontrado", "No existe un audiovisual con ese ID");
                return;
            }
            
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar borrado");
            confirmacion.setHeaderText("¿Está seguro de borrar este audiovisual?");
            confirmacion.setContentText("Esta acción no se puede deshacer");
            
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                audiovisualDAO.eliminar(id);
                mostrarAlerta("Éxito", "Borrado exitoso", "Audiovisual eliminado correctamente");
                limpiarCampos();
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID inválido", "El ID debe ser un número");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error de BD", e.getMessage());
        }
    }

    @FXML
    public void GoToMenu(ActionEvent event) {
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
        txtIdAudiovisual.clear();
        txtNombreAudiovisual.clear();
        txtDetalleAudiovisual.clear();
        txtEstadoAudiovisual.clear();
    }
}