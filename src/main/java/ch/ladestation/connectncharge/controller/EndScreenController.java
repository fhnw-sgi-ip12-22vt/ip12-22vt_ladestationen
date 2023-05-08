package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EndScreenController {

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

    public void goToGameScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(AppStarter.class.getResource("/ch/ladestation/connectncharge/gamepage.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        stage.setTitle("");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void goToNameInputScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(AppStarter.class.getResource("/ch/ladestation/connectncharge/nameinput.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        stage.setTitle("Name Eingeben");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}