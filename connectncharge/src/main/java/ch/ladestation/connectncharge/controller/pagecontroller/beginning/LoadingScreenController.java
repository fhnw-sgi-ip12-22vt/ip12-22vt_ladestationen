package ch.ladestation.connectncharge.controller.pagecontroller.beginning;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.pagecontroller.PageController;
import ch.ladestation.connectncharge.controller.pagecontroller.StageHandler;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.model.text.FilePath;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.List;

public class LoadingScreenController implements ViewMixin<Game, ControllerBase<Game>>, PageController {

    @FXML
    public void loadHomePage(ActionEvent event) throws IOException {
        StageHandler.openStage(FilePath.HOMEPAGE.getFilePath());
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
    public void setController(ApplicationController controller) {

    }
}
