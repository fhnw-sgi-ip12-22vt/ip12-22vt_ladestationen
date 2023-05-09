package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NameInputController implements Initializable {

    @FXML
    public TextField txtNameInput;
    @FXML
    public GridPane keyboardPane;
    @FXML
    public Button btnCaps;
    @FXML
    public Button btnSpace;
    @FXML
    public Button btnDel;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    private Parent root;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;

    private boolean capsLockOn = false;

    @FXML
    public void showNamePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/nameinput.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    public void goBackToEndScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/endscreen.fxml", "/css/style.css",
                (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtNameInput.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                showKeyboard();
            }
        });
        anchorPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                hideKeyboard();
            }
        });
    }

    @FXML
    public void handleInputName(ActionEvent actionEvent) {
        String playerName = txtNameInput.getText();
    }

    public void goToHighscoreScreen(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }

    public void keyPressed(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String buttonText = button.getText();
        String currentText = txtNameInput.getText();

        if (capsLockOn) {
            buttonText = buttonText.toUpperCase();
        }
        txtNameInput.setText(currentText + buttonText);
    }

    public void toggleCapsLock(ActionEvent actionEvent) {

    }

    public void toggleDelete(ActionEvent actionEvent) {
        String currentText = txtNameInput.getText();
        if (currentText.length() > 0) {
            txtNameInput.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    public void toggleSpace(ActionEvent actionEvent) {

    }

    public void showKeyboard() {
        keyboardPane.setVisible(true);
        keyboardPane.setOpacity(1);
    }

    public void hideKeyboard() {
        keyboardPane.setVisible(false);
        keyboardPane.setOpacity(1);
    }
}
