package controller;

import data.MantenimientoDAO;
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
import model.Mantenimiento;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class MantenimientoController {
    @FXML private TextField txtIdMantenimiento;
    @FXML private TextField txtResponsableMantenimiento;
    @FXML private TextField txtDescripcionMantenimiento;
    @FXML private ComboBox<Integer> comboBoxIdSalaMantenimiento;
    @FXML private ComboBox<Integer> comboBoxIdAudiovisualMantenimiento;
    @FXML private DatePicker fechaMantDatePicker;
    @FXML private Button btnRegistrarMantenimiento;
    @FXML private Button btnLeerMantenimiento;
    @FXML private Button btnActualizarMantenimiento;
    @FXML private Button btnBorrarMantenimiento;
    @FXML private Button btnMenu;

    private MantenimientoDAO mantenimientoDAO;

    @FXML
    public void initialize() {
        try {
            Connection connection = data.DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            mantenimientoDAO = new MantenimientoDAO(connection);

            comboBoxIdSalaMantenimiento.setItems(mantenimientoDAO.obtenerIdsSalasDisponibles());
            comboBoxIdAudiovisualMantenimiento.setItems(mantenimientoDAO.obtenerIdsAudiovisualesDisponibles());
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
    public void RegistrarMantenimiento() {
        if (mantenimientoDAO == null) {
            mostrarAlerta("Error", "No se puede registrar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            if (!validarCampos()) return;

            Mantenimiento mantenimiento = new Mantenimiento(
                0,
                fechaMantDatePicker.getValue(),
                txtDescripcionMantenimiento.getText(),
                txtResponsableMantenimiento.getText(),
                null,
                comboBoxIdSalaMantenimiento.getValue(),
                comboBoxIdAudiovisualMantenimiento.getValue()
            );

            mantenimientoDAO.guardar(mantenimiento);
            mostrarAlerta("Éxito", "Mantenimiento registrado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar el mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    public void LeerMantenimiento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MantenimientoTabla.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLeerMantenimiento.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Tabla de Mantenimientos");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista de tabla: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarMantenimiento() {
        if (mantenimientoDAO == null) {
            mostrarAlerta("Error", "No se puede actualizar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            if (txtIdMantenimiento.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del mantenimiento.");
                return;
            }
            if (!validarCampos()) return;

            int idMantenimiento = Integer.parseInt(txtIdMantenimiento.getText());
            if (!mantenimientoDAO.existeId(idMantenimiento)) {
                mostrarAlerta("Error", "El ID de mantenimiento no existe.");
                return;
            }

            Mantenimiento mantenimiento = new Mantenimiento(
                idMantenimiento,
                fechaMantDatePicker.getValue(),
                txtDescripcionMantenimiento.getText(),
                txtResponsableMantenimiento.getText(),
                "EnProceso",
                comboBoxIdSalaMantenimiento.getValue(),
                comboBoxIdAudiovisualMantenimiento.getValue()
            );

            mantenimientoDAO.actualizar(mantenimiento);
            mostrarAlerta("Éxito", "Mantenimiento actualizado correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de mantenimiento debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar el mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    public void BorrarMantenimiento() {
        if (mantenimientoDAO == null) {
            mostrarAlerta("Error", "No se puede borrar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            if (txtIdMantenimiento.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del mantenimiento.");
                return;
            }

            int idMantenimiento = Integer.parseInt(txtIdMantenimiento.getText());
            if (!mantenimientoDAO.existeId(idMantenimiento)) {
                mostrarAlerta("Error", "El ID de mantenimiento no existe.");
                return;
            }

            mantenimientoDAO.eliminar(idMantenimiento);
            mostrarAlerta("Éxito", "Mantenimiento borrado correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de mantenimiento debe ser numérico.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar el mantenimiento: " + e.getMessage());
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

        if (fechaMantDatePicker.getValue() == null ||
            txtDescripcionMantenimiento.getText().isEmpty() ||
            txtResponsableMantenimiento.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        if (fechaMantDatePicker.getValue().isBefore(fechaActual)) {
            mostrarAlerta("Error", "La fecha del mantenimiento no puede ser anterior a hoy (" + fechaActual + ").");
            return false;
        }

        Integer idSala = comboBoxIdSalaMantenimiento.getValue();
        Integer idAudiovisual = comboBoxIdAudiovisualMantenimiento.getValue();
        if (idSala == null && idAudiovisual == null) {
            mostrarAlerta("Error", "Debe seleccionar al menos un ID de sala o un ID de audiovisual.");
            return false;
        }

        if (idSala != null && idAudiovisual != null) {
            mostrarAlerta("Error", "No puede seleccionar un ID de sala y un ID de audiovisual al mismo tiempo.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtIdMantenimiento.clear();
        txtResponsableMantenimiento.clear();
        txtDescripcionMantenimiento.clear();
        comboBoxIdSalaMantenimiento.getSelectionModel().clearSelection();
        comboBoxIdAudiovisualMantenimiento.getSelectionModel().clearSelection();
        fechaMantDatePicker.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}