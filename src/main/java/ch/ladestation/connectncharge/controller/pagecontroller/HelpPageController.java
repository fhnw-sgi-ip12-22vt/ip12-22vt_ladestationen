package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class HelpPageController {
    private static final String DEFAUL_FXML_PATH = "/ch/ladestation/connectncharge/homepage.fxml";

    @FXML
    private void handleXCloseButton(ActionEvent event) throws IOException {
        String fxmlPath = StageHandler.getLastFxmlPath() != null ? StageHandler.getLastFxmlPath() : DEFAUL_FXML_PATH;
        StageHandler.openStage(fxmlPath, "/css/style.css");
    }
}
