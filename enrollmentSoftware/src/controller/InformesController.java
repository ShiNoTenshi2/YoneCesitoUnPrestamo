package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class InformesController {

    @FXML private Button btnInformeIndividual;
    @FXML private Button btnInformeGeneral;
    @FXML private Button btnMainMenu;

    @FXML
    private void GoToMainMenu() {
        loadView("/view/MainMenu.fxml", "Menú Principal");
    }

    @FXML
    private void GoToInformeIndividual() {
        loadView("/view/InformeIndividual.fxml", "Informe Individual");
    }

    @FXML
    private void GoToInformeGeneral() {
        loadView("/view/InformeGeneral.fxml", "Informe General");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) btnMainMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            // Cambia esta línea:
            // System.err.println("Error al cargar la vista: " + e.getMessage());
            // Por esta:
            e.printStackTrace();
        }
    }}