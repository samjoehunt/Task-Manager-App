package app.scene.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

    }

    @FXML
    public void dashboardScene() {

    }

    @FXML
    public void accountCreationScene() {
        SceneController.switchScene("/app/scene/accountCreation.fxml");
    }
}

