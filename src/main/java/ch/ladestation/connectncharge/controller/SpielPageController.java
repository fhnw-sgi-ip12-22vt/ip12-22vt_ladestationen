package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class SpielPageController implements Initializable {

    private LocalTime startTime = LocalTime.of(0, 0);
    private LocalTime publicEndTime;
    @FXML
    private Label timerLabel;
    @FXML
    private Button addTimeButton;
    @FXML
    private Button endGameButton;
    @FXML
    private AnchorPane popupPane; // updated data type to AnchorPane

    @FXML
    private Button stackMenu;

    private Timeline timeline;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button menuCloseButton;
    @FXML
    private int additionalTime = 15;

    @FXML
    private Label costs;
    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    public void showHomePage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(AppStarter.class.getResource("/ch/ladestation/connectncharge/homepage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        stage.setTitle("Connect 'n Charge");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
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

            // Überprüfegi, ist die  Zeit gleich oder größer als 1 Stunde ist
            if (startTime.isAfter(LocalTime.of(0, 59, 59))) {
                timeline.stop(); // Stoppe den Timer
                endGame(); // Rufe die endGame-Methode auf, wenn die maximale Zeit erreicht ist
                startTime = LocalTime.of(1, 0); // set time on 1h
            }

            // update
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
        //Add the new time only if the current Time is less 60min.
        if (!startTime.equals(LocalTime.of(1, 0))) {
            // check if the 60min is over the limit.
            LocalTime newTime = startTime.plusSeconds(additionalTime);
            if (newTime.isAfter(LocalTime.of(1, 0))) {
                startTime = LocalTime.of(1, 0); // Set the timer at 1 hour.
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
        popupPane.setOpacity(1);
        popupPane.setVisible(true);
    }

    @FXML
    private void handleConfirmEndGameButton(ActionEvent event) throws IOException {
        // Stop the game and go to the homepage screen.
        showHomePage(event);
        popupPane.setOpacity(0);
        popupPane.setVisible(false);
    }

    @FXML
    private void handleCancelEndGameButton(ActionEvent event) {
        // close the pop up and let the game still run.
        popupPane.setOpacity(0);
        popupPane.setVisible(false);
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

    private void saveEndTime() {
        publicEndTime = startTime;
    }

    private void endGame() {
        saveEndTime(); // call saveEndTime method.
        // endGame() have to get called for end the game.
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
}
