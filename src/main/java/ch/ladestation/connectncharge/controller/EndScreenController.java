package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EndScreenController {
    @FXML
    private Label lblTime;
    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
//    @FXML
//    private Button btnPlayAgain;
//    @FXML
//    private Button btnHighscore;


    @FXML
    public void showEndScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(AppStarter.class.getResource("/ch/ladestation/connectncharge/endscreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        stage.setTitle("Fertig Screen");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToHighScoreScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(AppStarter.class.getResource("/ch/ladestation/connectncharge/highscore.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        stage.setTitle("Highscore");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void goToPlayAgain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(AppStarter.class.getResource("/ch/ladestation/connectncharge/spielpage.fxml"));
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
}
