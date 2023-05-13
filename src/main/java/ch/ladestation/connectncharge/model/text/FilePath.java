package ch.ladestation.connectncharge.model.text;

public enum FilePath {
    LOADINGPAGE("/ch/ladestation/connectncharge/loadingpage.fxml"),
    HOMEPAGE("/ch/ladestation/connectncharge/homepage.fxml"),
    EDGECLICKSCREEN("/ch/ladestation/connectncharge/edgeclickscreen.fxml"),
    COUNTDOWNPAGE("/ch/ladestation/connectncharge/countdownpage.fxml");

    private final String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public FilePath getNext() {
        if (this.ordinal() == values().length - 1) {
            return values()[0];
        }
        return values()[this.ordinal() + 1];
    }

    public FilePath getPrevious() {
        if (this.ordinal() == 0) {
            return values()[values().length - 1];
        }
        return values()[this.ordinal() - 1];
    }
}
