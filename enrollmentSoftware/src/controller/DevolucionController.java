package controller;

import data.DevolucionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Devolucion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class DevolucionController {
    @FXML private TextField txtIdDevolucion;
    @FXML private ComboBox<String> comboBoxEntregaDevo;
    @FXML private DatePicker fechaDevDatePicker;
    @FXML private ComboBox<String> comboBoxEstadoDevo;
    @FXML private TextField txtDescripcionDevol;
    @FXML private ComboBox<Integer> comboBoxIdPrestamoDevol;
    @FXML private ComboBox<Integer> comboBoxIdMantenimientoDevol;
    @FXML private Button btnRegistrarDevolucion;
    @FXML private Button btnLeerDevolucion;
    @FXML private Button btnActualizarDevolucion;
    @FXML private Button btnBorrarDevolucion;
    @FXML private Button btnMenu;

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

            // Llenar ComboBox de entrega
            comboBoxEntregaDevo.getItems().addAll("Puntual", "Tardio");
            comboBoxEntregaDevo.getSelectionModel().selectFirst();

            // Llenar ComboBox de estado_equipo
            comboBoxEstadoDevo.getItems().addAll("BuenEstado", "MalEstado");
            comboBoxEstadoDevo.getSelectionModel().selectFirst();

            // Cargar IDs de préstamos disponibles en el ComboBox
            comboBoxIdPrestamoDevol.setItems(devolucionDAO.obtenerIdsPrestamos());

            // Cargar IDs de mantenimientos disponibles en el ComboBox
            comboBoxIdMantenimientoDevol.setItems(devolucionDAO.obtenerIdsMantenimientos());
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
            try {
                GoToMenu();
            } catch (Exception ex) {
                mostrarAlerta("Error Crítico", "No se pudo regresar al menú principal: " + ex.getMessage());
            }
        }
    }

    @FXML
    public void RegistrarDevolucion() {
        try {
            if (!validarCampos()) return;

            Devolucion devolucion = new Devolucion(
                0, // ID se genera con la secuencia
                fechaDevDatePicker.getValue(),
                comboBoxEntregaDevo.getValue(),
                txtDescripcionDevol.getText(),
                comboBoxEstadoDevo.getValue(),
                comboBoxIdPrestamoDevol.getValue(),
                comboBoxIdMantenimientoDevol.getValue()
            );

            devolucionDAO.guardar(devolucion);
            mostrarAlerta("Éxito", "Devolución registrada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            String mensajeError = e.getMessage();
            String mensajeLimpio = mensajeError.contains("ORA-20002") 
                ? "La fecha de devolución no puede ser anterior a la fecha de solicitud del préstamo."
                : (mensajeError.contains("ORA-20003") 
                    ? "La fecha de devolución no puede ser anterior a la fecha del mantenimiento."
                    : mensajeError);
            mostrarAlerta("Error", mensajeLimpio);
        }
    }

    @FXML
    public void LeerDevolucion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DevolucionTabla.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLeerDevolucion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Tabla de Devoluciones");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista de tabla: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarDevolucion() {
        try {
            if (txtIdDevolucion.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la devolución.");
                return;
            }
            if (!validarCampos()) return;

            int idDevolucion = Integer.parseInt(txtIdDevolucion.getText());
            if (!devolucionDAO.existeId(idDevolucion)) {
                mostrarAlerta("Error", "El ID de devolución no existe.");
                return;
            }

            Devolucion devolucion = new Devolucion(
                idDevolucion,
                fechaDevDatePicker.getValue(),
                comboBoxEntregaDevo.getValue(),
                txtDescripcionDevol.getText(),
                comboBoxEstadoDevo.getValue(),
                comboBoxIdPrestamoDevol.getValue(),
                comboBoxIdMantenimientoDevol.getValue()
            );

            devolucionDAO.actualizar(devolucion);
            mostrarAlerta("Éxito", "Devolución actualizada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de devolución debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar la devolución: " + e.getMessage());
        }
    }

    @FXML
    public void BorrarDevolucion() {
        try {
            if (txtIdDevolucion.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la devolución.");
                return;
            }

            int idDevolucion = Integer.parseInt(txtIdDevolucion.getText());
            if (!devolucionDAO.existeId(idDevolucion)) {
                mostrarAlerta("Error", "El ID de devolución no existe.");
                return;
            }

            devolucionDAO.eliminar(idDevolucion);
            mostrarAlerta("Éxito", "Devolución borrada correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de devolución debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar la devolución: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenu() {
        try {
            loadView("/view/MainMenu.fxml", "Menú Coordinador");
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al cargar el menú: " + e.getMessage());
        }
    }

    private void loadView(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) btnMenu.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private boolean validarCampos() {
        LocalDate fechaActual = LocalDate.now();

        if (fechaDevDatePicker.getValue() == null ||
            comboBoxEntregaDevo.getValue() == null ||
            comboBoxEstadoDevo.getValue() == null ||
            txtDescripcionDevol.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        if (fechaDevDatePicker.getValue().isBefore(fechaActual)) {
            mostrarAlerta("Error", "La fecha de devolución no puede ser anterior a hoy (" + fechaActual + ").");
            return false;
        }

        Integer idPrestamo = comboBoxIdPrestamoDevol.getValue();
        Integer idMantenimiento = comboBoxIdMantenimientoDevol.getValue();
        if (idPrestamo == null && idMantenimiento == null) {
            mostrarAlerta("Error", "Debe seleccionar al menos un ID de préstamo o un ID de mantenimiento.");
            return false;
        }

        if (idPrestamo != null && idMantenimiento != null) {
            mostrarAlerta("Error", "No puede seleccionar un ID de préstamo y un ID de mantenimiento al mismo tiempo.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtIdDevolucion.clear();
        comboBoxEntregaDevo.getSelectionModel().selectFirst();
        fechaDevDatePicker.setValue(null);
        comboBoxEstadoDevo.getSelectionModel().selectFirst();
        txtDescripcionDevol.clear();
        comboBoxIdPrestamoDevol.getSelectionModel().clearSelection();
        comboBoxIdMantenimientoDevol.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}