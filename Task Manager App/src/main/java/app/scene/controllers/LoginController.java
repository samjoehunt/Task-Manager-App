package app.scene.controllers;

import app.database.Database;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private StackPane root; // Root container in the FXML

    @FXML
    private Text loginFailureText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void authenticateUser() {
        if (Database.authenticateUser(usernameField.getText(), passwordField.getText())) {
            dashboardScene();
        } else {
            displayLoginError();
        }
    }

    @FXML
    public void dashboardScene() {

    }

    @FXML
    public void accountCreationScene() {
        SceneController.switchScene("/app/scene/accountCreation.fxml");
    }

    /**
     * Method to display an error message for a failed login attempt. This message will be visible
     * for 5 seconds.
     */
    public void displayLoginError() {
        // Make message visible
        loginFailureText.setVisible(true);
        loginFailureText.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            // Make message invisible again
            loginFailureText.setVisible(false);
            loginFailureText.setManaged(false);
        });
        pause.play();
    }
}

