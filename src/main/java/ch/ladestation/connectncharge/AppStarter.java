package ch.ladestation.connectncharge;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Node;
import ch.ladestation.connectncharge.pui.GamePUI;
import ch.ladestation.connectncharge.util.Pi4JContext;
import ch.ladestation.connectncharge.util.mvcbase.MvcLogger;
import com.pi4j.context.Context;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class AppStarter extends Application {
    private static final MvcLogger LOGGER = new MvcLogger();
    private static ApplicationController controller;

    public static void main(String[] args) {
        Context pi4J = Pi4JContext.createContext();

        controller = new ApplicationController(new Game());
        var gPUI = new GamePUI(controller, pi4J);

        LOGGER.logInfo("App started");

        int[] terms = {81, 27, 11, 31, 52, 47, 33, 62, 77, 16, 95, 18, 67};

        var terminalNodes = Arrays.stream(terms)
            .mapToObj(gPUI::lookUpSegmentIdToSegment)
            .map(seg -> (Node) seg)
            .toArray(Node[]::new);
        controller.setTerminals(terminalNodes);

        // This will ensure Pi4J is properly finished. All I/O instances are
        // released by the system and shutdown in the appropriate
        // manner. It will also ensure that any background
        // threads/processes are cleanly shutdown and any used memory
        // is returned to the system.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            controller.shutdown();
            pi4J.shutdown();
            LOGGER.logInfo("App stopped");
        }));
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StageHandler.setStage(stage);
        StageHandler.openStage("/ch/ladestation/connectncharge/loadingpage.fxml", "/css/style.css");
    }
}
