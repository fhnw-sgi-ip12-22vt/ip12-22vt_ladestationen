package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class StageHandler {

    private static final String CSS_PATH = "/css/style.css";
    private static final String STAGE_TITLE = "Connect 'n Charge";

    private static ApplicationController controller;
    private static String lastFxmlPath;
    private static int additionalTime = 15;
    private static int timer;

    private static Stage stage;

    private StageHandler() {
        throw new AssertionError();
    }


    public static void openStage(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlPath));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        PageController pageController = fxmlLoader.getController();
        pageController.setController(controller);

        scene.getStylesheets().add(CSS_PATH);
        stage.setTitle(STAGE_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        if (!stage.isShowing()) {
            stage.initStyle(StageStyle.UNDECORATED);
        }
        stage.toFront();
        stage.setX(0);
        stage.setY(0);
        stage.setWidth(Screen.getPrimary().getBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getBounds().getHeight());

        if (stage.getScene() != null) {
            stage.setMaximized(true);
            stage.setFullScreen(true);
        }

        stage.show();
    }

    public static String getLastFxmlPath() {
        return lastFxmlPath;
    }

    public static int getAdditionalTime() {
        return additionalTime;
    }
    public static int getTimer() {
        return timer;
    }

    public static void setStage(Stage stageParam) {
        stage = stageParam;
    }

    public static void setController(ApplicationController controllerParam) {
        controller = controllerParam;
    }

    public static void setAdditionalTime(int additionalTimeParam) {
        additionalTime = additionalTimeParam;
    }

    public static void setLastFxmlPath(String lastFxmlPathParam) {
        lastFxmlPath = lastFxmlPathParam;
    }
}
