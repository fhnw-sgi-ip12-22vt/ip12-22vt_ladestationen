package ch.ladestation.connectncharge.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpPageController {
    private String fxmlFileName;

    @FXML
    private void handleHelpButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
