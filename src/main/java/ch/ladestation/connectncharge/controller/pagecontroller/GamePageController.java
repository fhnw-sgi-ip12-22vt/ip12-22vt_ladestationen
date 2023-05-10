package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.PageController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    private Timeline timeline;
    private int additionalTime = 15;
    private int seconds = 0, minutes = 0;


    private static final String FXML_PATH = "/ch/ladestation/connectncharge/helppage.fxml";
    private static final String CSS_PATH = "/css/style.css";
    private LocalTime startTime = LocalTime.of(0, 0);
    private LocalTime publicEndTime;

    @FXML
    public void showHomePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTimer();
        stackMenu.setVisible(true);
        stackMenu.setOpacity(1);
    }

    @Override
    public void setController(ApplicationController controller) {
        init(controller);
    }

    private void startTimer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            startTime = startTime.plusSeconds(1);

            if (startTime.isAfter(LocalTime.of(0, 59, 59))) {
                timeline.stop();
                endGame();
                startTime = LocalTime.of(1, 0);
            }

            if (startTime.equals(LocalTime.of(1, 0))) {
                timerLabel.setText("Zeit: 60:00");
            } else {
                timerLabel.setText("Zeit: " + startTime.format(formatter));
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleAddTimeButton(ActionEvent event) {
        String tippText;
        if (!startTime.equals(LocalTime.of(1, 0))) {
            LocalTime newTime = startTime.plusSeconds(additionalTime);
            if (newTime.isAfter(LocalTime.of(1, 0))) {
                startTime = LocalTime.of(1, 0);
            } else {
                startTime = newTime;
            }
            timerLabel.setText("Zeit: " + startTime.format(DateTimeFormatter.ofPattern("mm:ss")));
        }
        additionalTime += 15;
        /*seconds = seconds % 60 == 0 ? 0 : seconds + 15;
        minutes += additionalTime % 60 == 0 ? 1 : 0;
        tippText = minutes > 0 ? "Tipp +" + minutes + "min. " + seconds + "sek." : "Tipp +" + seconds + "sek.";*/
        addTimeButton.setText("Tipp +" + additionalTime + "sek.");
    }

    @FXML
    private void handleEndGameButton(ActionEvent event) {
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
    private void handleMenuCloseButton(ActionEvent event) {
        menuPane.setVisible(false);
        menuPane.setOpacity(0);
        shadowPane.setVisible(false);
        shadowPane.setOpacity(0);
    }

    private void saveEndTime() {
        publicEndTime = startTime;
    }

    private void endGame() {
        saveEndTime();
    }

    @FXML
    public void showCost() {

    }

    public LocalTime getPublicEndTime() {
        return publicEndTime;
    }

    public Label getCosts() {
        return costs;
    }

    @FXML
    private void handleHelpButton(ActionEvent event) throws IOException {
        StageHandler.setLastFxmlPath("/ch/ladestation/connectncharge/gamepage.fxml");
        StageHandler.openStage("/ch/ladestation/connectncharge/helppage.fxml", "/css/style.css");
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
