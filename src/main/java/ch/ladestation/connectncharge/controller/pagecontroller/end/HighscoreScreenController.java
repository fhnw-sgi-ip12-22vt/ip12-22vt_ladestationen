package ch.ladestation.connectncharge.controller.pagecontroller.end;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.pagecontroller.PageController;
import ch.ladestation.connectncharge.controller.pagecontroller.StageHandler;
import ch.ladestation.connectncharge.controller.pagecontroller.middle.GamePageController;
import ch.ladestation.connectncharge.model.game.gameinfo.HighScorePlayer;
import ch.ladestation.connectncharge.model.game.gameinfo.Player;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.model.text.FilePath;
import ch.ladestation.connectncharge.services.file.TextFileEditor;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HighscoreScreenController implements ViewMixin<Game, ControllerBase<Game>>, PageController {
    @FXML
    private Button btnPlayAgain;
    @FXML
    private Button btnBonus;
    @FXML
    private ImageView imgHome;
    @FXML
    private TableView<HighScorePlayer> tableView;
    @FXML
    private TableColumn<HighScorePlayer, Integer> rankColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> nameColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> timeColumn;
    @FXML
    private ScrollPane restHighscore;
    @FXML
    private TableView<HighScorePlayer> restTableView;
    @FXML
    private TableColumn<HighScorePlayer, Integer> restRankColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> restNameColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> restTimeColumn;

    private static final int PLAYER_PLACE_TOP = 5;
    private String endTime = String.valueOf(GamePageController.getPublicEndTime());
    private String playerName = NameInputController.getCurrentName();


    @FXML
    public void initialize() {
        initColumns(rankColumn, nameColumn, timeColumn);
        initColumns(restRankColumn, restNameColumn, restTimeColumn);
        fetchDataAndPopulateTableViews();
    }

    private void initColumns(TableColumn<HighScorePlayer, Integer> rank, TableColumn<HighScorePlayer, String> name,
                             TableColumn<HighScorePlayer, String> time) {
        rank.setCellValueFactory(cellData -> {
            int rankValue = cellData.getValue().getRank();
            return new SimpleIntegerProperty(rankValue).asObject();
        });
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlayer().getPlayerName()));
        time.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlayer().getEndTime()));
    }

    private void fetchDataAndPopulateTableViews() {
        List<Player> playerList =
            TextFileEditor.readPlayerDataFromFile(FilePath.TEXT_FILE_PLAYER_PATH_LINUX.getFilePath());
        Player currentPlayer = new Player(playerName, endTime);
        playerList.add(currentPlayer);

        // Sort the playerList based on the endTime (time)
        playerList.sort(Comparator.comparing(Player::getEndTime));
        List<HighScorePlayer> highScorePlayers = Stream.iterate(0, i -> i + 1).limit(playerList.size())
            .map(number -> new HighScorePlayer(number.intValue() + 1, playerList.get(number)))
            .collect(Collectors.toList());

        // Create an ObservableList from the player list
        ObservableList<HighScorePlayer> topPlayers;
        ObservableList<HighScorePlayer> restPlayers;

        if (highScorePlayers.size() > PLAYER_PLACE_TOP) {
            topPlayers = FXCollections.observableArrayList(highScorePlayers.subList(0, PLAYER_PLACE_TOP));
            restPlayers =
                FXCollections.observableArrayList(highScorePlayers.subList(PLAYER_PLACE_TOP, highScorePlayers.size()));
            restTableView.setItems(restPlayers);
        } else {
            topPlayers = FXCollections.observableArrayList(highScorePlayers);
        }

        tableView.setItems(topPlayers);

        //writes the new data
        TextFileEditor.writeTextFile(FilePath.WHOLE_TEXT_FILE_PLAYER_PATH_LINUX.getFilePath(),
            playerList.stream().map(val -> val.getPlayerName() + "," + val.getEndTime()).peek(p -> {
                System.out.println(p);
            }).collect(Collectors.toList()));
    }

    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage(FilePath.HIGHSCORE.getFilePath());
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerTime(String endTime) {
        this.endTime = endTime;
    }

    public void showGamePage(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage(FilePath.GAMEPAGE.getFilePath());
    }

    public void showBonusPage(ActionEvent actionEvent) {
        //TODO
    }

    public void showHomeScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage(FilePath.HOMEPAGE.getFilePath());
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
