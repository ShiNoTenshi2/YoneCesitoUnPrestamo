package controller;

import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Usuario;
import model.UsuarioSesion;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SolicitudesController {
    @FXML private TableView<Usuario> tablaSolicitudes;
    @FXML private TableColumn<Usuario, Long> columnCedulaUsu;
    @FXML private TableColumn<Usuario, String> columnNombreUsu;
    @FXML private TableColumn<Usuario, String> columnRolUsu;
    @FXML private TableColumn<Usuario, String> columnEstadoUsu;
    @FXML private Button btnConfirmarUsuario;
    @FXML private Button btnDenegarUsuario;
    @FXML private Button btnMenu;

    @FXML
    public void initialize() {
        // Configurar las columnas de la tabla
        columnCedulaUsu.setCellValueFactory(new PropertyValueFactory<>("cedulaUsuario"));
        columnNombreUsu.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        columnRolUsu.setCellValueFactory(new PropertyValueFactory<>("rol"));
        columnEstadoUsu.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Cargar solicitudes al iniciar
        cargarSolicitudes();

        // Verificar que el usuario sea Coordinador
        try {
            if (!UsuarioSesion.getInstance().getRol().equals("Coordinador")) {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo el Coordinador puede aprobar solicitudes.");
                goToMenuInicial();
            }
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No hay una sesión activa.");
            goToMenuInicial();
        }
    }

    private void cargarSolicitudes() {
        try {
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
            List<Usuario> solicitudes = usuarioDAO.obtenerSolicitudes();
            tablaSolicitudes.setItems(FXCollections.observableArrayList(solicitudes));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las solicitudes: " + e.getMessage());
        }
    }

    @FXML
    private void ConfirmarUsuario() {
        Usuario selectedUser = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Seleccione un usuario para confirmar.");
            return;
        }

        try {
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
            if (usuarioDAO.confirmarUsuario(selectedUser.getCedulaUsuario())) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario confirmado exitosamente.");
                cargarSolicitudes(); // Actualizar tabla
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo confirmar el usuario.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al confirmar el usuario: " + e.getMessage());
        }
    }

    @FXML
    private void DenegarUsuario() {
        Usuario selectedUser = tablaSolicitudes.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Seleccione un usuario para denegar.");
            return;
        }

        try {
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
            if (usuarioDAO.denegarUsuario(selectedUser.getCedulaUsuario())) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario denegado exitosamente.");
                cargarSolicitudes(); // Actualizar tabla
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo denegar el usuario.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al denegar el usuario: " + e.getMessage());
        }
    }

    @FXML
    private void GoToMenu() {
        try {
            loadView("/view/MainMenu.fxml", "Menú Coordinador");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar el menú: " + e.getMessage());
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

    private void goToMenuInicial() {
        try {
            loadView("/view/MenuInicial.fxml", "Sistema de Gestión");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar la vista inicial: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}