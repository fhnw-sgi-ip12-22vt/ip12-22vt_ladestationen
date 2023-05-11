package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.PageController;
import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Player;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
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
    private TableView restTableView;
    @FXML
    private TableColumn restRankColumn;
    @FXML
    private TableColumn restNameColumn;
    @FXML
    private TableColumn restTimeColumn;
    private static final String PLAYER_PATH = "/textfiles/highscore/player.txt";
    private static final int PLAYER_PLACE_TOP = 5;

    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css");
    }

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        restRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        restNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        restTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Fetch data from the text file and populate the TableViews
        fetchDataAndPopulateTableViews();
    }

    private void fetchDataAndPopulateTableViews() {
        /*System.out.println(String.valueOf(AppStarter.class.getResource(PLAYER_PATH)));
        List<Player> players = TextFileReader.readPlayerDataFromFile(
            String.valueOf(HighscoreScreenController.class.getResource(PLAYER_PATH)));
        players.stream().forEach(System.out::println);*/
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1", "0:05:00"));
        players.add(new Player("Player141", "1:00:00"));
        players.add(new Player("Player3", "1:70:00"));
        players.add(new Player("Player2", "0:05:45"));
        players.add(new Player("Player89", "1:40:00"));
        players.add(new Player("Player11", "0:55:58"));
        players.stream().forEach(System.out::println);
        //players.sort(Comparator.comparingInt(player -> Integer.parseInt(player.getScore())));

        ObservableList<Player> topPlayers =
            FXCollections.observableArrayList(new Player("Player1", "0:05:00"), new Player("Player141", "1:00:00"));
        ObservableList<Player> restPlayers =
            FXCollections.observableArrayList(new Player("Player11", "0:55:58"), new Player("Player89", "1:40:00"));

        if (players.size() > PLAYER_PLACE_TOP) {
            topPlayers.addAll(players.subList(0, PLAYER_PLACE_TOP));
            restPlayers.addAll(players.subList(PLAYER_PLACE_TOP, players.size()));
        } else {
            topPlayers.addAll(players);
        }

        tableView.setItems(topPlayers);
        restTableView.setItems(restPlayers);
    }

    public void showGamePage(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css");
    }

    public void showBonusPage(ActionEvent actionEvent) {
        //TODO
    }

    public void showHomeScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css");
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
