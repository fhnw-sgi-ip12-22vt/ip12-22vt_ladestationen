package ch.ladestation.connectncharge.model.game.gamelogic;

import com.github.mbelling.ws281x.Color;

public enum Hint {

    HINT_PICK_EDGE("Diese orange leuchtende Kante ist in der Lösung enthalten.", Color.ORANGE),
    HINT_CYCLE("Du hast ein Kreis gebildet.\n"
        + "Das heisst, dass dadurch mindestens eine dieser Kanten überflüssig ist und du diese entfernen kannst.",
        Color.RED),
    HINT_REMOVE_EDGE("Diese orange leuchtende Kante ist nicht in der Lösung enthalten.", Color.GRAY),
    HINT_EMPTY_HINT("EMPTY_HINT", Color.BLACK),

    HINT_SOLUTION_NOT_FOUND(
        "Du hast zwar alle Häuser miteinander verbunden. "
            + "Jedoch ist dies noch nicht das kostengünstigste Verteilungsnetz.",
        Color.GRAY),

    HINT_NOT_ALL_NODES_CONNECTED("Du hast noch nicht alle Häuser miteinander verbunden.", Color.GRAY);

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
