package app.scene.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Class to control the initial loading and switching of scenes.
 */
public class SceneController {
    /**
     * Reference to the main stage
     */
    private static Stage primaryStage;

    /**
     * Method to set the stage which is currently in use to the primaryStage variable
     * @param stage Stage which is currently in use
     */
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Method to switch scenes
     * @param fxmlFile Path to the FXML file containing the front-end for the stage which is to be switched to
     */
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlFile));
            Parent root = loader.load();

            // To ensure that the screen stays in maximised, fullscreen mode, we set the stage's width and height
            // manually. Otherwise, the window will appear windowed, whilst technically being "maximised".
            primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
            primaryStage.setTitle("Task Manager");
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error switching scenes: " + e.getMessage());
        }
    }
}

