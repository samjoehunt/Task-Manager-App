package app.scene.controllers;

import app.database.Database;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AccountCreationController {
    @FXML
    private Text invalidCredentialsFailureText, takenUsernameFailureText, invalidEmailFailureText;

    @FXML
    private TextField emailField, usernameField;
    @FXML
    private PasswordField passwordField;

    /**
     * Function to create an account with the entered credentials. If credentials are valid, a new user is added
     * to the users table in taskManager.db. If not, an appropriate error will be displayed and the user is
     * encouraged to try again.
     */
    @FXML
    public void createAccount() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            displayError(invalidCredentialsFailureText);
        } else if (!correctEmailFormat(email)) {
            displayError(invalidEmailFailureText);
        } else if (Database.userExists(username)) {
            displayError(takenUsernameFailureText);
        } else {
            Database.insertUser(username, email, password);
            dashboardScene();
        }
    }

    /**
     * Function to find whether a given string follows the correct format for an email address.
     * @param email Email address to be compared with the specified regex.
     * @return Boolean value indicating whether the specified email is in the correct format.
     */
    public boolean correctEmailFormat(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    /**
     * Displays a passed Text node containing an error message for 5 seconds, before hiding it again.
     * @param errorText Text node containing the error message to be displayed.
     */
    public void displayError(Text errorText) {
        // Make message visible
        errorText.setVisible(true);
        errorText.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            // Make message invisible again
            errorText.setVisible(false);
            errorText.setManaged(false);
        });
        pause.play();
    }

    /**
     * Change the current scene to the login scene.
     */
    @FXML
    public void loginScene() {
        SceneController.switchScene("/app/scene/login.fxml");
    }

    /**
     * Change the current scene to the dashboard scene.
     */
    @FXML
    public void dashboardScene() {
        SceneController.switchScene("/app/scene/dashboard.fxml");
    }
}
