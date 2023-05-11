package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.PageController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable, ViewMixin<Game, ControllerBase<Game>>, PageController {
    @FXML
    private Button btnHighscore;
    @FXML
    private Button btnPlayAgain;
    @FXML
    private Label lblTime;
    @FXML
    private Button btnMenu;

    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    //create variable from GamePageController publicEndTime
    //private String endTime = GamePageController.publicEndTime;

    private final String endTime = String.valueOf(GamePageController.getPublicEndTime());

    @FXML
    public void showEndPage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/endscreen.fxml");
    }

    @FXML
    public void handlePlayAgainButton(ActionEvent actionEvent) throws IOException {
        showGameScreen(actionEvent);
    }

    public void showGameScreen(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml");

    }

    @FXML
    public void handleNameInputButton(ActionEvent actionEvent) throws IOException {
        showNameInputScreen(actionEvent);
    }

    @FXML
    public void handleMenuClick(ActionEvent actionEvent) {

    }

    public void showNameInputScreen(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/nameinput.fxml");
    }

    @FXML
    private void handleAdminButton(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/adminpage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTime.setText(endTime);
    }

    @Override
    public void setController(ApplicationController controller) {
        init(controller);
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
