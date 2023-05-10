package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class EdgePresserController {

    @FXML
    private AnchorPane menuPane;

    @FXML
    public void handleNextButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/countdownpage.fxml", "/css/style.css");
    }

    @FXML
    private void handleHelpButton(ActionEvent event) throws IOException {
        StageHandler.setLastFxmlPath("/ch/ladestation/connectncharge/edgepresserpage.fxml");
        StageHandler.openStage("/ch/ladestation/connectncharge/helppage.fxml", "/css/style.css");
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
