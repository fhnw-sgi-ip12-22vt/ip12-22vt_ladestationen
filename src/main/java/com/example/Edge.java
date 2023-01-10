package com.example;

public class Edge extends Component{
    /**
     * The ledstrip this edge is part of.
     */
    private final LEDStrip strip;
    /**
     * The color this segment should display
     */
    private int color = LEDStrip.PixelColor.PURPLE;

    /**
     * The start pixel of this edge
     */
    private final int startIndex;

    /**
     * The end pixel of this edge
     */
    private final int endIndex;

    /**
     * Whether this edge is shining
     */
    private boolean isOn = false;
    /**
     * how many milliseconds to cooldown after toggle
     */
    private final int COOLDOWN_MS = 100;
    /**
     * whether the edge has toggled recently and further interrupts will be ignored
     */
    private boolean inCooldown = false;

    /**
     * prevent multiple threads writing cooldown at the same time
     */
    private final Object cooldownWriteLock = new Object();
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
     * Constructor for the {@code Edge} class that expects one or multiple interrupt enabled input pins
     * that can be used to toggle the edge.
     *
     * @param strip the strip of which this edge is a part.
     * @param interruptPins the pin(s) on which to attach toggle on interrupt
     * @param startIndex the start pixel of the edge.
     * @param endIndex the end pixel of the edge
     */
    public Edge(LEDStrip strip, MCP23S17.PinView[] interruptPins, int startIndex, int endIndex){
        for(int i = 0; i < interruptPins.length; ++i){
            interruptPins[i].addListener((boolean capturedValue, MCP23S17.Pin pin)->{
                if(!capturedValue || inCooldown) {
                    return;
                }
                toggle();
                synchronized (cooldownWriteLock){
                    inCooldown = true;
                }
                delay(COOLDOWN_MS);
                synchronized (cooldownWriteLock){
                    inCooldown = false;
                }
            });
        }
        this.strip = strip;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * same constructor as the previous one but with only one interrupt pin.
     *
     *@param strip the strip of which this edge is a part.
     *@param interruptPin the pin on which to attach toggle on interrupt
     *@param startIndex the start pixel of the edge.
     *@param endIndex the end pixel of the edge
     */
    public Edge(LEDStrip strip, MCP23S17.PinView interruptPin, int startIndex, int endIndex){
        this(strip, new MCP23S17.PinView[] {interruptPin}, startIndex, endIndex);
    }
    /**
     * same constructor as the basic constructor, but with a color for the segment
     *
     *@param strip the strip of which this edge is a part.
     *@param color the segment's color of type {@link com.example.LEDStrip.PixelColor}
     *@param startIndex the start pixel of the edge.
     *@param endIndex the end pixel of the edge
     */
    public Edge(LEDStrip strip, int color, int startIndex, int endIndex){
        this(strip, startIndex, endIndex);
        this.color = color;
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
                    strip.setPixelColor(i, color);
                }
            }
            isOn = ! isOn;
        }

    }

}
