package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class AdminNormalLeaderboard implements ViewMixin<Game, ControllerBase<Game>>, PageController {
    public AnchorPane shadowPane;
    public AnchorPane verifyPopup;
    @FXML
    private Button backButton;
    @FXML
    private Button trashButton;
    @FXML
    private Button chooseAllButton;
    @FXML
    private Button unchooseAllButton;

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/adminhomepage.fxml");
    }

    @FXML
    private void handleChooseAllButton(ActionEvent event) {
        chooseAllButton.getStyleClass().remove("leaderboard-choose-button");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button-selected");
    }

    @FXML
    private void handleUnChooseAllButton(ActionEvent event) {
        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button");
        unchooseAllButton.getStyleClass().add("leaderboard-un-choose-button-selected");
    }

    @FXML
    private void handleTrashButton(ActionEvent event) { // Methode zum Löschen der Liste
        System.out.println("Liste wurde gelöscht.");

        shadowPane.setVisible(true);
        shadowPane.setOpacity(1);
        verifyPopup.setVisible(true);

        // Setze die CSS-Klassen der beiden Buttons zurück
        chooseAllButton.getStyleClass().remove("leaderboard-choose-button-selected");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button");

        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button-selected");
        unchooseAllButton.getStyleClass().add("leaderboard-choose-button");
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

    public void handleShadowAnchorPaneClick(MouseEvent mouseEvent) {

    }
}
