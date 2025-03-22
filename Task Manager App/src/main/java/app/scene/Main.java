package app.scene;

import app.scene.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file for the login scene, which will be the first scene displayed to the user
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Get the controller instance
        LoginController controller = loader.getController();

        // Pass the stage to the controller
        controller.setStage(primaryStage);

        // Set up the scene
        Scene scene = new Scene(root);

        // Configure the primary stage
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}