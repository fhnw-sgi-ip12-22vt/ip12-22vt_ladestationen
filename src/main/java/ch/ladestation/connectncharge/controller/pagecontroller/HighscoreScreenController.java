package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighscoreScreenController {
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


    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
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
        List<Player> players = readPlayerDataFromFile("players.txt");
        players.sort(Comparator.comparingInt(player -> Integer.parseInt(player.getScore())));

        ObservableList<Player> topPlayers = FXCollections.observableArrayList();
        ObservableList<Player> restPlayers = FXCollections.observableArrayList();

        if (players.size() > 5) {
            topPlayers.addAll(players.subList(0, 5));
            restPlayers.addAll(players.subList(5, players.size()));
        } else {
            topPlayers.addAll(players);
        }

        tableView.setItems(topPlayers);
        restTableView.setItems(restPlayers);
    }

    private List<Player> readPlayerDataFromFile(String filePath) {
        List<Player> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    int rank = Integer.parseInt(data[0]);
                    String playerName = data[1];
                    String score = data[2];
                    Player player = new Player(rank, playerName, score);
                    players.add(player);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return players;
    }

    public void showGamePage(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }

    public void showBonusPage(ActionEvent actionEvent) {
        //TODO
    }

    public void showHomeScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
                (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
    }
}
