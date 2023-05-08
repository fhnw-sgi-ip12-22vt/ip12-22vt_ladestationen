package ch.ladestation.connectncharge.model;

import ch.ladestation.connectncharge.services.file.CSVReader;
import ch.ladestation.connectncharge.util.mvcbase.ObservableArray;

import java.util.*;

public class Game {
    public static final String HOUSE_FLAG = "H";
    public final ObservableArray<Segment> activatedEdges = new ObservableArray<>(new Segment[0]);

}
