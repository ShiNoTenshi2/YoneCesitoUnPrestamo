package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button btnCursos;

    @FXML
    private Button btnEstudiante;

    @FXML
    private Button btnInscripciones;

    @FXML
    void GoToSolicitantes(ActionEvent event) {
    	Main.loadScene("/view/Solicitantes.fxml");

    }

}
