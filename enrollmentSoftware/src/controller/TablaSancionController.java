package controller;

import data.SancionDAO;
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
import model.Sancion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class TablaSancionController {
    @FXML private TableView<Sancion> tablaSancion;
    @FXML private TableColumn<Sancion, Integer> columnIdSancion;
    @FXML private TableColumn<Sancion, Integer> columnIdDevolucionSancion;
    @FXML private TableColumn<Sancion, Long> columnCedulaUsuarioSancion;
    @FXML private TableColumn<Sancion, String> columnEstadoSancion;
    @FXML private TableColumn<Sancion, Double> columnMontoSancion;
    @FXML private TableColumn<Sancion, String> columnMotivoSancion;
    @FXML private Button btnSancion;

    private SancionDAO sancionDAO;

    @FXML
    public void initialize() {
        try {
            // Obtener la conexión
            Connection connection = data.DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            // Inicializar DAO
            sancionDAO = new SancionDAO(connection);

            // Configurar las columnas
            columnIdSancion.setCellValueFactory(new PropertyValueFactory<>("idSancion"));
            columnIdDevolucionSancion.setCellValueFactory(new PropertyValueFactory<>("idDevolucion"));
            columnCedulaUsuarioSancion.setCellValueFactory(new PropertyValueFactory<>("cedulaUsuario"));
            columnEstadoSancion.setCellValueFactory(new PropertyValueFactory<>("estado"));
            columnMontoSancion.setCellValueFactory(new PropertyValueFactory<>("monto"));
            columnMotivoSancion.setCellValueFactory(new PropertyValueFactory<>("motivo"));

            // Cargar datos en la tabla
            tablaSancion.setItems(sancionDAO.obtenerTodas());
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    public void GoToSancion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Sancion.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSancion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registro Sanción");
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