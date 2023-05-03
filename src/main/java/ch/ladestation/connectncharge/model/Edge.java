package ch.ladestation.connectncharge.model;

import com.github.mbelling.ws281x.LedStrip;

import ch.ladestation.connectncharge.pui.MCP23S17.PinView;

public class Edge extends Segment {

    public Edge(int index, LedStrip strip, PinView pinView, int startIndex, int endIndex, int cost,
            SegmentStateChange changeCallBack) {
        super(index, strip, pinView, startIndex, endIndex, cost, changeCallBack);
    }

}
