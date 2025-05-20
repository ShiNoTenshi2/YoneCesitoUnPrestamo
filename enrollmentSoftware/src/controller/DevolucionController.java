package controller;

import data.DBConnection;
import data.DevolucionDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import application.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Devolucion;

public class DevolucionController {

    @FXML private TextField txtIdDevolucion;
    @FXML private DatePicker fechaDevDatePicker;
    @FXML private TextField txtEstadoDevolucion;
    @FXML private ComboBox<Integer> comboBoxIdPrestamoDevolucion;
    @FXML private Button btnRegistrarDevolucion;
    @FXML private Button btnLeerDevolucion;
    @FXML private Button btnActualizarDevolucion;
    @FXML private Button btnBorrarDevolucion;
    @FXML private Button btnMenu;

    private DevolucionDAO devolucionDAO;

    @FXML
    public void initialize() {
        // Inicializar DAO
        devolucionDAO = new DevolucionDAO(DBConnection.getInstance().getConnection());

        // Cargar IDs de préstamos en el ComboBox
        try {
            ObservableList<Integer> prestamoIds = devolucionDAO.obtenerIdsPrestamos();
            comboBoxIdPrestamoDevolucion.setItems(prestamoIds);
            if (!prestamoIds.isEmpty()) {
                comboBoxIdPrestamoDevolucion.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los IDs de préstamos: " + e.getMessage());
        }
    }

    @FXML
    public void RegistrarDevolucion() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear nueva devolución
            Devolucion devolucion = new Devolucion(
                0, // ID no se usa, la secuencia lo genera
                fechaDevDatePicker.getValue(),
                txtEstadoDevolucion.getText(),
                comboBoxIdPrestamoDevolucion.getValue()
            );

            // Guardar en la base de datos
            devolucionDAO.guardar(devolucion);
            mostrarAlerta("Éxito", "Devolución registrada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar la devolución: " + e.getMessage());
        }
    }

    @FXML
    public void LeerDevolucion() {
        try {
            // Obtener todas las devoluciones
            ObservableList<Devolucion> devoluciones = devolucionDAO.obtenerTodas();

            if (devoluciones.isEmpty()) {
                mostrarAlerta("Información", "No hay devoluciones registradas.");
                return;
            }

            // Mostrar cada devolución en la consola
            System.out.println("Lista de Devoluciones:");
            for (Devolucion devolucion : devoluciones) {
                System.out.println(devolucion.toString());
            }

            mostrarAlerta("Éxito", "Devoluciones mostradas en la consola.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer las devoluciones: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarDevolucion() {
        try {
            // Validar ID
            String idText = txtIdDevolucion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la devolución.");
                return;
            }
            int idDevolucion;
            try {
                idDevolucion = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID de devolución debe ser numérico.");
                return;
            }

            // Validar otros campos
            if (!validarCampos()) {
                return;
            }

            // Verificar si el ID existe
            if (!devolucionDAO.existeId(idDevolucion)) {
                mostrarAlerta("Error", "El ID de devolución no existe.");
                return;
            }

            // Crear devolución actualizada
            Devolucion devolucion = new Devolucion(
                idDevolucion,
                fechaDevDatePicker.getValue(),
                txtEstadoDevolucion.getText(),
                comboBoxIdPrestamoDevolucion.getValue()
            );

            // Actualizar en la base de datos
            devolucionDAO.actualizar(devolucion);
            mostrarAlerta("Éxito", "Devolución actualizada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar la devolución: " + e.getMessage());
        }
    }

    @FXML
    public void BorrarDevolucion() {
        try {
            // Validar ID
            String idText = txtIdDevolucion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la devolución.");
                return;
            }

            int idDevolucion;
            try {
                idDevolucion = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID de devolución debe ser numérico.");
                return;
            }

            // Verificar si el ID existe
            if (!devolucionDAO.existeId(idDevolucion)) {
                mostrarAlerta("Error", "El ID de devolución no existe.");
                return;
            }

            // Eliminar de la base de datos
            devolucionDAO.eliminar(idDevolucion);
            mostrarAlerta("Éxito", "Devolución borrada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar la devolución: " + e.getMessage());
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCampos() {
        // Validar campos obligatorios (excluimos txtIdDevolucion para registro)
        if (fechaDevDatePicker.getValue() == null ||
            txtEstadoDevolucion.getText().isEmpty() ||
            comboBoxIdPrestamoDevolucion.getValue() == null) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtIdDevolucion.clear();
        fechaDevDatePicker.setValue(null);
        txtEstadoDevolucion.clear();
        comboBoxIdPrestamoDevolucion.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}