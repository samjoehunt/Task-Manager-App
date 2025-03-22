package app.scene.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private StackPane root; // Root container in the FXML

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

