package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.AppStarter;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoadingScreenController implements ViewMixin<Game, ControllerBase<Game>> {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    public void loadHomePage(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader =
                new FXMLLoader(AppStarter.class.getResource("/ch/ladestation/connectncharge/homepage.fxml"));
            root = loader.load();
            HomePageController homePageController = loader.getController();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
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
}
