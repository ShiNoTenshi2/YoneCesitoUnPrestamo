package controller;

import data.SalaDAO;
import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Sala;
import java.io.IOException;
import java.sql.SQLException;

public class TablaSalasController {

    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, Long> columnIdSalas;
    @FXML private TableColumn<Sala, String> columnNombreSalas;
    @FXML private TableColumn<Sala, Integer> columnCapacidadSalas;
    @FXML private TableColumn<Sala, String> columnEstadoSalas;
    @FXML private TableColumn<Sala, String> columnDetallesSalas;
    @FXML private Button btnSala;
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

            // Bloquear el botón "Volver a Sala" para Estudiantes y Profesores
            if (!userRol.equals("Coordinador")) {
                btnSala.setDisable(true);
            }

            // Inicializar la tabla con los nombres correctos de las propiedades
            columnIdSalas.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
            columnNombreSalas.setCellValueFactory(new PropertyValueFactory<>("nombre_sala"));
            columnCapacidadSalas.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
            columnEstadoSalas.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnDetallesSalas.setCellValueFactory(new PropertyValueFactory<>("detalles_sala"));

            // Cargar datos en la tabla
            ObservableList<Sala> salas = FXCollections.observableArrayList(SalaDAO.getInstance().obtenerTodas());
            tablaSalas.setItems(salas);
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            GoToPrestamo();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "No se pudieron cargar las salas: " + e.getMessage());
        }
    }

    @FXML
    private void GoToSala() {
        loadView("/view/Sala.fxml", "Gestión de Salas");
    }

    @FXML
    private void GoToPrestamo() {
        loadView("/view/Prestamo.fxml", "Gestión de Préstamos");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnSala.getScene().getWindow();
            stage.setScene(new Scene(root));
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