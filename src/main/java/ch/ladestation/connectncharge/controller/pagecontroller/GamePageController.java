package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class GamePageController implements Initializable {

    private static final String FXML_PATH = "/ch/ladestation/connectncharge/helppage.fxml";
    private static final String CSS_PATH = "/css/style.css";
    private LocalTime startTime = LocalTime.of(0, 0);
    private LocalTime publicEndTime;
    @FXML
    private Label timerLabel;
    @FXML
    private Button addTimeButton;
    @FXML
    private Button endGameButton;
    @FXML
    private AnchorPane endGampePopupPane;
    @FXML
    private AnchorPane shadowPane;
    @FXML
    private Button stackMenu;
    @FXML
    private Button cancelEndGameButton;

    private Timeline timeline;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private AnchorPane tippPopupPane;
    @FXML
    private Button menuCloseButton;
    @FXML
    private int additionalTime = 15;
    @FXML
    private Label costs;

    @FXML
    public void showHomePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTimer();
        stackMenu.setVisible(true);
        stackMenu.setOpacity(1);
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
        addTimeButton.setText("Tipp +" + additionalTime + "sec");
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
        StageHandler.openStage("/ch/ladestation/connectncharge/helppage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
