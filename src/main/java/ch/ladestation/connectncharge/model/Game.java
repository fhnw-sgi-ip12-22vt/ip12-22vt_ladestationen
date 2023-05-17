package ch.ladestation.connectncharge.model;

import ch.ladestation.connectncharge.util.mvcbase.ObservableArray;
import ch.ladestation.connectncharge.util.mvcbase.ObservableValue;
import com.github.mbelling.ws281x.Color;


public class Game {
    public static final String HOUSE_FLAG = "H";
    public final ObservableArray<Edge> solution = new ObservableArray<>(new Edge[0]);
    public final ObservableArray<Edge> activatedEdges = new ObservableArray<>(new Edge[0]);
    public final ObservableArray<Node> terminals = new ObservableArray<>(new Node[0]);
    public final ObservableValue<Integer> currentScore = new ObservableValue<>(0);
    public final ObservableValue<Boolean> gameStarted = new ObservableValue<>(false);
    public Edge tippEdge = null;
    public final ObservableValue<Boolean> isTippOn = new ObservableValue<>(false);

    public final Hint hintPickEdge = new Hint("Diese leuchtende Kante muss du noch auswählen!", Color.ORANGE);
    public final Hint hintHasCycle = new Hint("Achtung du hast einen Kreis! Entferne eine Kante!", Color.RED);
    public final Hint hintRemoveEdge = new Hint("Entferne deine vorherige Kante!", Color.GRAY);

}
