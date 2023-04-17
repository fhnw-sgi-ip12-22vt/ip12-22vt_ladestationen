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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SpielPageController implements Initializable {

    private LocalTime startTime = LocalTime.of(0, 0);
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
    @FXML
    private AnchorPane menuPane;
    @FXML
    private Button menuCloseButton;
    @FXML
    private int additionalTime = 15;
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
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            startTime = startTime.plusSeconds(1);
            timerLabel.setText("Zeit: " + startTime.format(formatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleAddTimeButton(ActionEvent event) {
        startTime = startTime.plusSeconds(additionalTime);
        timerLabel.setText("Zeit: " + startTime.format(DateTimeFormatter.ofPattern("mm:ss")));
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
        // Schließen Sie das Popup und lassen Sie das Spiel weiterlaufen.
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
}
