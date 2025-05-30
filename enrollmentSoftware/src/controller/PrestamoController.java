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

    // Elementos de Prestamo.fxml (ya existentes)
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

    // Elementos de PrestamoEstado.fxml (actualizados con nuevas columnas)
    @FXML private TableView<Prestamo> tablaPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdUsuarioPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdSalaPrestamo;
    @FXML private TableColumn<Prestamo, Long> columnIdAudiovPrestamo;
    @FXML private TableColumn<Prestamo, Timestamp> columnHoraInicioPrestamo;
    @FXML private TableColumn<Prestamo, Timestamp> columnIdHoraFinPrestamo;
    @FXML private TableColumn<Prestamo, LocalDate> columnFechaPrestamo;
    @FXML private TableColumn<Prestamo, String> columnEstadoPrestamo;
    @FXML private TableColumn<Prestamo, String> columnDetallePrestamo;
    @FXML private Button btnConfirmarPrestamo;
    @FXML private Button btnDenegarPrestamo;

    private String userRol;

    @FXML
    public void initialize() {
        // Verificar rol del usuario
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

        // Si estamos en PrestamoEstado.fxml (tablaPrestamo no es null), inicializamos la tabla
        if (tablaPrestamo != null) {
            inicializarTablaPrestamo();
            return; // Salimos para no ejecutar la inicialización de Prestamo.fxml
        }

        // Inicialización para Prestamo.fxml
        // Deshabilitar botones para Estudiantes y Profesores
        if (!userRol.equals("Coordinador")) {
            btnActualizarPrestamo.setDisable(true);
            btnBorrarPrestamo.setDisable(true);
            btnLeerPrestamo.setDisable(true); // Deshabilitar "Leer" para Estudiante y Profesor
        }

        // Inicializar ComboBox de cédulas
        try {
            PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
            List<Long> cedulas = prestamoDAO.obtenerCedulasUsuariosDisponibles();
            if (cedulas.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Sin Datos", "No se encontraron cédulas de usuarios disponibles (estado 'Aprobado'). Verifique la base de datos.");
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

        // Inicializar ComboBox de IDs de salas y audiovisuales (solo disponibles)
        try {
            PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
            List<Long> salas = prestamoDAO.obtenerIdsSalasDisponibles();
            comboBoxIdSalaPrestamo.setItems(FXCollections.observableArrayList(salas));
            List<Long> audiovisuales = prestamoDAO.obtenerIdsAudiovisualesDisponibles();
            comboBoxIdAudiovisualPrestamo.setItems(FXCollections.observableArrayList(audiovisuales));

            // Deshabilitar ComboBox de salas para Estudiantes
            if (userRol.equals("Estudiante")) {
                comboBoxIdSalaPrestamo.setDisable(true);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los IDs: " + e.getMessage());
        }

        // Inicializar ComboBox de horas con espacio
        comboBoxHoraInicio.setItems(FXCollections.observableArrayList("6:00 AM", "8:00 AM", "9:45 AM"));
        comboBoxHoraInicio.setEditable(false);
        comboBoxHoraInicio.getSelectionModel().selectFirst();
        comboBoxHoraFin.setItems(FXCollections.observableArrayList("7:30 AM", "9:30 AM", "11:15 AM"));
        comboBoxHoraFin.setEditable(false);
        comboBoxHoraFin.getSelectionModel().selectFirst();
    }

    private void inicializarTablaPrestamo() {
        // Configurar las columnas de la tabla con los nombres correctos de las propiedades
        columnIdPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_prestamo"));
        columnIdUsuarioPrestamo.setCellValueFactory(new PropertyValueFactory<>("cedula_usuario"));
        columnIdSalaPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
        columnIdAudiovPrestamo.setCellValueFactory(new PropertyValueFactory<>("id_audiovisual"));
        columnHoraInicioPrestamo.setCellValueFactory(new PropertyValueFactory<>("hora_inicio"));
        columnIdHoraFinPrestamo.setCellValueFactory(new PropertyValueFactory<>("hora_fin"));
        columnEstadoPrestamo.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnDetallePrestamo.setCellValueFactory(new PropertyValueFactory<>("detalle_prestamo"));

        // Cargar todos los préstamos (EnRevision y Aprobado)
        try {
            ObservableList<Prestamo> prestamos = FXCollections.observableArrayList(PrestamoDAO.getInstance().obtenerTodos());
            tablaPrestamo.setItems(prestamos);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de BD", "No se pudieron cargar los préstamos: " + e.getMessage());
        }

        // Deshabilitar botones para usuarios no coordinadores
        if (!userRol.equals("Coordinador")) {
            btnConfirmarPrestamo.setDisable(true);
            btnDenegarPrestamo.setDisable(true);
        }
    }

    @FXML
    private void RegistrarPrestamo() {
        try {
            if (!validarCampos()) return;

            long idPrestamo = Long.parseLong(txtIdPrestamo.getText());
            if (idPrestamo <= 0) {
                showAlert(Alert.AlertType.ERROR, "ID Inválido", "El ID debe ser un número positivo.");
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

            PrestamoDAO prestamoDAO = PrestamoDAO.getInstance();
            if (prestamoDAO.registrarPrestamo(prestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo registrado con estado EnRevision.");
                limpiarCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "El ID ya está registrado.");
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
        // Solo los coordinadores pueden leer préstamos
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
        // Solo los coordinadores pueden actualizar préstamos
        if (!userRol.equals("Coordinador")) {
            showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo los coordinadores pueden actualizar préstamos.");
            return;
        }

        try {
            if (!validarCampos()) return;

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
        // Solo los coordinadores pueden borrar préstamos
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

            if (PrestamoDAO.getInstance().eliminarPrestamo(idPrestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo borrado.");
                limpiarCampos();
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
                // Refrescar la tabla con todos los préstamos
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
            // Verificar si el ID existe antes de intentar eliminar
            if (!PrestamoDAO.getInstance().existeId(idPrestamo)) {
                showAlert(Alert.AlertType.ERROR, "Error", "El préstamo con ID " + idPrestamo + " no existe en la base de datos.");
                return;
            }

            if (PrestamoDAO.getInstance().eliminarPrestamo(idPrestamo)) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo denegado y eliminado de la base de datos.");
                // Refrescar la tabla con todos los préstamos
                tablaPrestamo.setItems(FXCollections.observableArrayList(PrestamoDAO.getInstance().obtenerTodos()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el préstamo con ID " + idPrestamo + ". Verifique los datos o restricciones en la base de datos.");
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

    private boolean validarCampos() {
        // Validar campos obligatorios básicos
        if (txtIdPrestamo.getText().isEmpty() || comboBoxCedulaUsuarioPrestamo.getValue() == null ||
            fechaDatePicker.getValue() == null || comboBoxHoraInicio.getValue() == null ||
            comboBoxHoraFin.getValue() == null || txtDetallesPrestamo.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Todos los campos obligatorios deben estar llenos.");
            return false;
        }

        // Validaciones específicas para Estudiante
        if (userRol.equals("Estudiante")) {
            // Estudiantes solo pueden solicitar audiovisuales, no salas
            if (comboBoxIdSalaPrestamo.getValue() != null) {
                showAlert(Alert.AlertType.ERROR, "Selección No Permitida", "Los estudiantes no pueden solicitar salas, solo audiovisuales.");
                return false;
            }
            if (comboBoxIdAudiovisualPrestamo.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Selección Requerida", "Los estudiantes deben seleccionar un audiovisual.");
                return false;
            }
        } else {
            // Para Coordinador y Profesor: deben seleccionar al menos una sala o un audiovisual
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
}