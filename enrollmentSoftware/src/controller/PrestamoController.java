package controller;

import data.DBConnection;
import data.PrestamoDAO;
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
import model.Prestamo;

public class PrestamoController {

    @FXML private TextField txtIdPrestamo;
    @FXML private ComboBox<Integer> comboBoxIdSolicitantePrestamo;
    @FXML private DatePicker fechaDatePicker;
    @FXML private TextField txtMontoSancion; // Mapeado a estado
    @FXML private ComboBox<String> comboBoxIdUsuarioPrestamo; // Mapeado a nombre_usuario
    @FXML private ComboBox<Integer> comboBoxIdAudiovisualPrestamo;
    @FXML private ComboBox<Integer> comboBoxIdSalaPrestamo;
    @FXML private TextField txtEstadoSancion; // Mapeado a detalle_prestamo
    @FXML private TextField txtHoraInicio; // Nuevo campo
    @FXML private TextField txtHoraFin; // Nuevo campo
    @FXML private Button btnRegistrarPrestamo;
    @FXML private Button btnLeerPrestamo;
    @FXML private Button btnActualizarPrestamo;
    @FXML private Button btnBorrarPrestamo;
    @FXML private Button btnMenu;

    private PrestamoDAO prestamoDAO;

    @FXML
    public void initialize() {
        // Inicializar DAO
        prestamoDAO = new PrestamoDAO(DBConnection.getInstance().getConnection());

        // Cargar IDs en los ComboBox
        try {
            // IDs de solicitantes
            ObservableList<Integer> solicitanteIds = prestamoDAO.obtenerIdsSolicitantes();
            comboBoxIdSolicitantePrestamo.setItems(solicitanteIds);
            if (!solicitanteIds.isEmpty()) {
                comboBoxIdSolicitantePrestamo.getSelectionModel().selectFirst();
            }

            // Nombres de usuarios (como cadenas)
            ObservableList<String> usuarioNombres = prestamoDAO.obtenerNombresUsuarios();
            comboBoxIdUsuarioPrestamo.setItems(usuarioNombres);
            if (!usuarioNombres.isEmpty()) {
                comboBoxIdUsuarioPrestamo.getSelectionModel().selectFirst();
            }

            // IDs de audiovisuales
            ObservableList<Integer> audiovisualIds = prestamoDAO.obtenerIdsAudiovisuales();
            comboBoxIdAudiovisualPrestamo.setItems(audiovisualIds);
            // No seleccionamos un valor por defecto para hacerlo opcional

            // IDs de salas
            ObservableList<Integer> salaIds = prestamoDAO.obtenerIdsSalas();
            comboBoxIdSalaPrestamo.setItems(salaIds);
            // No seleccionamos un valor por defecto para hacerlo opcional
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    public void RegistrarPrestamo() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear nuevo préstamo
            Prestamo prestamo = new Prestamo(
                Integer.parseInt(txtIdPrestamo.getText()),
                comboBoxIdSolicitantePrestamo.getValue(),
                fechaDatePicker.getValue(),
                txtMontoSancion.getText(), // Mapeado a estado
                comboBoxIdUsuarioPrestamo.getValue(), // Mapeado a nombre_usuario
                comboBoxIdAudiovisualPrestamo.getValue(), // Puede ser null
                comboBoxIdSalaPrestamo.getValue(), // Puede ser null
                txtEstadoSancion.getText(), // Mapeado a detalle_prestamo
                txtHoraInicio.getText(), // Nuevo campo
                txtHoraFin.getText() // Nuevo campo
            );

            // Verificar si el ID ya existe
            if (prestamoDAO.existeId(prestamo.getId_prestamo())) {
                mostrarAlerta("Error", "El ID de préstamo ya existe.");
                return;
            }

            // Guardar en la base de datos
            prestamoDAO.guardar(prestamo);
            mostrarAlerta("Éxito", "Préstamo registrado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar el préstamo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de préstamo debe ser numérico.");
        }
    }

    @FXML
    public void LeerPrestamo() {
        try {
            // Obtener todos los préstamos
            ObservableList<Prestamo> prestamos = prestamoDAO.obtenerTodas();

            if (prestamos.isEmpty()) {
                mostrarAlerta("Información", "No hay préstamos registrados.");
                return;
            }

            // Mostrar cada préstamo en la consola
            System.out.println("Lista de Préstamos:");
            for (Prestamo prestamo : prestamos) {
                System.out.println(prestamo.toString());
            }

            mostrarAlerta("Éxito", "Préstamos mostrados en la consola.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer los préstamos: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarPrestamo() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear préstamo actualizado
            Prestamo prestamo = new Prestamo(
                Integer.parseInt(txtIdPrestamo.getText()),
                comboBoxIdSolicitantePrestamo.getValue(),
                fechaDatePicker.getValue(),
                txtMontoSancion.getText(), // Mapeado a estado
                comboBoxIdUsuarioPrestamo.getValue(), // Mapeado a nombre_usuario
                comboBoxIdAudiovisualPrestamo.getValue(), // Puede ser null
                comboBoxIdSalaPrestamo.getValue(), // Puede ser null
                txtEstadoSancion.getText(), // Mapeado a detalle_prestamo
                txtHoraInicio.getText(), // Nuevo campo
                txtHoraFin.getText() // Nuevo campo
            );

            // Verificar si el ID existe
            if (!prestamoDAO.existeId(prestamo.getId_prestamo())) {
                mostrarAlerta("Error", "El ID de préstamo no existe.");
                return;
            }

            // Actualizar en la base de datos
            prestamoDAO.actualizar(prestamo);
            mostrarAlerta("Éxito", "Préstamo actualizado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar el préstamo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de préstamo debe ser numérico.");
        }
    }

    @FXML
    public void BorrarPrestamo() {
        try {
            // Validar ID
            String idText = txtIdPrestamo.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del préstamo.");
                return;
            }

            int idPrestamo = Integer.parseInt(idText);

            // Verificar si el ID existe
            if (!prestamoDAO.existeId(idPrestamo)) {
                mostrarAlerta("Error", "El ID de préstamo no existe.");
                return;
            }

            // Eliminar de la base de datos
            prestamoDAO.eliminar(idPrestamo);
            mostrarAlerta("Éxito", "Préstamo eliminado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar el préstamo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de préstamo debe ser numérico.");
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCampos() {
        if (txtIdPrestamo.getText().isEmpty() ||
            comboBoxIdSolicitantePrestamo.getValue() == null ||
            fechaDatePicker.getValue() == null ||
            txtMontoSancion.getText().isEmpty() || // Mapeado a estado
            comboBoxIdUsuarioPrestamo.getValue() == null ||
            txtEstadoSancion.getText().isEmpty() || // Mapeado a detalle_prestamo
            txtHoraInicio.getText().isEmpty() || // Nuevo campo
            txtHoraFin.getText().isEmpty()) { // Nuevo campo
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtIdPrestamo.clear();
        comboBoxIdSolicitantePrestamo.getSelectionModel().selectFirst();
        fechaDatePicker.setValue(null);
        txtMontoSancion.clear(); // Mapeado a estado
        comboBoxIdUsuarioPrestamo.getSelectionModel().selectFirst();
        comboBoxIdAudiovisualPrestamo.getSelectionModel().clearSelection(); // Opcional
        comboBoxIdSalaPrestamo.getSelectionModel().clearSelection(); // Opcional
        txtEstadoSancion.clear(); // Mapeado a detalle_prestamo
        txtHoraInicio.clear(); // Nuevo campo
        txtHoraFin.clear(); // Nuevo campo
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}