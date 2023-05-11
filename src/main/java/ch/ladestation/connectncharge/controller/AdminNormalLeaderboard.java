package ch.ladestation.connectncharge.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminNormalLeaderboard {
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
        StageHandler.openStage("/ch/ladestation/connectncharge/adminhomepage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
    @FXML
    private void handleChooseAllButton(ActionEvent event) {
        chooseAllButton.getStyleClass().remove("leaderboard-choose-button");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button-selected");

        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button-selected"); // Zeile hinzugefügt
        unchooseAllButton.getStyleClass().add("leaderboard-un-choose-button"); // Zeile hinzugefügt
    }
    @FXML
    private void handleUnChooseAllButton(ActionEvent event) {
        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button");
        unchooseAllButton.getStyleClass().add("leaderboard-un-choose-button-selected");

        chooseAllButton.getStyleClass().remove("leaderboard-choose-button-selected"); // Zeile hinzugefügt
        chooseAllButton.getStyleClass().add("leaderboard-choose-button"); // Zeile hinzugefügt
    }
    @FXML
    private void handleTrashButton(ActionEvent event) { // Methode zum Löschen der Liste
        System.out.println("Liste wurde gelöscht.");

        // Setze die CSS-Klassen der beiden Buttons zurück
        chooseAllButton.getStyleClass().remove("leaderboard-choose-button-selected");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button");

        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button-selected");
        unchooseAllButton.getStyleClass().add("leaderboard-choose-button");
    }
}
