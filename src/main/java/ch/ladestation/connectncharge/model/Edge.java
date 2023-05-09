package ch.ladestation.connectncharge.model;

import com.github.mbelling.ws281x.Color;

public class Edge extends Segment {

    private int cost;
    private int fromNodeId;
    private int toNodeId;

    private Node fromNode;
    private Node toNode;

    public Edge(int index, int startIndex, int endIndex, int cost, int fromNodeId, int toNodeId) {
        super(index, startIndex, endIndex, Color.GREEN);
        this.cost = cost;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
    }

    public int getCost() {
        return cost;
    }

    public int getFromNodeId() {
        return fromNodeId;
    }

    public int getToNodeId() {
        return toNodeId;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }
}
