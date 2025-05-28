package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuInicialController {

    @FXML
    private Button btnInicioSesion;

    @FXML
    private Button btnRegistrarUsuario;

    @FXML
    private void GoToInicioSesion() {
        try {
            // Cargar la vista MenuUsuarios.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuUsuarios.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual desde el botón
            Stage stage = (Stage) btnInicioSesion.getScene().getWindow();

            // Crear y establecer la nueva escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menú de Usuarios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí podrías mostrar un mensaje de error al usuario
        }
    }

    @FXML
    private void GoToRegistrarUsuario() {
        try {
            // Cargar la vista MenuRegistro.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuRegistro.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual desde el botón
            Stage stage = (Stage) btnRegistrarUsuario.getScene().getWindow();

            // Crear y establecer la nueva escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registro de Usuario");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Aquí podrías mostrar un mensaje de error al usuario
        }
    }
}