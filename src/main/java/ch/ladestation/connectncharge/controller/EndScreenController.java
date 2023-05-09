package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EndScreenController {
    @FXML
    public Button btnHighscore;
    @FXML
    public Button btnPlayAgain;
    @FXML
    public Label lblTime;


    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    public void showEndPage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/endscreen.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    public void handlePlayAgainButton(ActionEvent actionEvent) throws IOException {
        showGameScreen(actionEvent);
    }

    public void showGameScreen(ActionEvent actionEvent) throws IOException{
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());

    }

    @FXML
    public void handleNameInputButton(ActionEvent actionEvent) throws IOException {
        showNameInputScreen(actionEvent);
    }


    public void showNameInputScreen(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/nameinput.fxml", "/css/style.css",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }



}