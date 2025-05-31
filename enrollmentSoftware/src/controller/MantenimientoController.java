package controller;

import data.DBConnectionFactory;
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
            // Obtener la conexión
            Connection connection = DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            // Inicializar DAO
            mantenimientoDAO = new MantenimientoDAO(connection);

            // Cargar IDs de salas disponibles en el ComboBox
            try {
                comboBoxIdSalaMantenimiento.setItems(mantenimientoDAO.obtenerIdsSalasDisponibles());
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudieron cargar los IDs de salas: " + e.getMessage());
            }

            // Cargar IDs de audiovisuales disponibles en el ComboBox
            try {
                comboBoxIdAudiovisualMantenimiento.setItems(mantenimientoDAO.obtenerIdsAudiovisualesDisponibles());
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudieron cargar los IDs de audiovisuales: " + e.getMessage());
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
            // Regresar al menú principal si no se puede conectar
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
            // Validar campos
            if (!validarCampos()) return;

            // Crear nuevo mantenimiento
            Mantenimiento mantenimiento = new Mantenimiento(
                0, // ID se genera con la secuencia
                fechaMantDatePicker.getValue(),
                txtDescripcionMantenimiento.getText(),
                txtResponsableMantenimiento.getText(),
                comboBoxIdSalaMantenimiento.getValue(),
                comboBoxIdAudiovisualMantenimiento.getValue()
            );

            // Guardar en la base de datos
            mantenimientoDAO.guardar(mantenimiento);
            mostrarAlerta("Éxito", "Mantenimiento registrado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar el mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    public void LeerMantenimiento() {
        if (mantenimientoDAO == null) {
            mostrarAlerta("Error", "No se puede leer: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Obtener todos los mantenimientos
            var mantenimientos = mantenimientoDAO.obtenerTodas();

            if (mantenimientos.isEmpty()) {
                mostrarAlerta("Información", "No hay mantenimientos registrados.");
                return;
            }

            // Construir el texto para mostrar en la alerta
            StringBuilder sb = new StringBuilder();
            sb.append("Lista de Mantenimientos:\n\n");
            for (Mantenimiento mantenimiento : mantenimientos) {
                sb.append(mantenimiento.toString()).append("\n");
            }

            // Mostrar los mantenimientos en una alerta
            mostrarAlerta("Mantenimientos Registrados", sb.toString());
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer los mantenimientos: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarMantenimiento() {
        if (mantenimientoDAO == null) {
            mostrarAlerta("Error", "No se puede actualizar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar ID
            String idText = txtIdMantenimiento.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del mantenimiento.");
                return;
            }
            int idMantenimiento = Integer.parseInt(idText);

            // Validar otros campos
            if (!validarCampos()) return;

            // Verificar si el ID existe
            if (!mantenimientoDAO.existeId(idMantenimiento)) {
                mostrarAlerta("Error", "El ID de mantenimiento no existe.");
                return;
            }

            // Crear mantenimiento actualizado
            Mantenimiento mantenimiento = new Mantenimiento(
                idMantenimiento,
                fechaMantDatePicker.getValue(),
                txtDescripcionMantenimiento.getText(),
                txtResponsableMantenimiento.getText(),
                comboBoxIdSalaMantenimiento.getValue(),
                comboBoxIdAudiovisualMantenimiento.getValue()
            );

            // Actualizar en la base de datos
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
            // Validar ID
            String idText = txtIdMantenimiento.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del mantenimiento.");
                return;
            }
            int idMantenimiento = Integer.parseInt(idText);

            // Verificar si el ID existe
            if (!mantenimientoDAO.existeId(idMantenimiento)) {
                mostrarAlerta("Error", "El ID de mantenimiento no existe.");
                return;
            }

            // Eliminar de la base de datos
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

        // Validar campos obligatorios
        if (fechaMantDatePicker.getValue() == null ||
            txtDescripcionMantenimiento.getText().isEmpty() ||
            txtResponsableMantenimiento.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        // Validar que la fecha no sea anterior a hoy
        if (fechaMantDatePicker.getValue().isBefore(fechaActual)) {
            mostrarAlerta("Error", "La fecha del mantenimiento no puede ser anterior a hoy (" + fechaActual + ").");
            return false;
        }

        // Validar que al menos un ComboBox tenga un valor seleccionado
        Integer idSala = comboBoxIdSalaMantenimiento.getValue();
        Integer idAudiovisual = comboBoxIdAudiovisualMantenimiento.getValue();
        if (idSala == null && idAudiovisual == null) {
            mostrarAlerta("Error", "Debe seleccionar al menos un ID de sala o un ID de audiovisual.");
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