package com.example;

public class Edge {
    /**
     * The ledstrip this edge is part of.
     */
    private LEDStrip strip;

    /**
     * The start pixel of this edge
     */
    private int startIndex;

    /**
     * The end pixel of this edge
     */
    private int endIndex;

    /**
     * Whether this edge is shining
     */
    private boolean isOn = false;

    /**
     * Basic constructor for the {@code Edge} class
     * @param strip the strip of which this edge is a part.
     * @param startIndex the start pixel of the edge.
     * @param endIndex the end pixel of the edge
     */
    public Edge(LEDStrip strip, int startIndex, int endIndex){
        this.strip = strip;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * Toggle this edge's state. Synchronized on strip for thread safety.
     */
    public void toggle(){
        synchronized (strip) {
            if (isOn) {
                for (int i = startIndex; i <= endIndex; ++i) {
                    strip.setPixelColor(i, 0);
                }
            } else {
                for (int i = startIndex; i <= endIndex; ++i) {
                    strip.setPixelColor(i, LEDStrip.PixelColor.PURPLE);
                }
            }
            strip.render();
            isOn = ! isOn;
        }

    }

}
