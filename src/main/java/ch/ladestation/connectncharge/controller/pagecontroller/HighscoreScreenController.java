package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.AppStarter;
import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HighscoreScreenController {
    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
