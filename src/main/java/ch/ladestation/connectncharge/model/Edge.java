package ch.ladestation.connectncharge.model;

import com.github.mbelling.ws281x.Color;

public class Edge extends Segment {

    private int cost;

    public Edge(int index, int startIndex, int endIndex, int cost) {
        super(index, startIndex, endIndex, Color.GREEN);
        this.cost = cost;
    }

}
