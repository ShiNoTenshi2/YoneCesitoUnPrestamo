package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button btnSolicitantes;
    
    @FXML
    private Button btnSancion;
    
    @FXML
    private Button btnSala;
    
    @FXML
    private Button btnAudiovisual;
    
    @FXML
    private Button btnDevolucion;
    
    @FXML
    private Button btnPrestamo;
    
    @FXML
    void GoToSolicitantes(ActionEvent event) {
    	Main.loadScene("/view/Solicitantes.fxml");
    }

    @FXML
    void GoToSala(ActionEvent event) {
    	Main.loadScene("/view/Sala.fxml");
    }
    
    @FXML
    void GoToSancion(ActionEvent event) {
    	Main.loadScene("/view/Sancion.fxml");
    }
    
    @FXML
    void GoToAudiovisual(ActionEvent event) {
    	Main.loadScene("/view/Audiovisual.fxml");
    }
    
    @FXML
    void GoToDevolucion(ActionEvent event) {
    	Main.loadScene("/view/Devolucion.fxml");
    }

    @FXML
    void GoToPrestamo(ActionEvent event) {
    	Main.loadScene("/view/Prestamo.fxml");
    }
    
}
