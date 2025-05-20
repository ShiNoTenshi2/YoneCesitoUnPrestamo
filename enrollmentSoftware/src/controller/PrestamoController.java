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
    @FXML private TextField txtIdUsuarioPrestamo; // Mapeado a nombre_usuario
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
        if (txtIdUsuarioPrestamo != null) {
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

            // IDs de salas
            ObservableList<Integer> salaIds = prestamoDAO.obtenerIdsSalas();
            comboBoxIdSalaPrestamo.setItems(salaIds);
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
                0, // ID no se usa, la secuencia lo genera
                comboBoxIdSolicitantePrestamo.getValue(),
                fechaDatePicker.getValue(),
                txtMontoSancion.getText(),
                txtIdUsuarioPrestamo.getText(),
                comboBoxIdAudiovisualPrestamo.getValue(),
                comboBoxIdSalaPrestamo.getValue(),
                txtEstadoSancion.getText(),
                txtHoraInicio.getText(),
                txtHoraFin.getText()
            );

            // Guardar en la base de datos
            prestamoDAO.guardar(prestamo);
            mostrarAlerta("Éxito", "Préstamo registrado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar el préstamo: " + e.getMessage());
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
            // Validar ID
            String idText = txtIdPrestamo.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del préstamo.");
                return;
            }
            int idPrestamo;
            try {
                idPrestamo = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID de préstamo debe ser numérico.");
                return;
            }

            // Validar otros campos
            if (!validarCampos()) {
                return;
            }

            // Verificar si el ID existe
            if (!prestamoDAO.existeId(idPrestamo)) {
                mostrarAlerta("Error", "El ID de préstamo no existe.");
                return;
            }

            // Crear préstamo actualizado
            Prestamo prestamo = new Prestamo(
                idPrestamo,
                comboBoxIdSolicitantePrestamo.getValue(),
                fechaDatePicker.getValue(),
                txtMontoSancion.getText(),
                txtIdUsuarioPrestamo.getText(),
                comboBoxIdAudiovisualPrestamo.getValue(),
                comboBoxIdSalaPrestamo.getValue(),
                txtEstadoSancion.getText(),
                txtHoraInicio.getText(),
                txtHoraFin.getText()
            );

            // Actualizar en la base de datos
            prestamoDAO.actualizar(prestamo);
            mostrarAlerta("Éxito", "Préstamo actualizado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar el préstamo: " + e.getMessage());
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

            int idPrestamo;
            try {
                idPrestamo = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID de préstamo debe ser numérico.");
                return;
            }

            // Verificar si el ID existe
            if (!prestamoDAO.existeId(idPrestamo)) {
                mostrarAlerta("Error", "El ID de préstamo no existe.");
                return;
            }

            // Eliminar de la base de datos
            prestamoDAO.eliminar(idPrestamo);
            mostrarAlerta("Éxito", "Préstamo borrado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar el préstamo: " + e.getMessage());
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCampos() {
        // Validar campos obligatorios (excluimos txtIdPrestamo para registro)
        if (comboBoxIdSolicitantePrestamo.getValue() == null ||
            fechaDatePicker.getValue() == null ||
            txtMontoSancion.getText().isEmpty() ||
            txtIdUsuarioPrestamo.getText().isEmpty() ||
            txtEstadoSancion.getText().isEmpty() ||
            txtHoraInicio.getText().isEmpty() ||
            txtHoraFin.getText().isEmpty()) {
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
        txtMontoSancion.clear();
        // txtIdUsuarioPrestamo no se limpia, es el usuario actual
        comboBoxIdAudiovisualPrestamo.getSelectionModel().clearSelection();
        comboBoxIdSalaPrestamo.getSelectionModel().clearSelection();
        txtEstadoSancion.clear();
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