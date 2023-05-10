package ch.ladestation.connectncharge.controller.pagecontroller;

import ch.ladestation.connectncharge.controller.StageHandler;
import ch.ladestation.connectncharge.model.Player;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighscoreScreenController {
    @FXML
    public Button btnPlayAgain;
    @FXML
    public Button btnBonus;
    @FXML
    public ImageView imgHome;
    @FXML
    public TableView<Player> tableView;
    @FXML
    public TableColumn<Player, Integer> rankColumn;
    @FXML
    public TableColumn<Player, String> nameColumn;
    @FXML
    public TableColumn<Player, String> timeColumn;


    @FXML
    public void showHighscorePage(ActionEvent event) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/highscore.fxml", "/css/style.css",
                (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        List<Player> players = new ArrayList<>();

        players.add(new Player(2, "Emma", "1min 45sec"));
        players.add(new Player(11, "Matthew", "1min 51sec"));
        players.add(new Player(3, "David", "2min 44sec"));
        players.add(new Player(4, "Sophia", "1min 55sec"));
        players.add(new Player(9, "Daniel", "2min 5sec"));
        players.add(new Player(5, "Michael", "2min 12sec"));
        players.add(new Player(6, "Olivia", "2min 30sec"));
        players.add(new Player(20, "Charlotte", "2min 8sec"));
        players.add(new Player(7, "James", "1min 58sec"));
        players.add(new Player(8, "Emily", "2min 19sec"));
        players.add(new Player(18, "Aria", "2min 14sec"));
        players.add(new Player(10, "Ava", "2min 37sec"));
        players.add(new Player(1, "John", "1min 23sec"));
        players.add(new Player(12, "Lily", "2min 41sec"));
        players.add(new Player(13, "Benjamin", "1min 39sec"));
        players.add(new Player(14, "Mia", "2min 26sec"));
        players.add(new Player(15, "William", "2min 10sec"));
        players.add(new Player(16, "Sophia", "1min 55sec"));
        players.add(new Player(17, "Lucas", "2min 32sec"));
        players.add(new Player(19, "Oliver", "2min 49sec"));

        setPlayerData(players);
    }

    //Create a method setPlayerData and get the 5 top best one from the list given
    public void setPlayerData(List<Player> players) {
        //get the top players from the arraylist

        players.sort(Comparator.comparing(Player::getRank));

        int topPlayersCount = Math.min(5, players.size());
        tableView.setItems(FXCollections.observableArrayList(players.subList(0, topPlayersCount)));

        if (players.size() > topPlayersCount) {
            StringBuilder showcaseText = new StringBuilder();
            int showcaseStartIndex = topPlayersCount;
            int showcaseEndIndex = Math.min(topPlayersCount + 5, players.size()
            );
            if (topPlayersCount >= 11) {
                showcaseText.append("...\n");
                showcaseStartIndex = Math.max(showcaseStartIndex, topPlayersCount - 5);
            }

            for (int i = showcaseStartIndex; i < showcaseEndIndex; i++) {
                Player player = players.get(i);
                showcaseText.append(String.format("%d. %10s %10s\n", player.getRank(), player.getPlayerName(), player.getScore()));
            }

            if (players.size() > showcaseEndIndex) {
                showcaseText.append("...");
            }

            // Display the showcaseText however you want (e.g., set it to a label or print it)
            // For example, assuming you have a label with fx:id "showcaseLabel":
            // showcaseLabel.setText(showcaseText.toString());
        }
    }


    /*public void setPlayerData(List<Player> players) {
       //get the top players from the arraylist

      players.sort(Comparator.comparing(Player::getRank));

       int topPlayersCount = Math.min(5, players.size());
       tableView.setItems(FXCollections.observableArrayList(players.subList(0, topPlayersCount)));

       if (players.size() > topPlayersCount) {
           StringBuilder showcaseText = new StringBuilder();
           int showcaseStartIndex = topPlayersCount;
           int showcaseEndIndex = Math.min(topPlayersCount + 5, players.size()
           );
           if (topPlayersCount >= 11) {
               showcaseText.append("...\n");
               showcaseStartIndex = Math.max(showcaseStartIndex, topPlayersCount - 5);
           }

           for (int i = showcaseStartIndex; i < showcaseEndIndex; i++) {
               Player player = players.get(i);
               showcaseText.append(String.format("%d. %10s %10s\n", player.getRank(), player.getPlayerName(), player.getScore()));
           }

           if (players.size() > showcaseEndIndex) {
               showcaseText.append("...");
           }

           // Display the showcaseText however you want (e.g., set it to a label or print it)
           // For example, assuming you have a label with fx:id "showcaseLabel":
           // showcaseLabel.setText(showcaseText.toString());
       }
   }*/
    public void showGamePage(ActionEvent actionEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/gamepage.fxml", "/css/style.css",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow());

    }

    public void showBonusPage(ActionEvent actionEvent) {

    }

    public void showHomeScreen(MouseEvent mouseEvent) throws IOException {
        StageHandler.openStage("/ch/ladestation/connectncharge/homepage.fxml", "/css/style.css",
                (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
    }

}
