package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.PageController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Player;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighscoreScreenController implements ViewMixin<Game, ControllerBase<Game>>, PageController {
    @FXML
    private Button btnPlayAgain;
    @FXML
    private Button btnBonus;
    @FXML
    private ImageView imgHome;
    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn<Player, Integer> rankColumn;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, String> timeColumn;
    @FXML
    private ScrollPane restHighscore;
    @FXML
    private TableView<Player> restTableView;
    @FXML
    private TableColumn<Player, Integer> restRankColumn;
    @FXML
    private TableColumn<Player, String> restNameColumn;
    @FXML
    private TableColumn<Player, String> restTimeColumn;
    //private static final String PLAYER_PATH = "/textfiles/highscore/player.txt";

    private static final int PLAYER_PLACE_TOP = 5;
    private String endTime = String.valueOf(GamePageController.getPublicEndTime());
    private String playerName = NameInputController.getCurrentName();

    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml");
    }

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlayerName()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));
        restRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        restNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlayerName()));
        restTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));

        fetchDataAndPopulateTableViews();
    }

    private void fetchDataAndPopulateTableViews() {
        List<Player> playerList = new ArrayList<>();
        Player currentPlayer = new Player(playerName, endTime);
        playerList.add(currentPlayer);

        // Sort the playerList based on the endTime (time)
        playerList.sort(Comparator.comparing(Player::getEndTime));

        // Create an ObservableList from the player list
        ObservableList<Player> observablePlayerList = FXCollections.observableArrayList(playerList);

        // Set the items in the TableView
        tableView.setItems(observablePlayerList);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerTime(String endTime) {
        this.endTime = endTime;
    }

    public void showGamePage(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml");
    }

    public void showBonusPage(ActionEvent actionEvent) {
        //TODO
    }

    public void showHomeScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml");
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
}
