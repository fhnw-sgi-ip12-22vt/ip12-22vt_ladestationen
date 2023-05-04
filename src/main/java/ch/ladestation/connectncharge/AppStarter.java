package ch.ladestation.connectncharge;

import ch.ladestation.connectncharge.controller.PUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStarter extends Application {
    public static void main(String[] args) {
        PUIController pc = new PUIController();
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                AppStarter.class.getResource("/ch/ladestation/connectncharge/loadingscreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("src/main/resources/css/style.css");
            stage.setTitle("Connect 'n Charge");
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
