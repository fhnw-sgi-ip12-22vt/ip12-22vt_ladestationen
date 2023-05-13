package ch.ladestation.connectncharge.controller.pagecontroller.admin;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.controller.pagecontroller.PageController;
import ch.ladestation.connectncharge.controller.pagecontroller.StageHandler;
import ch.ladestation.connectncharge.model.game.gameinfo.HighScorePlayer;
import ch.ladestation.connectncharge.model.game.gameinfo.HighScoreTable;
import ch.ladestation.connectncharge.model.game.gameinfo.Player;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.model.text.FilePath;
import ch.ladestation.connectncharge.services.file.TextFileEditor;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import ch.ladestation.connectncharge.util.mvcbase.ViewMixin;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminNormalLeaderboard implements ViewMixin<Game, ControllerBase<Game>>, PageController, Initializable {
    @FXML
    private Button backButton;
    @FXML
    private Button trashButton;
    @FXML
    private Button chooseAllButton;
    @FXML
    private Button unchooseAllButton;
    @FXML
    private TableView<HighScorePlayer> tableView;
    @FXML
    private TableColumn<HighScorePlayer, Integer> rankColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> nameColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> timeColumn;
    @FXML
    private TableView<HighScorePlayer> restTableView;
    @FXML
    private TableColumn<HighScorePlayer, Integer> restRankColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> restNameColumn;
    @FXML
    private TableColumn<HighScorePlayer, String> restTimeColumn;

    List<Player> playerList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //playerList = TextFileEditor.readPlayerDataFromFile(FilePath.TEXT_FILE_PLAYER_PATH_LINUX.getFilePath());
        playerList = TextFileEditor.readPlayerDataFromFile("src/main/resources/textfiles/highscore/player.txt");
        HighScoreTable.initColumns(rankColumn, nameColumn, timeColumn);
        HighScoreTable.initColumns(restRankColumn, restNameColumn, restTimeColumn);
        HighScoreTable.initRowHeight(tableView);
        HighScoreTable.initRowHeight(restTableView);
        HighScoreTable.populateTableViews(playerList, tableView, restTableView);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        restTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        StageHandler.openStage(FilePath.ADMINHOMEPAGE.getFilePath());
    }

    @FXML
    private void handleChooseAllButton(ActionEvent event) {
        tableView.getSelectionModel().selectAll();
        restTableView.getSelectionModel().selectAll();

        chooseAllButton.getStyleClass().remove("leaderboard-choose-button");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button-selected");

        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button-selected");
        unchooseAllButton.getStyleClass().add("leaderboard-un-choose-button");
    }

    @FXML
    private void handleUnChooseAllButton(ActionEvent event) {
        tableView.getSelectionModel().clearSelection();
        restTableView.getSelectionModel().clearSelection();
        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button");
        unchooseAllButton.getStyleClass().add("leaderboard-un-choose-button-selected");

        chooseAllButton.getStyleClass().remove("leaderboard-choose-button-selected");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button");
    }

    @FXML
    private void handleTrashButton(ActionEvent event) {
        ObservableList<HighScorePlayer> selectedRows = tableView.getSelectionModel().getSelectedItems();
        for (HighScorePlayer rowData : selectedRows) {
            playerList.remove(rowData.getPlayer());
        }
        selectedRows = restTableView.getSelectionModel().getSelectedItems();
        for (HighScorePlayer rowData : selectedRows) {
            playerList.remove(rowData.getPlayer());
        }
        //HighScoreTable.editTableViews(playerList, FilePath.WHOLE_TEXT_FILE_PLAYER_PATH_LINUX.getFilePath());
        HighScoreTable.editTableViews(playerList, "src/main/resources/textfiles/highscore/player.txt");
        HighScoreTable.populateTableViews(playerList, tableView, restTableView);

        // Rollback the CSS classes
        chooseAllButton.getStyleClass().remove("leaderboard-choose-button-selected");
        chooseAllButton.getStyleClass().add("leaderboard-choose-button");

        unchooseAllButton.getStyleClass().remove("leaderboard-un-choose-button-selected");
        unchooseAllButton.getStyleClass().add("leaderboard-choose-button");
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
