package ch.ladestation.connectncharge.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminHomeController {

    @FXML
    private Button backButton;

    @FXML
    private Button normalButton; // Fügen Sie diese Zeile hinzu, um den normalButton zu verknüpfen

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void handleNormalButton(ActionEvent event) throws IOException { // Fügen Sie diese Methode hinzu
        StageHandler.openStage("/ch/ladestation/connectncharge/adminnormalleaderboard.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
