package ch.ladestation.connectncharge.controller.pagecontroller.middle;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.pagecontroller.PageController;
import ch.ladestation.connectncharge.controller.pagecontroller.StageHandler;
import ch.ladestation.connectncharge.model.game.gameinfo.MyTimer;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.model.text.FilePath;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GamePageController implements ViewMixin<Game, ControllerBase<Game>>, Initializable, PageController {

    @FXML
    private AnchorPane endGampePopupPane;
    @FXML
    private AnchorPane hintPopupPane;
    @FXML
    private AnchorPane shadowPane;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button addTimeButton;
    @FXML
    private Button stackMenu;
    @FXML
    private Label costs;
    @FXML
    private Label timerLabel;
    private static String publicEndTime;

    private String leaveGamePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTimer();
        stackMenu.setVisible(true);
        stackMenu.setOpacity(1);
        MyTimer.addTime(0, addTimeButton);
    }

    @Override
    public void setController(ApplicationController controller) {
        init(controller);
    }

    private void startTimer() {
        MyTimer.setTimerLabel(timerLabel);
        MyTimer.start();
    }

    @FXML
    public void showHomePage(ActionEvent event) throws IOException {
        StageHandler.openStage(leaveGamePath);
    }

    @FXML
    private void handleAddTimeButton(ActionEvent event) {
        MyTimer.addTime(MyTimer.ADD_TIME, addTimeButton);
    }

    @FXML
    private void handleEndGameButton(ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonId = button.getId();

        if (buttonId.contains("highScore")) {
            leaveGamePath = FilePath.HIGHSCORE.getFilePath();
        } else if (buttonId.contains("bonusRound")) {
            leaveGamePath = FilePath.HOMEPAGE.getFilePath();
        } else if (buttonId.contains("admin")) {
            leaveGamePath = FilePath.ADMINPAGE.getFilePath();
        } else {
            leaveGamePath = FilePath.HOMEPAGE.getFilePath();
        }

        endGampePopupPane.setVisible(true);
        endGampePopupPane.setOpacity(1);
        shadowPane.setVisible(true);
        shadowPane.setOpacity(1);
    }

    @FXML
    private void handleConfirmEndGameButton(ActionEvent event) throws IOException {
        showHomePage(event);
        endGampePopupPane.setVisible(false);
        endGampePopupPane.setOpacity(0);
        MyTimer.stop();
    }

    @FXML
    private void handleCancelEndGameButton(ActionEvent event) {
        endGampePopupPane.setVisible(false);
        endGampePopupPane.setOpacity(0);
        shadowPane.setVisible(false);
        shadowPane.setOpacity(0);
    }

    @FXML
    private void handleStackMenuClick(ActionEvent event) {
        menuPane.setVisible(true);
        menuPane.setOpacity(1);
        shadowPane.setVisible(true);
        shadowPane.setOpacity(1);
    }

    @FXML
    private void handleHelpButton(ActionEvent event) throws IOException {
        StageHandler.setLastFxmlPath(FilePath.GAMEPAGE.getFilePath());
        StageHandler.openStage(FilePath.HELPPAGE.getFilePath());
    }

    @FXML
    private void handleShadowAnchorPaneClick(ActionEvent event) {
        endGampePopupPane.setVisible(false);
        endGampePopupPane.setOpacity(0);
        shadowPane.setVisible(false);
        shadowPane.setOpacity(0);
        menuPane.setVisible(false);
        menuPane.setOpacity(0);
    }

    @FXML
    private void handleMenuCloseButton(ActionEvent event) {
        menuPane.setVisible(false);
        menuPane.setOpacity(0);
        shadowPane.setVisible(false);
        shadowPane.setOpacity(0);
    }

    private void saveEndTime() {
        publicEndTime = timerLabel.getText().replaceAll("Zeit: ", "");
    }

    public static String getPublicEndTime() {
        return publicEndTime;
    }

    private void endGame() {
        saveEndTime();
    }

    @FXML
    public void showCost() {

    }

    public Label getCosts() {
        return costs;
    }

    @Override
    public void setupModelToUiBindings(Game model) {
        onChangeOf(model.currentScore).convertedBy(String::valueOf).update(costs.textProperty());
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
