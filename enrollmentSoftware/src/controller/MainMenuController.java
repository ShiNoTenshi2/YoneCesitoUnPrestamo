package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button btnSolicitantes;
    
    @FXML
    private Button btnSala;
    
    @FXML
    void GoToSolicitantes(ActionEvent event) {
    	Main.loadScene("/view/Solicitantes.fxml");

    }

    @FXML
    void GoToSala(ActionEvent event) {
    	Main.loadScene("/view/Sala.fxml");

    }
    
}
