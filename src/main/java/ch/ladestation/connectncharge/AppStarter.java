package ch.ladestation.connectncharge;

//import ch.ladestation.connectncharge.controller.PUIController;

import ch.ladestation.connectncharge.controller.StageHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStarter extends Application {
    public static void main(String[] args) {
        //PUIController pc = new PUIController();
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/nameinput.fxml", "/css/style.css", stage);
    }
}
