package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.PageController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelpPageController implements ViewMixin<Game, ControllerBase<Game>>, Initializable, PageController {
    private static final String DEFAUL_FXML_PATH = "/ch/ladestation/connectncharge/homepage.fxml";

    @FXML
    private TextArea helpTextField;

    @FXML
    private void handleXCloseButton(ActionEvent event) throws IOException {
        String fxmlPath = StageHandler.getLastFxmlPath() != null ? StageHandler.getLastFxmlPath() : DEFAUL_FXML_PATH;
        StageHandler.openStage(fxmlPath, "/css/style.css");
    }

    @Override
    public void setController(ApplicationController controller) {
        init(controller);
    }

    @Override
    public void initializeParts() {

    }

    @Override
    public void layoutParts() {

    }

    @Override
    public List<String> getStylesheets() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helpTextField.addEventFilter(MouseEvent.ANY, MouseEvent::consume);

        // Enable touch events
        helpTextField.addEventFilter(TouchEvent.ANY, event -> {
            if (event.getTouchCount() == 1) {
                // Handle single-finger touch scrolling gestures here
                TouchPoint touchPoint = event.getTouchPoint();
                if (event.getEventType() == TouchEvent.TOUCH_MOVED) {
                    // Calculate the vertical scroll delta based on the touch movements
                    //double deltaY = touchPoint.getSceneY() - touchPoint.getPreviousSceneY();
                    double deltaY = touchPoint.getSceneY();
                    double scrollDelta = deltaY / helpTextField.getHeight() * helpTextField.getHeight();
                    helpTextField.setScrollTop(helpTextField.getScrollTop() - scrollDelta);
                    event.consume();
                }
            }
        });
    }
}
