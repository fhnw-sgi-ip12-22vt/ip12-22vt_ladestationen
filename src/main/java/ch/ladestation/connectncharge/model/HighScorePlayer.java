package ch.ladestation.connectncharge.model;

public class HighScorePlayer {
    private int rank;
    private Player player;

    public HighScorePlayer(final int rank, final Player player) {
        this.rank = rank;
        this.player = player;
    }

    public int getRank() {
        return rank;
    }

    public Player getPlayer() {
        return player;
    }


}
