package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class StageHandler {
    private static ApplicationController controller;
    private static String lastFxmlPath;

    private static Stage stage;

    private StageHandler() {
        throw new AssertionError();
    }

    private static final String STAGE_TITLE = "Connect 'n Charge";

    public static void openStage(String fxmlPath, String cssPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlPath));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        PageController pageController = fxmlLoader.getController();
        pageController.setController(controller);

        scene.getStylesheets().add(cssPath);
        stage.setTitle(STAGE_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);

        if (stage.getScene() != null) {
            stage.setMaximized(true);
            stage.setFullScreen(true);
        }

        stage.show();
    }

    public static void setLastFxmlPath(String lastFxmlPathParam) {
        lastFxmlPath = lastFxmlPathParam;
    }

    public static String getLastFxmlPath() {
        return lastFxmlPath;
    }

    public static void setStage(Stage stageParam) {
        stage = stageParam;
    }

    public static void setController(ApplicationController controllerParam) {
        controller = controllerParam;
    }
}
