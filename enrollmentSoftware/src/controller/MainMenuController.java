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
    void GoToInscripciones(ActionEvent event) {
    	Main.loadScene("/view/Enrollments.fxml");
    }
    
    @FXML
    void GoToCursos(ActionEvent event) {
    	Main.loadScene("/view/Courses.fxml");
    }

    @FXML
    void GoToEstudiante(ActionEvent event) {
    	Main.loadScene("/view/Students.fxml");

    }

}
