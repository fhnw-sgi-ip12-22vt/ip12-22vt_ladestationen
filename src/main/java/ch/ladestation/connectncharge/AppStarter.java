package ch.ladestation.connectncharge;

import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.pui.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css",stage);
    }

    public static void main(String[] args) {
        Main.main(new String[]{});
        launch();
    }
}
