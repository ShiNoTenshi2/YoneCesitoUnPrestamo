package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            Main.primaryStage = primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuInicial.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root); // Tamaño dinámico, ajusta si quieres fijo como 400x400
            String cssPath = getClass().getResource("/Style.css").toExternalForm();
            if (cssPath != null) {
                scene.getStylesheets().add(cssPath);
            } else {
                System.out.println("Error: No se encontró el archivo /Style.css");
            }
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistema de Gestión");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root); // Ajusta tamaño si quieres, e.g., 600x600
                primaryStage.setScene(scene);
                String cssPath = Main.class.getResource("/Style.css").toExternalForm();
                if (cssPath != null) {
                    scene.getStylesheets().add(cssPath);
                }
            } else {
                scene.setRoot(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


