package ch.ladestation.connectncharge.model;

import ch.ladestation.connectncharge.util.mvcbase.ObservableArray;



public class Game {
    public static final String HOUSE_FLAG = "H";
    public final ObservableArray<Segment> activatedEdges = new ObservableArray<>(new Segment[0]);

}
