package ch.ladestation.connectncharge;

import ch.ladestation.connectncharge.pui.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main Application class
 */
public class AppStarter extends Application {
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

    public static void main(String[] args) {
        Main.main(new String[]{});
        launch();
    }
}
