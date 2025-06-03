package controller;

import data.MantenimientoDAO;
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
import model.Mantenimiento;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class TablaMantenimientoController {
    @FXML private TableView<Mantenimiento> TablaMantenimiento;
    @FXML private TableColumn<Mantenimiento, Integer> columnIdMantenimiento;
    @FXML private TableColumn<Mantenimiento, LocalDate> columnFechaMantenimiento;
    @FXML private TableColumn<Mantenimiento, String> columnResponsableMantenimiento;
    @FXML private TableColumn<Mantenimiento, String> columnEstadoMantenimiento;
    @FXML private TableColumn<Mantenimiento, Integer> columnIdSalaMantenimiento;
    @FXML private TableColumn<Mantenimiento, Integer> columnIdAudiovisualMantenimiento;
    @FXML private TableColumn<Mantenimiento, String> columnDescripcionMantenimiento;
    @FXML private Button btnMantenimiento;

    private MantenimientoDAO mantenimientoDAO;

    @FXML
    public void initialize() {
        try {
            Connection connection = data.DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            mantenimientoDAO = new MantenimientoDAO(connection);

            columnIdMantenimiento.setCellValueFactory(new PropertyValueFactory<>("idMantenimiento"));
            columnFechaMantenimiento.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            columnResponsableMantenimiento.setCellValueFactory(new PropertyValueFactory<>("responsable"));
            columnEstadoMantenimiento.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnIdSalaMantenimiento.setCellValueFactory(new PropertyValueFactory<>("idSala"));
            columnIdAudiovisualMantenimiento.setCellValueFactory(new PropertyValueFactory<>("idAudiovisual"));
            columnDescripcionMantenimiento.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

            TablaMantenimiento.setItems(mantenimientoDAO.obtenerTodas());
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    public void GoToMantenimiento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Mantenimiento.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnMantenimiento.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registro Mantenimiento");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista de registro: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}