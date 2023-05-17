package ch.ladestation.connectncharge.model;

import com.github.mbelling.ws281x.Color;

public class Hint {
    private String text;
    private Color color;

    public Hint(String text, Color color) {
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
