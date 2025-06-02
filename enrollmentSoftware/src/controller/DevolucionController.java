package controller;

import data.DBConnectionFactory;
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
            Connection connection = DBConnectionFactory.getConnectionByRole("Coordinador").getConnection();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La conexión a la base de datos no está disponible.");
            }

            // Inicializar DAO
            devolucionDAO = new DevolucionDAO(connection);

            // Llenar ComboBox de entrega
            comboBoxEntregaDevo.getItems().addAll("Puntual", "Tardio");

            // Llenar ComboBox de estado_equipo
            comboBoxEstadoDevo.getItems().addAll("BuenEstado", "MalEstado");

            // Cargar IDs de préstamos disponibles en el ComboBox
            try {
                comboBoxIdPrestamoDevol.setItems(devolucionDAO.obtenerIdsPrestamos());
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudieron cargar los IDs de préstamos: " + e.getMessage());
            }

            // Cargar IDs de mantenimientos disponibles en el ComboBox
            try {
                comboBoxIdMantenimientoDevol.setItems(devolucionDAO.obtenerIdsMantenimientos());
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudieron cargar los IDs de mantenimientos: " + e.getMessage());
            }
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
        if (devolucionDAO == null) {
            mostrarAlerta("Error", "No se puede registrar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar campos
            if (!validarCampos()) return;

            // Crear nueva devolución
            Devolucion devolucion = new Devolucion(
                0, // ID se genera con la secuencia
                fechaDevDatePicker.getValue(),
                comboBoxEntregaDevo.getValue(),
                txtDescripcionDevol.getText(),
                comboBoxEstadoDevo.getValue(),
                comboBoxIdPrestamoDevol.getValue(),
                comboBoxIdMantenimientoDevol.getValue()
            );

            // Guardar en la base de datos
            devolucionDAO.guardar(devolucion);
            mostrarAlerta("Éxito", "Devolución registrada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            // Extraer solo el mensaje útil del error
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
        if (devolucionDAO == null) {
            mostrarAlerta("Error", "No se puede leer: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Obtener todas las devoluciones
            var devoluciones = devolucionDAO.obtenerTodas();

            if (devoluciones.isEmpty()) {
                mostrarAlerta("Información", "No hay devoluciones registradas.");
                return;
            }

            // Construir el texto para mostrar en la alerta
            StringBuilder sb = new StringBuilder();
            sb.append("Lista de Devoluciones:\n\n");
            for (Devolucion devolucion : devoluciones) {
                sb.append(devolucion.toString()).append("\n");
            }

            // Mostrar las devoluciones en una alerta
            mostrarAlerta("Devoluciones Registradas", sb.toString());
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer las devoluciones: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarDevolucion() {
        if (devolucionDAO == null) {
            mostrarAlerta("Error", "No se puede actualizar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar ID
            String idText = txtIdDevolucion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la devolución.");
                return;
            }
            int idDevolucion = Integer.parseInt(idText);

            // Validar otros campos
            if (!validarCampos()) return;

            // Verificar si el ID existe
            if (!devolucionDAO.existeId(idDevolucion)) {
                mostrarAlerta("Error", "El ID de devolución no existe.");
                return;
            }

            // Crear devolución actualizada
            Devolucion devolucion = new Devolucion(
                idDevolucion,
                fechaDevDatePicker.getValue(),
                comboBoxEntregaDevo.getValue(),
                txtDescripcionDevol.getText(),
                comboBoxEstadoDevo.getValue(),
                comboBoxIdPrestamoDevol.getValue(),
                comboBoxIdMantenimientoDevol.getValue()
            );

            // Actualizar en la base de datos
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
        if (devolucionDAO == null) {
            mostrarAlerta("Error", "No se puede borrar: la conexión a la base de datos no está disponible.");
            return;
        }

        try {
            // Validar ID
            String idText = txtIdDevolucion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la devolución.");
                return;
            }
            int idDevolucion = Integer.parseInt(idText);

            // Verificar si el ID existe
            if (!devolucionDAO.existeId(idDevolucion)) {
                mostrarAlerta("Error", "El ID de devolución no existe.");
                return;
            }

            // Eliminar de la base de datos
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

        // Validar campos obligatorios
        if (fechaDevDatePicker.getValue() == null ||
            comboBoxEntregaDevo.getValue() == null ||
            comboBoxEstadoDevo.getValue() == null ||
            txtDescripcionDevol.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        // Validar que la fecha no sea anterior a hoy
        if (fechaDevDatePicker.getValue().isBefore(fechaActual)) {
            mostrarAlerta("Error", "La fecha de devolución no puede ser anterior a hoy (" + fechaActual + ").");
            return false;
        }

        // Validar que al menos un ComboBox de ID tenga un valor seleccionado
        Integer idPrestamo = comboBoxIdPrestamoDevol.getValue();
        Integer idMantenimiento = comboBoxIdMantenimientoDevol.getValue();
        if (idPrestamo == null && idMantenimiento == null) {
            mostrarAlerta("Error", "Debe seleccionar al menos un ID de préstamo o un ID de mantenimiento.");
            return false;
        }

        // Validar que no se seleccionen ambos IDs a la vez
        if (idPrestamo != null && idMantenimiento != null) {
            mostrarAlerta("Error", "No puede seleccionar un ID de préstamo y un ID de mantenimiento al mismo tiempo.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtIdDevolucion.clear();
        comboBoxEntregaDevo.getSelectionModel().clearSelection();
        fechaDevDatePicker.setValue(null);
        comboBoxEstadoDevo.getSelectionModel().clearSelection();
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