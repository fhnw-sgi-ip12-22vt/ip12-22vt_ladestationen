package ch.ladestation.connectncharge.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private TextField textField4;

    @FXML
    private TextField textField5;

    @FXML
    private TextField textField6;

    @FXML
    private GridPane keypadGridPane;

    @FXML
    public AnchorPane codeBackground;

    @FXML
    public Button deleteButton;

    @FXML
    public Button stopButton;

    @FXML
    private Label errorMessage;

    private TextField[] textFields;

    public void initialize() {
        textFields = new TextField[]{textField1, textField2, textField3, textField4, textField5, textField6};
        keypadGridPane.setVisible(false);
        errorMessage.setVisible(false);
    }

    @FXML
    private void showKeypad() {
        keypadGridPane.setVisible(true);
    }

    @FXML
    private void hideKeypad() {
        keypadGridPane.setVisible(false);
    }

    @FXML
    private void onKeyPressed(MouseEvent event) throws IOException {
        Button pressedButton = (Button) event.getSource();
        String inputNumber = pressedButton.getText();

        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                textField.setText(inputNumber);
                break;
            }
        }

        if (areAllTextFieldsFilled()) {
            checkCode(event);
        }
    }

    @FXML
    private void onDeleteKeyPressed() {
        for (int i = textFields.length - 1; i >= 0; i--) {
            if (!textFields[i].getText().isEmpty()) {
                textFields[i].setText("");
                break;
            }
        }
        errorMessage.setVisible(false);
    }

    private boolean areAllTextFieldsFilled() {
        for (TextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void checkCode(MouseEvent event) throws IOException {
        String enteredCode = String.join("", textField1.getText(), textField2.getText(), textField3.getText(), textField4.getText(), textField5.getText(), textField6.getText());

        if (enteredCode.equals("123456")) {
            StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
        } else {
            errorMessage.setText("Falscher Code");
            errorMessage.setVisible(true);
            for (TextField textField : textFields) {
                textField.setText("");
            }
        }
    }

    @FXML
    private void onStopButtonClicked(MouseEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
            (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
