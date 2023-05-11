package ch.ladestation.connectncharge.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminChooseAll {
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/adminhomepage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
