package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageHandler {
    private static final String STAGE_TITLE = "Connect 'n Charge";

    public static void openStage(String fxmlPath, String cssPath, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            AppStarter.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(cssPath);
        Parent root = FXMLLoader.load(AppStarter.class.getResource(fxmlPath));
        scene = new Scene(root);
        scene.getStylesheets().add(cssPath);
        stage.setTitle(STAGE_TITLE);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
