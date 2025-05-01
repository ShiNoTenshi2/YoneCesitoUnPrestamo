package controller;

import application.Main;
import data.MenuUsuariosDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

public class MenuUsuariosController {

    @FXML
    private Button btnIngreso;

    @FXML
    private Button btnRegistro;

    @FXML
    private PasswordField txtContraseñaU;

    @FXML
    private TextField txtNombreU;

    private MenuUsuariosDAO userDAO = MenuUsuariosDAO.getInstance();

    @FXML
    public void initialize() {
        // Inicialización si es necesaria
    }

    @FXML
    void IngresoUsuario(ActionEvent event) {
        String nombre = txtNombreU.getText().trim();
        String contrasena = txtContraseñaU.getText().trim();

        if (nombre.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        User user = userDAO.getUserByNombre(nombre);
        if (user != null && user.validarCredenciales(nombre, contrasena)) {
            Main.loadScene("/view/MainMenu.fxml");
        } else {
            mostrarAlerta("Error", "Usuario no existente", "El nombre de usuario o la contraseña son incorrectos.");
        }
    }

    @FXML
    void RegistrarUsuario(ActionEvent event) {
        String nombre = txtNombreU.getText().trim();
        String contrasena = txtContraseñaU.getText().trim();

        if (nombre.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        if (userDAO.getUserByNombre(nombre) != null) {
            mostrarAlerta("Error", "Usuario existente", "El nombre de usuario ya está registrado.");
            return;
        }

        User newUser = new User(nombre, contrasena);
        userDAO.addUser(newUser);
        mostrarAlerta("Éxito", "Usuario registrado", "Usuario registrado exitosamente.");
        txtNombreU.clear();
        txtContraseñaU.clear();
    }

    private void mostrarAlerta(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(titulo.equals("Éxito") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}