package ch.ladestation.connectncharge.model;

public class Player {
    private String playerName;
    private String endTime;

    public Player(String playerName, String endTime) {
        this.playerName = playerName.replaceAll(" ", "");
        this.endTime = endTime.replaceAll(" ", "");
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
