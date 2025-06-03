package controller;

import data.AudiovisualDAO;
import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Audiovisual;
import java.io.IOException;
import java.sql.SQLException;

public class TablaAudiovisualController {

    @FXML private TableView<Audiovisual> tablaAudiovisual;
    @FXML private TableColumn<Audiovisual, Long> columnIdAudiovisual;
    @FXML private TableColumn<Audiovisual, String> columnNombreAudiovisual;
    @FXML private TableColumn<Audiovisual, String> columnEstadoAudiovisual;
    @FXML private TableColumn<Audiovisual, String> columnDetallesAudiovisual;
    @FXML private Button btnAudiovisual;
    @FXML private Button btnPrestamo;

    private String userRol;

    @FXML
    public void initialize() {
        try {
            // Obtener el rol del usuario
            userRol = UsuarioDAO.getInstance().getRol();
            if (!userRol.equals("Coordinador") && !userRol.equals("Estudiante") && !userRol.equals("Profesor")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Esta vista es solo para Coordinadores, Estudiantes y Profesores.");
                GoToPrestamo(); // Si no tiene acceso, lo mandamos a Prestamo.fxml
                return;
            }

            // Bloquear el botón "Volver a Audiovisuales" para Estudiantes y Profesores
            if (!userRol.equals("Coordinador")) {
                btnAudiovisual.setDisable(true);
            }

            // Configurar las columnas de la tabla con los nombres correctos de las propiedades
            columnIdAudiovisual.setCellValueFactory(new PropertyValueFactory<>("id_audiovisual"));
            columnNombreAudiovisual.setCellValueFactory(new PropertyValueFactory<>("nombre_audiov"));
            columnEstadoAudiovisual.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnDetallesAudiovisual.setCellValueFactory(new PropertyValueFactory<>("detalle_audiovisual"));

            // Cargar datos en la tabla
            cargarDatosTabla();
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            GoToPrestamo();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "No se pudieron cargar los audiovisuales: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al inicializar la tabla: " + e.getMessage());
        }
    }

    private void cargarDatosTabla() throws SQLException {
        AudiovisualDAO audiovisualDAO = AudiovisualDAO.getInstance();
        tablaAudiovisual.setItems(FXCollections.observableArrayList(audiovisualDAO.obtenerTodos()));
    }

    @FXML
    private void GoToAudiovisual() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Audiovisual.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAudiovisual.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registro Audiovisual");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de registro: " + e.getMessage());
        }
    }

    @FXML
    private void GoToPrestamo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Prestamo.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnPrestamo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Préstamos");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista de préstamos: " + e.getMessage());
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