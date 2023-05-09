package ch.ladestation.connectncharge.model;

import ch.ladestation.connectncharge.util.mvcbase.ObservableArray;



public class Game {
    public static final String HOUSE_FLAG = "H";
    public final ObservableArray<Edge> activatedEdges = new ObservableArray<>(new Edge[0]);
    public final ObservableArray<Node> terminals = new ObservableArray<>(new Node[0]);

}
