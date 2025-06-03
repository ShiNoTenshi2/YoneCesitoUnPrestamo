package controller;

import data.DevolucionDAO;
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
import model.Devolucion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class TablaDevolucionController {
    @FXML private TableView<Devolucion> tablaDevolucion;
    @FXML private TableColumn<Devolucion, Integer> columnIdDevolucion;
    @FXML private TableColumn<Devolucion, Integer> columnIdPrestamoDevolucion;
    @FXML private TableColumn<Devolucion, Integer> columnIdMantenimientoDevolucion;
    @FXML private TableColumn<Devolucion, String> columnEntregaDevolucion;
    @FXML private TableColumn<Devolucion, String> columnEstadoDevolucion;
    @FXML private TableColumn<Devolucion, LocalDate> columnFechaDevolucion;
    @FXML private TableColumn<Devolucion, String> columnDescripcionDevolucion;
    @FXML private Button btnDevolucion;

    private DevolucionDAO devolucionDAO;

    @FXML
    public void initialize() {
        try {
            // Obtener la conexión
            Connection connection = data.DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            // Inicializar DAO
            devolucionDAO = new DevolucionDAO(connection);

            // Configurar las columnas
            columnIdDevolucion.setCellValueFactory(new PropertyValueFactory<>("idDevolucion"));
            columnIdPrestamoDevolucion.setCellValueFactory(new PropertyValueFactory<>("idPrestamo"));
            columnIdMantenimientoDevolucion.setCellValueFactory(new PropertyValueFactory<>("idMantenimiento"));
            columnEntregaDevolucion.setCellValueFactory(new PropertyValueFactory<>("entrega"));
            columnEstadoDevolucion.setCellValueFactory(new PropertyValueFactory<>("estadoEquipo"));
            columnFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
            columnDescripcionDevolucion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

            // Cargar datos en la tabla
            tablaDevolucion.setItems(devolucionDAO.obtenerTodas());
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    public void GoToDevolucion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Devolucion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnDevolucion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registro Devolución");
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