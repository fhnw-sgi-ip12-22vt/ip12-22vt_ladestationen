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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NameInputController implements Initializable, ViewMixin<Game, ControllerBase<Game>>, PageController {

    @FXML
    private TextField txtNameInput;
    @FXML
    private GridPane keyboardPane;
    @FXML
    private Button btnCaps;
    @FXML
    private Button btnSpace;
    @FXML
    private Button btnDel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button q;
    @FXML
    private Button w;
    @FXML
    private Button e;
    @FXML
    private Button r;
    @FXML
    private Button t;
    @FXML
    private Button z;
    @FXML
    private Button u;
    @FXML
    private Button i;
    @FXML
    private Button o;
    @FXML
    private Button p;
    @FXML
    private Button ue;
    @FXML
    private Button a;
    @FXML
    private Button s;
    @FXML
    private Button d;
    @FXML
    private Button f;
    @FXML
    private Button g;
    @FXML
    private Button h;
    @FXML
    private Button j;
    @FXML
    private Button k;
    @FXML
    private Button l;
    @FXML
    private Button oe;
    @FXML
    private Button ae;
    @FXML
    private Button y;
    @FXML
    private Button x;
    @FXML
    private Button c;
    @FXML
    private Button v;
    @FXML
    private Button b;
    @FXML
    private Button n;
    @FXML
    private Button m;
    @FXML
    private Label lblWarning;

    private boolean capsLockOn = false;
    private static String currentName;

    @FXML
    public void showNamePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/nameinput.fxml", "/css/style.css");
    }

    public void goBackToEndScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/endscreen.fxml", "/css/style.css");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtNameInput.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                showKeyboard();
            }
        });

        txtNameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                txtNameInput.setText(oldValue);
                lblWarning.setText("Zu lang");
            } else {
                lblWarning.setText("");
            }
        });

        anchorPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                hideKeyboard();
            }
        });
    }


    public void goToHighscoreScreen(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css");
    }

    public void keyPressed(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String buttonText = button.getText();
        String currentText = txtNameInput.getText();

        txtNameInput.setText(currentText + buttonText);

        if (capsLockOn) {
            capsLockOn = false;
            btnCaps.setStyle("-fx-background-color: #ffffff");
            q.setText("q");
            w.setText("w");
            e.setText("e");
            r.setText("r");
            t.setText("t");
            z.setText("z");
            u.setText("u");
            i.setText("i");
            o.setText("o");
            p.setText("p");
            ue.setText("ü");
            a.setText("a");
            s.setText("s");
            d.setText("d");
            f.setText("f");
            g.setText("g");
            h.setText("h");
            j.setText("j");
            k.setText("k");
            l.setText("l");
            oe.setText("ö");
            ae.setText("ä");
            y.setText("y");
            x.setText("x");
            c.setText("c");
            v.setText("v");
            b.setText("b");
            n.setText("n");
            m.setText("m");
        }
    }

    public void toggleCapsLock(ActionEvent actionEvent) {
        if (capsLockOn) {
            capsLockOn = false;
            btnCaps.setStyle("-fx-background-color: #ffffff");
            q.setText("q");
            w.setText("w");
            e.setText("e");
            r.setText("r");
            t.setText("t");
            z.setText("z");
            u.setText("u");
            i.setText("i");
            o.setText("o");
            p.setText("p");
            ue.setText("ü");
            a.setText("a");
            s.setText("s");
            d.setText("d");
            f.setText("f");
            g.setText("g");
            h.setText("h");
            j.setText("j");
            k.setText("k");
            l.setText("l");
            oe.setText("ö");
            ae.setText("ä");
            y.setText("y");
            x.setText("x");
            c.setText("c");
            v.setText("v");
            b.setText("b");
            n.setText("n");
            m.setText("m");
        } else {
            capsLockOn = true;
            btnCaps.setStyle("-fx-background-color: #AEAEAE");
            q.setText("Q");
            w.setText("W");
            e.setText("E");
            r.setText("R");
            t.setText("T");
            z.setText("Z");
            u.setText("U");
            i.setText("I");
            o.setText("O");
            p.setText("P");
            ue.setText("Ü");
            a.setText("A");
            s.setText("S");
            d.setText("D");
            f.setText("F");
            g.setText("G");
            h.setText("H");
            j.setText("J");
            k.setText("K");
            l.setText("L");
            oe.setText("Ö");
            ae.setText("Ä");
            y.setText("Y");
            x.setText("X");
            c.setText("C");
            v.setText("V");
            b.setText("B");
            n.setText("N");
            m.setText("M");
        }
    }

    public void toggleDelete(ActionEvent actionEvent) {
        String currentText = txtNameInput.getText();
        if (currentText.length() > 0) {
            txtNameInput.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    public void toggleSpace(ActionEvent actionEvent) {
        String currentText = txtNameInput.getText();
        txtNameInput.setText(currentText + "  ");

    }

    public void showKeyboard() {
        keyboardPane.setVisible(true);
        keyboardPane.setOpacity(1);
    }

    public void hideKeyboard() {
        keyboardPane.setVisible(false);
        keyboardPane.setOpacity(1);
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

    public void handleInputName(ActionEvent actionEvent) {
        currentName = txtNameInput.getText();
    }

    public static String getCurrentName() {
        return currentName;
    }

}
