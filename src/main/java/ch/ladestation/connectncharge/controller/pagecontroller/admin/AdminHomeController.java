package ch.ladestation.connectncharge.controller.pagecontroller.admin;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.pagecontroller.PageController;
import ch.ladestation.connectncharge.controller.pagecontroller.StageHandler;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;

public class AdminHomeController implements ViewMixin<Game, ControllerBase<Game>>, PageController {

    @FXML
    private Button backButton;

    @FXML
    private Button normalButton; // Fügen Sie diese Zeile hinzu, um den normalButton zu verknüpfen

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml");
    }

    @FXML
    private void handleNormalButton(ActionEvent event) throws IOException { // Fügen Sie diese Methode hinzu
        StageHandler.openStage("/ch/ladestation/connectncharge/adminnormalleaderboard.fxml");
    }

    @Override
    public void setController(ApplicationController controller) {
        init(controller);
    }

    @Override
    public void initializeParts() {

    }

    @Override
    public void layoutParts() {

    }

    @Override
    public List<String> getStylesheets() {
        return null;
    }
}
