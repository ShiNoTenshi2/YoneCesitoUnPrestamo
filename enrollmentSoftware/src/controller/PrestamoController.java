package controller;

import data.PrestamoDAO;
import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Prestamo;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class PrestamoController {

    // Elementos de Prestamo.fxml
    @FXML private TextField txtIdPrestamo;
    @FXML private ComboBox<Long> comboBoxCedulaUsuarioPrestamo;
    @FXML private DatePicker fechaDatePicker;
    @FXML private ComboBox<String> comboBoxHoraInicio;
    @FXML private ComboBox<String> comboBoxHoraFin;
    @FXML private TextField txtDetallesPrestamo;
    @FXML private ComboBox<Long> comboBoxIdSalaPrestamo;
    @FXML private ComboBox<Long> comboBoxIdAudiovisualPrestamo;
    @FXML private Button btnRegistrarPrestamo;
    @FXML private Button btnLeerPrestamo;
    @FXML private Button btnActualizarPrestamo;
    @FXML private Button btnBorrarPrestamo;
    @FXML private Button btnMenu;
    @FXML private Button btnLeerSala;
    @FXML private Button btnLeerAudiovisual;

    // Elementos de PrestamoEstado.fxml (separados para claridad)
    @FXML private TableView<Prestamo> tablaPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdUsuarioPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdSalaPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdAudiovPrestamo;
    @FXML private TableColumn<Prestamo, Timestamp> columnHoraInicioPrestamo;
    @FXML private TableColumn<Prestamo, Timestamp> columnIdHoraFinPrestamo;
    @FXML private TableColumn<Prestamo, String> columnEstadoPrestamo;
    @FXML private TableColumn<Prestamo, String> columnDetallePrestamo;
    @FXML private Button btnConfirmarPrestamo;
    @FXML private Button btnDenegarPrestamo;
   
    private String userRol;

    @FXML
    public void initialize() {
        try {
            userRol = UsuarioDAO.getInstance().getRol();
            if (!userRol.equals("Coordinador") && !userRol.equals("Estudiante") && !userRol.equals("Profesor")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Esta vista es solo para Coordinadores, Estudiantes y Profesores.");
                GoToMenu();
                return;
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            GoToMenu();
            return;
        }

        // Inicialización para Prestamo.fxml
        if (tablaPrestamo == null) {
            if (!userRol.equals("Coordinador")) {
                btnActualizarPrestamo.setDisable(true);
                btnBorrarPrestamo.setDisable(true);
                btnLeerPrestamo.setDisable(true);
                txtIdPrestamo.setDisable(true);
            }

            try {
                PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
                List<Long> cedulas = prestamoDAO.obtenerCedulasUsuariosDisponibles();
                if (cedulas.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Sin Datos", "No se encontraron cédulas de usuarios disponibles.");
                } else {
                    comboBoxCedulaUsuarioPrestamo.setItems(FXCollections.observableArrayList(cedulas));
                }
                if (!userRol.equals("Coordinador")) {
                    try {
                        long cedulaUsuario = UsuarioDAO.getInstance().getCedula();
                        comboBoxCedulaUsuarioPrestamo.setValue(cedulaUsuario);
                        comboBoxCedulaUsuarioPrestamo.setDisable(true);
                    } catch (IllegalStateException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "La cédula no está disponible en la sesión.");
                        GoToMenu();
                        return;
                    }
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error de BD", "No se pudieron cargar las cédulas: " + e.getMessage());
            }

            try {
                PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
                List<Long> salas = prestamoDAO.obtenerIdsSalasDisponibles();
                comboBoxIdSalaPrestamo.setItems(FXCollections.observableArrayList(salas));
                List<Long> audiovisuales = prestamoDAO.obtenerIdsAudiovisualesDisponibles();
                comboBoxIdAudiovisualPrestamo.setItems(FXCollections.observableArrayList(audiovisuales));

                if (userRol.equals("Estudiante")) {
                    comboBoxIdSalaPrestamo.setDisable(true);
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los IDs: " + e.getMessage());
            }

            comboBoxHoraInicio.setItems(FXCollections.observableArrayList("6:00 AM", "8:00 AM", "9:45 AM"));
            comboBoxHoraInicio.setEditable(false);
            comboBoxHoraInicio.getSelectionModel().selectFirst();
            comboBoxHoraFin.setItems(FXCollections.observableArrayList("7:30 AM", "9:30 AM", "11:15 AM"));
            comboBoxHoraFin.setEditable(false);
            comboBoxHoraFin.getSelectionModel().selectFirst();
        }
        // Inicialización para PrestamoEstado.fxml
        else {
            inicializarTablaPrestamo();
        }
    }

    private void inicializarTablaPrestamo() {
        columnIdPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_prestamo"));
        columnIdUsuarioPrestamo.setCellValueFactory(new PropertyValueFactory<>("cedula_usuario"));
        columnIdSalaPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
        columnIdAudiovPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_audiovisual"));
        columnHoraInicioPrestamo.setCellValueFactory(new PropertyValueFactory<>("hora_inicio"));
        columnIdHoraFinPrestamo.setCellValueFactory(new PropertyValueFactory<>("hora_fin"));
        columnEstadoPrestamo.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnDetallePrestamo.setCellValueFactory(new PropertyValueFactory<>("detalle_prestamo"));

        try {
            ObservableList<Prestamo> prestamos = FXCollections.observableArrayList(PrestamoDAO.getInstance().obtenerTodos());
            tablaPrestamo.setItems(prestamos);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "No se pudieron cargar los préstamos: " + e.getMessage());
        }

        if (!userRol.equals("Coordinador")) {
            btnConfirmarPrestamo.setDisable(true);
            btnDenegarPrestamo.setDisable(true);
        }
    }

    @FXML
    private void GoToPrestamoGestion() {
        loadView("/view/PrestamoEstado.fxml", "Gestión de Prestamos");
    }

    @FXML
    private void LeerSala() {
        loadView("/view/SalaTabla.fxml", "Tabla de Salas");
    }

    @FXML
    private void LeerAudiovisual() {
        loadView("/view/AudiovisualTabla.fxml", "Tabla de Audiovisuales");
    }

    @FXML
    private void RegistrarPrestamo() {
        try {
            if (!validarCampos(false)) return;

            String detalle = txtDetallesPrestamo.getText().trim();
            if (detalle.length() > 200) {
                showAlert(Alert.AlertType.ERROR, "Error", "Los detalles no pueden exceder 200 caracteres.");
                return;
            }

            LocalDate fechaSolicitud = fechaDatePicker.getValue();
            if (fechaSolicitud == null || fechaSolicitud.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Fecha Inválida", "La fecha debe ser hoy o posterior.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            LocalTime horaInicioTime = LocalTime.parse(comboBoxHoraInicio.getValue(), formatter);
            LocalTime horaFinTime = LocalTime.parse(comboBoxHoraFin.getValue(), formatter);
            LocalDateTime horaInicio = LocalDateTime.of(fechaSolicitud, horaInicioTime);
            LocalDateTime horaFin = LocalDateTime.of(fechaSolicitud, horaFinTime);

            if (!horaInicio.isBefore(horaFin)) {
                showAlert(Alert.AlertType.ERROR, "Horas Inválidas",
                    "La hora de inicio (" + comboBoxHoraInicio.getValue() +
                    ") debe ser anterior a la hora de fin (" + comboBoxHoraFin.getValue() + ").");
                return;
            }

            Prestamo prestamo = new Prestamo(
                fechaSolicitud, detalle, "EnRevision",
                Timestamp.valueOf(horaInicio), Timestamp.valueOf(horaFin),
                comboBoxCedulaUsuarioPrestamo.getValue(),
                comboBoxIdSalaPrestamo.getValue(), comboBoxIdAudiovisualPrestamo.getValue()
            );

            PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
            if (prestamoDAO.registrarPrestamo(prestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo registrado con estado EnRevision.");
                limpiarCampos();
                refrescarComboBoxSalasYAudiovisuales();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "El registro falló (posible duplicación de datos).");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al registrar: " + e.getMessage());
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato",
                "El formato de la hora es inválido. Usa el formato 'h:mm AM/PM' (ejemplo: '8:00 AM'). Detalle: " + e.getMessage());
        }
    }

    @FXML
    private void LeerPrestamo() {
        if (!userRol.equals("Coordinador")) {
            showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo los coordinadores pueden leer préstamos.");
            return;
        }

        try {
            PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
            List<Prestamo> prestamos = prestamoDAO.obtenerTodos();
            if (prestamos.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Información", "No hay préstamos registrados.");
            } else {
                StringBuilder sb = new StringBuilder("=== LISTADO DE PRÉSTAMOS ===\n");
                for (Prestamo p : prestamos) {
                    sb.append(p.toString()).append("\n");
                }
                sb.append("===========================");
                showAlert(Alert.AlertType.INFORMATION, "Préstamos", sb.toString());
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al leer: " + e.getMessage());
        }
    }

    @FXML
    private void ActualizarPrestamo() {
        if (!userRol.equals("Coordinador")) {
            showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo los coordinadores pueden actualizar préstamos.");
            return;
        }

        try {
            if (!validarCampos(true)) return;

            long idPrestamo = Long.parseLong(txtIdPrestamo.getText());
            if (idPrestamo <= 0 || !PrestamoDAO.getInstance().existeId(idPrestamo)) {
                showAlert(Alert.AlertType.ERROR, "ID Inválido", "ID no válido o no encontrado.");
                return;
            }

            String detalle = txtDetallesPrestamo.getText().trim();
            if (detalle.length() > 200) {
                showAlert(Alert.AlertType.ERROR, "Error", "Los detalles no pueden exceder 200 caracteres.");
                return;
            }

            LocalDate fechaSolicitud = fechaDatePicker.getValue();
            if (fechaSolicitud == null || fechaSolicitud.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Fecha Inválida", "La fecha debe ser hoy o posterior.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            LocalTime horaInicioTime = LocalTime.parse(comboBoxHoraInicio.getValue(), formatter);
            LocalTime horaFinTime = LocalTime.parse(comboBoxHoraFin.getValue(), formatter);
            LocalDateTime horaInicio = LocalDateTime.of(fechaSolicitud, horaInicioTime);
            LocalDateTime horaFin = LocalDateTime.of(fechaSolicitud, horaFinTime);

            if (!horaInicio.isBefore(horaFin)) {
                showAlert(Alert.AlertType.ERROR, "Horas Inválidas",
                    "La hora de inicio (" + comboBoxHoraInicio.getValue() +
                    ") debe ser anterior a la hora de fin (" + comboBoxHoraFin.getValue() + ").");
                return;
            }

            Prestamo prestamo = new Prestamo(
                idPrestamo, fechaSolicitud, detalle, "EnRevision",
                Timestamp.valueOf(horaInicio), Timestamp.valueOf(horaFin),
                comboBoxCedulaUsuarioPrestamo.getValue(),
                comboBoxIdSalaPrestamo.getValue(), comboBoxIdAudiovisualPrestamo.getValue()
            );

            if (PrestamoDAO.getInstance().actualizarPrestamo(prestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo actualizado.");
                limpiarCampos();
                refrescarComboBoxSalasYAudiovisuales();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al actualizar: " + e.getMessage());
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato",
                "El formato de la hora es inválido. Usa el formato 'h:mm AM/PM' (ejemplo: '8:00 AM'). Detalle: " + e.getMessage());
        }
    }

    @FXML
    private void BorrarPrestamo() {
        if (!userRol.equals("Coordinador")) {
            showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo los coordinadores pueden borrar préstamos.");
            return;
        }

        try {
            if (txtIdPrestamo.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Campo Vacío", "Ingrese un ID.");
                return;
            }

            long idPrestamo = Long.parseLong(txtIdPrestamo.getText());
            if (idPrestamo <= 0 || !PrestamoDAO.getInstance().existeId(idPrestamo)) {
                showAlert(Alert.AlertType.ERROR, "ID Inválido", "ID no válido o no encontrado.");
                return;
            }

            Prestamo prestamo = PrestamoDAO.getInstance().obtenerPorId(idPrestamo);
            if (prestamo == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No se encontró el préstamo con el ID especificado.");
                return;
            }
            if (!"Finalizado".equals(prestamo.getEstado())) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo se pueden borrar préstamos con estado 'Finalizado'. Estado actual: " + prestamo.getEstado());
                return;
            }

            if (PrestamoDAO.getInstance().tieneDependencias(idPrestamo)) {
                showAlert(Alert.AlertType.ERROR, "Error de Dependencia", "No se puede borrar el préstamo porque tiene registros de devolución asociados.");
                return;
            }

            if (PrestamoDAO.getInstance().eliminarPrestamo(idPrestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo borrado.");
                limpiarCampos();
                refrescarComboBoxSalasYAudiovisuales();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo borrar.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al borrar: " + e.getMessage());
        }
    }

    @FXML
    private void ConfirmarPrestamo() {
        Prestamo prestamoSeleccionado = tablaPrestamo.getSelectionModel().getSelectedItem();
        if (prestamoSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Selección Requerida", "Por favor, selecciona un préstamo de la tabla.");
            return;
        }

        if (!prestamoSeleccionado.getEstado().equals("EnRevision")) {
            showAlert(Alert.AlertType.WARNING, "Estado Inválido", "El préstamo ya ha sido procesado (estado: " + prestamoSeleccionado.getEstado() + ").");
            return;
        }

        try {
            if (PrestamoDAO.getInstance().actualizarEstado(prestamoSeleccionado.getId_prestamo(), "Aprobado")) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo confirmado (estado: Aprobado).");
                tablaPrestamo.setItems(FXCollections.observableArrayList(PrestamoDAO.getInstance().obtenerTodos()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo confirmar el préstamo.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al confirmar el préstamo: " + e.getMessage());
        }
    }

    @FXML
    private void DenegarPrestamo() {
        Prestamo prestamoSeleccionado = tablaPrestamo.getSelectionModel().getSelectedItem();
        if (prestamoSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Selección Requerida", "Por favor, selecciona un préstamo de la tabla.");
            return;
        }

        if (!prestamoSeleccionado.getEstado().equals("EnRevision")) {
            showAlert(Alert.AlertType.WARNING, "Estado Inválido", "El préstamo ya ha sido procesado (estado: " + prestamoSeleccionado.getEstado() + ").");
            return;
        }

        long idPrestamo = prestamoSeleccionado.getId_prestamo();
        try {
            if (!PrestamoDAO.getInstance().existeId(idPrestamo)) {
                showAlert(Alert.AlertType.ERROR, "Error", "El préstamo con ID " + idPrestamo + " no existe en la base de datos.");
                return;
            }

            if (PrestamoDAO.getInstance().eliminarPrestamo(idPrestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo denegado y eliminado de la base de datos.");
                tablaPrestamo.setItems(FXCollections.observableArrayList(PrestamoDAO.getInstance().obtenerTodos()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el préstamo con ID " + idPrestamo + ".");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "Error al denegar el préstamo: " + e.getMessage() + " (ID: " + idPrestamo + ")");
        }
    }

    @FXML
    private void GoToMenu() {
        loadView(userRol.equals("Coordinador") ? "/view/MainMenu.fxml" : "/view/MainMenuProfeEstud.fxml",
                 userRol.equals("Coordinador") ? "Menú Coordinador" : "Menú Estudiante/Profesor");
    }

    private boolean validarCampos(boolean requiereId) {
        if ((requiereId && txtIdPrestamo.getText().isEmpty()) || 
            comboBoxCedulaUsuarioPrestamo.getValue() == null ||
            fechaDatePicker.getValue() == null || 
            comboBoxHoraInicio.getValue() == null ||
            comboBoxHoraFin.getValue() == null || 
            txtDetallesPrestamo.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos obligatorios deben estar llenos.");
            return false;
        }

        if (userRol.equals("Estudiante")) {
            if (comboBoxIdSalaPrestamo.getValue() != null) {
                showAlert(Alert.AlertType.ERROR, "Selección No Permitida", "Los estudiantes no pueden solicitar salas, solo audiovisuales.");
                return false;
            }
            if (comboBoxIdAudiovisualPrestamo.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Selección Requerida", "Los estudiantes deben seleccionar un audiovisual.");
                return false;
            }
        } else {
            if (comboBoxIdSalaPrestamo.getValue() == null && comboBoxIdAudiovisualPrestamo.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Selección Requerida", "Debe seleccionar al menos una sala o un audiovisual.");
                return false;
            }
        }
        return true;
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtIdPrestamo.clear();
        if (!userRol.equals("Coordinador")) {
            try {
                comboBoxCedulaUsuarioPrestamo.setValue(UsuarioDAO.getInstance().getCedula());
            } catch (IllegalStateException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "La cédula no está disponible en la sesión.");
                GoToMenu();
                return;
            }
        } else {
            comboBoxCedulaUsuarioPrestamo.getSelectionModel().clearSelection();
        }
        fechaDatePicker.setValue(null);
        comboBoxHoraInicio.getSelectionModel().selectFirst();
        comboBoxHoraFin.getSelectionModel().selectFirst();
        txtDetallesPrestamo.clear();
        comboBoxIdSalaPrestamo.getSelectionModel().clearSelection();
        comboBoxIdAudiovisualPrestamo.getSelectionModel().clearSelection();
    }

    private void refrescarComboBoxSalasYAudiovisuales() {
        try {
            PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
            List<Long> salas = prestamoDAO.obtenerIdsSalasDisponibles();
            comboBoxIdSalaPrestamo.setItems(FXCollections.observableArrayList(salas));
            List<Long> audiovisuales = prestamoDAO.obtenerIdsAudiovisualesDisponibles();
            comboBoxIdAudiovisualPrestamo.setItems(FXCollections.observableArrayList(audiovisuales));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron refrescar los IDs: " + e.getMessage());
        }
    }
}