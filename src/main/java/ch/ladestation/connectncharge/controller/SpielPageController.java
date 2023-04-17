package ch.ladestation.connectncharge.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class SpielPageController implements Initializable {

    @FXML
    private AnchorPane menuPane;

    @FXML
    private Label timerLabel;

    @FXML
    private Button addTimeButton;

    private int additionalTime = 15;

    // Burger-Menü-Button Touch-Event-Handler
    @FXML
    private void handleBurgerMenuTouch(TouchEvent event) {
        // Menüfenster anzeigen
        menuPane.setOpacity(1);
    }

    // Schließen-Schaltfläche (X) Event-Handler
    @FXML
    private void handleCloseMenu(ActionEvent event) {
        // Menüfenster ausblenden
        menuPane.setOpacity(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTimer();
    }

    private void startTimer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
        LocalTime[] startTime = {LocalTime.of(0, 0)};
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            startTime[0] = startTime[0].plusSeconds(1);
            timerLabel.setText("Zeit: " + startTime[0].format(formatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    @FXML
    private void handleAddTimeButton(ActionEvent event) {
        timerLabel.setText("Zeit: " + LocalTime.parse(timerLabel.getText().substring(6)).plusSeconds(additionalTime).format(DateTimeFormatter.ofPattern("mm:ss")));
        additionalTime += 15;
        addTimeButton.setText("Tipp +" + additionalTime + "sec");
    }
    @FXML
    private void handleEndGameButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Spiel beenden");
        alert.setHeaderText("Möchten Sie das Spiel wirklich beenden?");
        alert.setContentText("Wählen Sie Ja oder Nein.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Führen Sie die Aktionen aus, die beim Beenden des Spiels erforderlich sind.
            System.out.println("Spiel beendet");
        } else {
            // Schließen Sie das Popup, wenn der Benutzer "Nein" auswählt.
            System.out.println("Spiel fortgesetzt");
        }
    }
}

