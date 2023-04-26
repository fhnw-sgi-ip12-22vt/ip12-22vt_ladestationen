package ch.ladestation.connectncharge.pui;

import ch.ladestation.connectncharge.pui.ws281x.Color;
import ch.ladestation.connectncharge.pui.ws281x.LedStrip;
import com.pi4j.util.Console;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraction of a section of the LED-Strip and its button(s)
 *
 * Can be used for both Edges and nodes and is only intended to provide an
 * abstract API to the hardware.
 */
public class Segment extends Component {
    /**
     * The flag that is used in the CSV file to indicated that a row corresponds to a house
     */
    public static final String HOUSE_FLAG = "H";
    /**
     * The ledstrip this edge is part of.
     */
    private final LedStrip strip;
    /**
     * The color this segment should display
     */
    private Color color = Color.BLUE;

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
    private final int cOOLDOWNMS = 100;
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
    public Segment(LedStrip strip, MCP23S17.PinView[] interruptPins, int startIndex, int endIndex) {
        addInterruptPins(interruptPins);
        this.strip = strip;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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
                logInfo("toggled pin " + pin.name());
            });
        }
    }

    /**
     * same constructor as the previous one but with only one interrupt pin.
     *
     * @param strip        the strip of which this edge is a part.
     * @param interruptPin the pin on which to attach toggle on interrupt
     * @param startIndex   the start pixel of the edge.
     * @param endIndex     the end pixel of the edge
     */
    public Segment(LedStrip strip, MCP23S17.PinView interruptPin, int startIndex, int endIndex) {
        this(strip, new MCP23S17.PinView[] {interruptPin}, startIndex, endIndex);
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
                    strip.setPixel(i, 0, 0, 0);
                }
            } else {
                for (int i = startIndex; i <= endIndex; ++i) {
                    strip.setPixel(i, color);
                }
            }
            isOn = !isOn;
            strip.render();
        }
    }

    /**
     * Factory method to arbitrarily assign button pins and Strip segments to one another
     * based on a CSV file
     *
     * @param console for logging
     * @param strip for controlling the LED-Strip
     * @param pins All the buttons that will be assigned
     * @param file the file that stored the assignments
     * @return a list of all the correctly created segments
     */
    public static List<Segment> createSegemntsAccordingToCSV(Console console, LedStrip strip,
                                                             ArrayList<ArrayList<MCP23S17.PinView>> pins,
                                                             InputStream file) {

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            console.println("Exception when reading CSV: " + e.getMessage());
        }

        int runningTotal = 0;
        var retSegments = new ArrayList<Segment>();
        for (int i = 1; i < records.size(); i++) {
            var record = records.get(i);
            int startIndex = runningTotal;
            runningTotal += Integer.parseInt(record.get(1));
            int endIndex = runningTotal - 1;

            Segment segment;
            if (record.get(2).equals(HOUSE_FLAG)) {
                segment = new Segment(strip, Color.WHITE, startIndex, endIndex);
            } else {
                int chip = Integer.parseInt(record.get(2));
                int pin = Integer.parseInt(record.get(3));
                segment = new Segment(strip, pins.get(chip).get(pin), startIndex, endIndex);
            }
            retSegments.add(segment);
        }
        return retSegments;
    }

}
