package ch.ladestation.connectncharge.model;

public class Player {
    private int rank;
    private String playerName;
    private String score;

    public Player(int rank, String playerName, String score) {
        this.rank = rank;
        this.playerName = playerName;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getScore() {
        return score;
    }
}
