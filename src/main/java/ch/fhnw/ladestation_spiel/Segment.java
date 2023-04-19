package ch.fhnw.ladestation_spiel;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStrip;
import com.pi4j.util.Console;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Segment extends Component {

    public interface SegmentStateChange {
        void onStateChange(int deltaCost);
    }
    /**
     * the callback if this edge changed state
     */
    private SegmentStateChange change;
    /**
     * The ledstrip this edge is part of.
     */
    private final LedStrip strip;
    /**
     * The color this segment should display
     */
    private Color color = Color.GREEN;

    /**
     * The start pixel of this edge
     */
    private final int startIndex;

    /**
     * The end pixel of this edge
     */
    private final int endIndex;
    /**
     * Cost, if this segment is an edge, 0 otherwise
     */
    private int cost;
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
     *
     * @param strip      the strip of which this edge is a part.
     * @param startIndex the start pixel of the edge.
     * @param endIndex   the end pixel of the edge
     */
    public Segment(LedStrip strip, int startIndex, int endIndex) {
        this.strip = strip;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * Constructor for the {@code Edge} class that expects one or multiple interrupt enabled input pins
     * that can be used to toggle the edge.
     *
     * @param strip         the strip of which this edge is a part.
     * @param interruptPins the pin(s) on which to attach toggle on interrupt
     * @param startIndex    the start pixel of the edge.
     * @param endIndex      the end pixel of the edge
     */
    public Segment(LedStrip strip, MCP23S17.PinView[] interruptPins, int startIndex, int endIndex, int cost) {
        addInterruptPins(interruptPins);
        this.strip = strip;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.cost = cost;
    }

    /**
     * Adds one or more {@link MCP23S17.PinView} objects that are expected to be
     * interrupt-enabled, pulled-up input pins.
     * The method will attach state-change listeners to those pins that toggle the edge.
     *
     * @param interruptPins the {@link MCP23S17.PinView} objects to which state-change listeners will be attached
     */
    private void addInterruptPins(MCP23S17.PinView[] interruptPins) {
        for (MCP23S17.PinView interruptPin : interruptPins) {
            interruptPin.addListener((boolean capturedValue, MCP23S17.Pin pin) -> {
                if (!capturedValue) {
                    return;
                }
                toggle();
                logInfo("toggled pin "+pin.name());
            });
        }
    }

    /**
     * same constructor as the previous one but with only one interrupt pin.
     *
     * @param strip        the strip of which this edge is a part.
     * @param interruptPin the pin on which to attach toggle on interrupt
     * @param startIndex   the start pixel of the edge.
     * @param cost         the cost if this segment represents an edge
     * @param callback     the callback to update total cost.
     * @param endIndex     the end pixel of the edge
     */
    public Segment(LedStrip strip, MCP23S17.PinView interruptPin, int startIndex, int endIndex, int cost, SegmentStateChange callback) {
        this(strip, new MCP23S17.PinView[]{interruptPin}, startIndex, endIndex, cost);
        this.change = callback;
    }

    /**
     * same constructor as the basic constructor, but with a color for the segment
     *
     * @param strip      the strip of which this edge is a part.
     * @param color      the segment's color of type {@link Color}
     * @param startIndex the start pixel of the edge.
     * @param endIndex   the end pixel of the edge
     */
    public Segment(LedStrip strip, Color color, int startIndex, int endIndex) {
        this(strip, startIndex, endIndex);
        this.color = color;
    }

    /**
     * Toggle this edge's state. Synchronized on strip for thread safety.
     */
    public void toggle() {
        synchronized (strip) {
            if (isOn) {
                for (int i = startIndex; i <= endIndex; ++i) {
                    strip.setPixel(i, 0,0,0);
                }
            } else {
                for (int i = startIndex; i <= endIndex; ++i) {
                    strip.setPixel(i, color);
                }
            }
            isOn = !isOn;
            if(this.change != null){
                int cost = (isOn ? 1 : -1) * this.cost;
                this.change.onStateChange(cost);
            }
            strip.render();
        }
    }

    public static List<Segment> createSegemntsAccordingToCSV(Console console, LedStrip strip, ArrayList<ArrayList<MCP23S17.PinView>> pins, InputStream file, SegmentStateChange changeCallBack) {
        var SEMICOLON_DELIMITER = ";";

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(SEMICOLON_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e){
            console.println("Exception when reading CSV: "+e.getMessage());
        }

        int runningTotal = 0;
        var retSegments = new ArrayList<Segment>();
        for (int i = 1; i < records.size(); i++) {
            var record = records.get(i);
            int startIndex = runningTotal;
            runningTotal += Integer.parseInt(record.get(1));
            int endIndex = runningTotal-1;

            Segment segment;
            if(record.get(2).equals("H")) {
                segment = new Segment(strip, Color.BLUE, startIndex, endIndex);
            }else{
                int chip = Integer.parseInt(record.get(2));
                int pin = Integer.parseInt(record.get(3));
                int cost = Integer.parseInt(record.get(4));
                segment = new Segment(strip, pins.get(chip).get(pin), startIndex, endIndex, cost, changeCallBack);
            }
            retSegments.add(segment);
        }
        return retSegments;
    }

}
