package app.scene.controllers;

import app.database.Database;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private Text loginFailureText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    /**
     * Method to authenticate a user by their login details. If the details are correct, the scene is switched
     * to the dashboard scene. If not, an error is displayed, informing the user that their login attempt failed.
     */
    @FXML
    public void authenticateUser() {
        if (Database.authenticateUser(usernameField.getText(), passwordField.getText())) {
            dashboardScene();
        } else {
            displayLoginError();
        }
    }

    /**
     * Method to switch the current scene to the dashboard scene.
     */
    @FXML
    public void dashboardScene() {
        SceneController.switchScene("/app/scene/dashboard.fxml");
    }

    /**
     * Method to switch the current scene to the account creation scene.
     */
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

