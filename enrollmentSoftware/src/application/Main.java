package application;
	//oña
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


public class Main extends Application {
	private static Stage primaryStage;
	@Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage;
            VBox root = (VBox) FXMLLoader.load(getClass().getResource("/view/MenuUsuarios.fxml"));
            Scene scene = new Scene(root, 600, 600); 
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	
    public static void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root, 600, 600); 
                primaryStage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void main(String[] args) {
		launch(args);
	}
}
