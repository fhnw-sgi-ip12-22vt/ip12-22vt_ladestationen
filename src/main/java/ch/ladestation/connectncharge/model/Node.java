package ch.ladestation.connectncharge.model;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStrip;

public class Node extends Segment {

    public Node(int index, LedStrip strip, Color color, int startIndex, int endIndex) {
        super(index, strip, color, startIndex, endIndex);
    }

}
