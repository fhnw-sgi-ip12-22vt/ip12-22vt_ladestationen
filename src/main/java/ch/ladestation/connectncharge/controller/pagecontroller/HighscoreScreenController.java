package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HighscoreScreenController {
    public Button btnPlayAgain;
    public Button btnBonus;
    public ImageView imgHome;

    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    public void handlePlayAgain(ActionEvent actionEvent) {

    }

    public void handlePlayBonus(ActionEvent actionEvent) {

    }

    public void showHomeScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
                (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
    }
}
