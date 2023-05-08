package ch.ladestation.connectncharge;

import ch.ladestation.connectncharge.controller.ApplicationControler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.pui.GamePUI;
import ch.ladestation.connectncharge.util.Pi4JContext;
import ch.ladestation.connectncharge.util.mvcbase.MvcLogger;
import com.pi4j.context.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStarter extends Application {
    private static final MvcLogger LOGGER = new MvcLogger();
    public static void main(String[] args) {
        Context pi4J = Pi4JContext.createContext();

        ApplicationControler controller = new ApplicationControler(new Game());
        var gPUI = new GamePUI(controller, pi4J);

        LOGGER.logInfo("App started");

        /////////////////////TMMMMMMMMMMMMMP
        int[] terms = {
                81,
                27,
                11,
                31,
                52,
                47,
                33,
                62,
                77,
                16,
                95,
                18,
                67};

        for (int terminal : terms) {
            controller.segmentToggled(gPUI.lookUpSegmentIdToSegment(terminal));
        }
        //////////////////TMPPPPPPPPPPPPPPPPPPPPPPPPPPPPP

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    AppStarter.class.getResource("/ch/ladestation/connectncharge/spielpage.fxml"));
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
