package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.Edge;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Segment;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;

import java.util.Arrays;

public class ApplicationController extends ControllerBase<Game> {
    public ApplicationController(Game model) {
        super(model);
    }

    public void edgePressed(Edge edge) {
        segmentToggled(edge);
    }

    public void segmentToggled(Segment segment) {
        if (!segment.isOn()) {
            segment.on();
            Segment[] oldValues = model.activatedEdges.getValues();
            Segment[] newValues = new Segment[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[newValues.length - 1] = segment;
            setValues(model.activatedEdges, newValues);
        } else {
            segment.off();
            Segment[] oldValues = model.activatedEdges.getValues();
            Segment[] newValues =
                    Arrays.stream(oldValues)
                            .filter(curr -> curr != segment)
                            .toArray(Segment[]::new);
            setValues(model.activatedEdges, newValues);
        }
    }
}
