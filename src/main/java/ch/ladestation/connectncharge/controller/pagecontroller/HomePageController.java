package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.PageController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class HomePageController implements ViewMixin<Game, ControllerBase<Game>>, PageController {
    private static final String HELP_FXML_PATH = "/ch/ladestation/connectncharge/helppage.fxml";
    private static final String CSS_PATH = "/css/style.css";


    @FXML
    private AnchorPane menuPane;
    @FXML
    private AnchorPane shadowPane;

    @FXML
    public void handleShowEdgePresser(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/edgeclickscreen.fxml");
    }

    @FXML
    private void handleStackMenuClick(ActionEvent event) {
        menuPane.setVisible(true);
        menuPane.setOpacity(1);
        shadowPane.setVisible(true);
        shadowPane.setOpacity(1);
    }

    @FXML
    private void handleMenuCloseButton(ActionEvent event) {
        menuPane.setVisible(false);
        menuPane.setOpacity(0);
        shadowPane.setVisible(false);
        shadowPane.setOpacity(0);
    }

    @FXML
    private void handleHelpButton(ActionEvent event) throws IOException {
        StageHandler.setLastFxmlPath("/ch/ladestation/connectncharge/homepage.fxml");
        StageHandler.openStage(HELP_FXML_PATH);
    }

    @FXML
    private void handleAdminButton(ActionEvent event) throws IOException {
        StageHandler.setLastFxmlPath("/ch/ladestation/connectncharge/homepage.fxml");
        StageHandler.openStage("/ch/ladestation/connectncharge/adminpage.fxml");
    }

    @FXML
    private void handleShadowAnchorPaneClick(ActionEvent event) {
        shadowPane.setVisible(true);
        shadowPane.setOpacity(0);
        menuPane.setVisible(false);
        menuPane.setOpacity(0);
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
