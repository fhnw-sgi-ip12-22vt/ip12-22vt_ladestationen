package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.Edge;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Node;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;

import java.time.Duration;
import java.util.Arrays;
import java.util.logging.Logger;

public class ApplicationController extends ControllerBase<Game> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private Edge blinkingEdge;
    private Node[] terms;

    public ApplicationController(Game model) {
        super(model);
        model.activatedEdges.onChange((oldValue, newValue) -> {
            updateScore(Arrays.stream(newValue).mapToInt(Edge::getCost).sum());
        });
    }

    public void edgePressed(Edge edge) {
        if (!model.gameStarted.getValue()) {
            if (edge == this.blinkingEdge) {
                setValue(model.gameStarted, true);
                setValues(model.terminals, this.terms);
            }
            return;
        }
        toggleEdge(edge);
    }

    private void toggleEdge(Edge edge) {
        if (!edge.isOn()) {
            activateEdge(edge);
        } else {
            deactivateEdge(edge);
        }
    }

    public void edgeToggledByApp(Edge edge) {
        toggleEdge(edge);
    }

    private void activateEdge(Edge edge) {
        edge.on();
        Edge[] oldValues = model.activatedEdges.getValues();
        Edge[] newValues = new Edge[oldValues.length + 1];
        System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
        newValues[newValues.length - 1] = edge;
        setValues(model.activatedEdges, newValues);
    }

    private void deactivateEdge(Edge edge) {
        edge.off();
        Edge[] oldValues = model.activatedEdges.getValues();
        Edge[] newValues = Arrays.stream(oldValues).filter(curr -> curr != edge).toArray(Edge[]::new);
        setValues(model.activatedEdges, newValues);
    }

    public void startBlinkingEdge(Edge edge) {
        if (model.gameStarted.getValue()) {
            return;
        }
        this.blinkingEdge = edge;
        async(() -> {
            edgeToggledByApp(edge);
        });
        pauseExecution(Duration.ofSeconds(1));
        async(() -> startBlinkingEdge(edge));
    }


    public void updateScore(int score) {
        setValue(model.currentScore, score);
    }

    public void setTerminals(Node[] terms) {
        if (!model.gameStarted.getValue()) {
            this.terms = terms;
            return;
        }
        setValues(model.terminals, terms);
    }
}
