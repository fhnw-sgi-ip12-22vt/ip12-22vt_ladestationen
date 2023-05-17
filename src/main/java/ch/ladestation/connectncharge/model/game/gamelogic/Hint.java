package ch.ladestation.connectncharge.model.game.gamelogic;

import com.github.mbelling.ws281x.Color;

public enum Hint {

    HINT_PICK_EDGE("Diese leuchtende Kante muss du noch auswählen!", Color.ORANGE),
    HINT_CYCLE("Achtung du hast einen Kreis! Entferne eine Kante!", Color.RED),
    HINT_REMOVE_EDGE("Entferne die Leuchtende Kante!", Color.GRAY),
    HINT_EMPTY_HINT("EMPTY_HINT", Color.BLACK);

    private String text;
    private Color color;

    Hint(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
