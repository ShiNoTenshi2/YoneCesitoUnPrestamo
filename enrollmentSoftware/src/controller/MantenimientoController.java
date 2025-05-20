package controller;

import data.DBConnection;
import data.MantenimientoDAO;
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
import model.Mantenimiento;

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
        // Inicializar DAO
        mantenimientoDAO = new MantenimientoDAO(DBConnection.getInstance().getConnection());

        // Cargar IDs de salas en el ComboBox
        try {
            ObservableList<Integer> salaIds = mantenimientoDAO.obtenerIdsSalas();
            comboBoxIdSalaMantenimiento.setItems(salaIds);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los IDs de salas: " + e.getMessage());
        }

        // Cargar IDs de audiovisuales en el ComboBox
        try {
            ObservableList<Integer> audiovisualIds = mantenimientoDAO.obtenerIdsAudiovisuales();
            comboBoxIdAudiovisualMantenimiento.setItems(audiovisualIds);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los IDs de audiovisuales: " + e.getMessage());
        }
    }

    @FXML
    public void RegistrarMantenimiento() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear nuevo mantenimiento
            Mantenimiento mantenimiento = new Mantenimiento(
                0, // ID no se usa, la secuencia lo genera
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
        try {
            // Obtener todos los mantenimientos
            ObservableList<Mantenimiento> mantenimientos = mantenimientoDAO.obtenerTodas();

            if (mantenimientos.isEmpty()) {
                mostrarAlerta("Información", "No hay mantenimientos registrados.");
                return;
            }

            // Mostrar cada mantenimiento en la consola
            System.out.println("Lista de Mantenimientos:");
            for (Mantenimiento mantenimiento : mantenimientos) {
                System.out.println(mantenimiento.toString());
            }

            mostrarAlerta("Éxito", "Mantenimientos mostrados en la consola.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer los mantenimientos: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarMantenimiento() {
        try {
            // Validar ID
            String idText = txtIdMantenimiento.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del mantenimiento.");
                return;
            }
            int idMantenimiento;
            try {
                idMantenimiento = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID de mantenimiento debe ser numérico.");
                return;
            }

            // Validar otros campos
            if (!validarCampos()) {
                return;
            }

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
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar el mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    public void BorrarMantenimiento() {
        try {
            // Validar ID
            String idText = txtIdMantenimiento.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID del mantenimiento.");
                return;
            }

            int idMantenimiento;
            try {
                idMantenimiento = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID de mantenimiento debe ser numérico.");
                return;
            }

            // Verificar si el ID existe
            if (!mantenimientoDAO.existeId(idMantenimiento)) {
                mostrarAlerta("Error", "El ID de mantenimiento no existe.");
                return;
            }

            // Eliminar de la base de datos
            mantenimientoDAO.eliminar(idMantenimiento);
            mostrarAlerta("Éxito", "Mantenimiento borrado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar el mantenimiento: " + e.getMessage());
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCampos() {
        // Validar campos obligatorios (excluimos txtIdMantenimiento para registro)
        if (fechaMantDatePicker.getValue() == null ||
            txtDescripcionMantenimiento.getText().isEmpty() ||
            txtResponsableMantenimiento.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos obligatorios.");
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