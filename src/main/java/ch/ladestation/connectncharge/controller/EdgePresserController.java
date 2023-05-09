package ch.ladestation.connectncharge.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EdgePresserController {

    @FXML
    private AnchorPane menuPane;

    @FXML
    public void showEdgePresser(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/helppage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void handleHelpButton(ActionEvent event) throws IOException {
        StageHandler.setLastFxmlPath("/ch/ladestation/connectncharge/edgepresser.fxml");
        StageHandler.openStage("/ch/ladestation/connectncharge/helppage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void handleStackMenuClick(ActionEvent event) {
        menuPane.setVisible(true);
        menuPane.setOpacity(1);
    }

    @FXML
    private void handleMenuCloseButton(ActionEvent event) {
        menuPane.setVisible(false);
        menuPane.setOpacity(0);
    }
}
