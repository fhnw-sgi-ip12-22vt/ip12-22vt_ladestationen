package ch.ladestation.connectncharge.model;

public class Player {
    private String playerName;
    private String time;

    public Player(String playerName, String time) {
        this.playerName = playerName;
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTime() {
        return time;
    }
}
