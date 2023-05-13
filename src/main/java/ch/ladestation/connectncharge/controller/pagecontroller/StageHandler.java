package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.AppStarter;
import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.model.text.FilePath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class StageHandler {

    private static final String STAGE_TITLE = "Connect 'n Charge";

    private static ApplicationController controller;
    private static String lastFxmlPath;

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

        scene.getStylesheets().add(FilePath.CSS.getFilePath());
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

    public static void setStage(Stage stageParam) {
        stage = stageParam;
    }

    public static void setController(ApplicationController controllerParam) {
        controller = controllerParam;
    }

    public static void setLastFxmlPath(String lastFxmlPathParam) {
        lastFxmlPath = lastFxmlPathParam;
    }
}
