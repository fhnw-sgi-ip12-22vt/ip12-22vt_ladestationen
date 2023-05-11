package ch.ladestation.connectncharge.model;

import com.github.mbelling.ws281x.Color;

public class Node extends Segment {
    public Node(int index, int startIndex, int endIndex) {
        super(index, startIndex, endIndex, Color.BLUE);

    }
}
