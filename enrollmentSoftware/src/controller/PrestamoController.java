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
    @FXML private TextField txtIdUsuarioPrestamo; // Mapeado a nombre_usuario, ahora TextField
    @FXML private ComboBox<Integer> comboBoxIdAudiovisualPrestamo;
    @FXML private ComboBox<Integer> comboBoxIdSalaPrestamo;
    @FXML private TextField txtEstadoSancion; // Mapeado a detalle_prestamo
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private Button btnRegistrarPrestamo;
    @FXML private Button btnLeerPrestamo;
    @FXML private Button btnActualizarPrestamo;
    @FXML private Button btnBorrarPrestamo;
    @FXML private Button btnMenu;

    private PrestamoDAO prestamoDAO;
    private String usuarioActual; // Simulamos el usuario que inició sesión

    @FXML
    public void initialize() {
        // Inicializar DAO
        prestamoDAO = new PrestamoDAO(DBConnection.getInstance().getConnection());

        // Simulamos el usuario actual (debería venir de un SessionManager)
        usuarioActual = application.SessionManager.getUsuarioActual(); // Obtener usuario
        if (usuarioActual == null) {
            usuarioActual = "Usuario Desconocido"; // Valor por defecto para pruebas
        }
        if (txtIdUsuarioPrestamo != null) { // Verificación de null
            txtIdUsuarioPrestamo.setText(usuarioActual);
        } else {
            System.out.println("Error: txtIdUsuarioPrestamo es null en initialize");
        }

        // Cargar IDs en los ComboBox
        try {
            // IDs de solicitantes
            ObservableList<Integer> solicitanteIds = prestamoDAO.obtenerIdsSolicitantes();
            comboBoxIdSolicitantePrestamo.setItems(solicitanteIds);
            if (!solicitanteIds.isEmpty()) {
                comboBoxIdSolicitantePrestamo.getSelectionModel().selectFirst();
            }

            // IDs de audiovisuales
            ObservableList<Integer> audiovisualIds = prestamoDAO.obtenerIdsAudiovisuales();
            comboBoxIdAudiovisualPrestamo.setItems(audiovisualIds);
            // No seleccionamos automáticamente para permitir que el usuario elija

            // IDs de salas
            ObservableList<Integer> salaIds = prestamoDAO.obtenerIdsSalas();
            comboBoxIdSalaPrestamo.setItems(salaIds);
            // No seleccionamos automáticamente para permitir que el usuario elija
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
                txtIdUsuarioPrestamo.getText(), // Mapeado a nombre_usuario, ahora fijo
                comboBoxIdAudiovisualPrestamo.getValue(),
                comboBoxIdSalaPrestamo.getValue(),
                txtEstadoSancion.getText(), // Mapeado a detalle_prestamo
                txtHoraInicio.getText(),
                txtHoraFin.getText()
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
                txtIdUsuarioPrestamo.getText(), // Mapeado a nombre_usuario, ahora fijo
                comboBoxIdAudiovisualPrestamo.getValue(),
                comboBoxIdSalaPrestamo.getValue(),
                txtEstadoSancion.getText(), // Mapeado a detalle_prestamo
                txtHoraInicio.getText(),
                txtHoraFin.getText()
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
        // Validar campos obligatorios
        if (txtIdPrestamo.getText().isEmpty() ||
            comboBoxIdSolicitantePrestamo.getValue() == null ||
            fechaDatePicker.getValue() == null ||
            txtMontoSancion.getText().isEmpty() || // Mapeado a estado
            txtIdUsuarioPrestamo.getText().isEmpty() || // Mapeado a nombre_usuario
            txtEstadoSancion.getText().isEmpty() || // Mapeado a detalle_prestamo
            txtHoraInicio.getText().isEmpty() || // Nuevo campo
            txtHoraFin.getText().isEmpty()) { // Nuevo campo
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
            return false;
        }

        // Validar que al menos un ComboBox tenga un valor seleccionado
        Integer idAudiovisual = comboBoxIdAudiovisualPrestamo.getValue();
        Integer idSala = comboBoxIdSalaPrestamo.getValue();
        
        if (idAudiovisual == null && idSala == null) {
            mostrarAlerta("Error", "Debe seleccionar al menos un ID de audiovisual o un ID de sala.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        txtIdPrestamo.clear();
        comboBoxIdSolicitantePrestamo.getSelectionModel().selectFirst();
        fechaDatePicker.setValue(null);
        txtMontoSancion.clear(); // Mapeado a estado
        // txtIdUsuarioPrestamo no se limpia, es el usuario actual
        comboBoxIdAudiovisualPrestamo.getSelectionModel().clearSelection();
        comboBoxIdSalaPrestamo.getSelectionModel().clearSelection();
        txtEstadoSancion.clear(); // Mapeado a detalle_prestamo
        txtHoraInicio.clear();
        txtHoraFin.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}