package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.Edge;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Node;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;

import java.util.Arrays;

public class ApplicationController extends ControllerBase<Game> {
    public ApplicationController(Game model) {
        super(model);
        model.activatedEdges.onChange((oldValue, newValue) -> {
            updateScore(Arrays.stream(newValue).mapToInt(Edge::getCost).sum());
        });
    }

    public void edgePressed(Edge edge) {
        if (!edge.isOn()) {
            edge.on();
            Edge[] oldValues = model.activatedEdges.getValues();
            Edge[] newValues = new Edge[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[newValues.length - 1] = edge;
            setValues(model.activatedEdges, newValues);
        } else {
            edge.off();
            Edge[] oldValues = model.activatedEdges.getValues();
            Edge[] newValues = Arrays.stream(oldValues).filter(curr -> curr != edge).toArray(Edge[]::new);
            setValues(model.activatedEdges, newValues);
        }


    }


    public void updateScore(int score) {
        setValue(model.currentScore, score);
    }

    public void setTerminals(Node[] terms) {
        setValues(model.terminals, terms);
    }
}
