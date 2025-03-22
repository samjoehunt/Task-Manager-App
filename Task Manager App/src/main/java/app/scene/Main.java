package app.scene;

import app.scene.controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the login scene, and connect the corresponding controller,
        // and display it to the user using the SceneController
        SceneController.setStage(primaryStage);
        SceneController.switchScene("/app/scene/login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}