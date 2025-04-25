package controller;

import data.DBConnection;
import data.SancionDAO;
import data.SolicitantesDAO;
import java.io.IOException;
import java.sql.SQLException;

import application.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Sancion;

public class SancionController {

    @FXML private TextField txtIdSancion;
    @FXML private ComboBox<Integer> comboBoxIdSolicitanteSancion;
    @FXML private TextField txtMotivoSancion;
    @FXML private TextField txtMontoSancion;
    @FXML private TextField txtEstadoSancion;
    @FXML private Button btnRegistrarSancion;
    @FXML private Button btnLeerSancion;
    @FXML private Button btnActualizarSancion;
    @FXML private Button btnBorrarSancion;
    @FXML private Button btnMenu;

    private SancionDAO sancionDAO;
    private SolicitantesDAO solicitantesDAO;

    @FXML
    public void initialize() {
        // Inicializar DAOs
        sancionDAO = new SancionDAO(DBConnection.getInstance().getConnection());
        solicitantesDAO = new SolicitantesDAO(DBConnection.getInstance().getConnection());

        // Cargar IDs de solicitantes en el ComboBox
        try {
            ObservableList<Integer> solicitanteIds = solicitantesDAO.obtenerTodosIds();
            comboBoxIdSolicitanteSancion.setItems(solicitanteIds);
            if (!solicitanteIds.isEmpty()) {
                comboBoxIdSolicitanteSancion.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los IDs de solicitantes: " + e.getMessage());
        }
    }

    @FXML
    public void RegistrarSancion() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear nueva sanción
            Sancion sancion = new Sancion(
                Integer.parseInt(txtIdSancion.getText()),
                comboBoxIdSolicitanteSancion.getValue(),
                txtMotivoSancion.getText(),
                Integer.parseInt(txtMontoSancion.getText()),
                txtEstadoSancion.getText()
            );

            // Verificar si el ID ya existe
            if (sancionDAO.existeId(sancion.getid_sancion())) {
                mostrarAlerta("Error", "El ID de sanción ya existe.");
                return;
            }

            // Guardar en la base de datos
            sancionDAO.guardar(sancion);
            mostrarAlerta("Éxito", "Sanción registrada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al registrar la sanción: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de sanción y el monto deben ser numéricos.");
        }
    }

    @FXML
    public void LeerSancion() {
        try {
            // Obtener todas las sanciones
            ObservableList<Sancion> sanciones = sancionDAO.obtenerTodas();

            if (sanciones.isEmpty()) {
                mostrarAlerta("Información", "No hay sanciones registradas.");
                return;
            }

            // Mostrar cada sanción en la consola
            System.out.println("Lista de Sanciones:");
            for (Sancion sancion : sanciones) {
                System.out.println(sancion.toString());
            }

            mostrarAlerta("Éxito", "Sanciones mostradas en la consola.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al leer las sanciones: " + e.getMessage());
        }
    }

    @FXML
    public void ActualizarSancion() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }

            // Crear sanción actualizada
            Sancion sancion = new Sancion(
                Integer.parseInt(txtIdSancion.getText()),
                comboBoxIdSolicitanteSancion.getValue(),
                txtMotivoSancion.getText(),
                Integer.parseInt(txtMontoSancion.getText()),
                txtEstadoSancion.getText()
            );

            // Verificar si el ID existe
            if (!sancionDAO.existeId(sancion.getid_sancion())) {
                mostrarAlerta("Error", "El ID de sanción no existe.");
                return;
            }

            // Actualizar en la base de datos
            sancionDAO.actualizar(sancion);
            mostrarAlerta("Éxito", "Sanción actualizada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar la sanción: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de sanción y el monto deben ser numéricos.");
        }
    }

    @FXML
    public void BorrarSancion() {
        try {
            // Validar ID
            String idText = txtIdSancion.getText();
            if (idText.isEmpty()) {
                mostrarAlerta("Error", "Por favor, ingrese el ID de la sanción.");
                return;
            }

            int idSancion = Integer.parseInt(idText);

            // Verificar si el ID existe
            if (!sancionDAO.existeId(idSancion)) {
                mostrarAlerta("Error", "El ID de sanción no existe.");
                return;
            }

            // Eliminar de la base de datos
            sancionDAO.eliminar(idSancion);
            mostrarAlerta("Éxito", "Sanción eliminada correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al eliminar la sanción: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID de sanción debe ser numérico.");
        }
    }

    @FXML
    void GoToMenu(ActionEvent event) {
        Main.loadScene("/view/MainMenu.fxml");
    }

    private boolean validarCampos() {
        if (txtIdSancion.getText().isEmpty() ||
            comboBoxIdSolicitanteSancion.getValue() == null ||
            txtMotivoSancion.getText().isEmpty() ||
            txtMontoSancion.getText().isEmpty() ||
            txtEstadoSancion.getText().isEmpty()) {
            mostrarAlerta("Error", "Por favor, complete todos los campos.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtIdSancion.clear();
        comboBoxIdSolicitanteSancion.getSelectionModel().selectFirst();
        txtMotivoSancion.clear();
        txtMontoSancion.clear();
        txtEstadoSancion.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}