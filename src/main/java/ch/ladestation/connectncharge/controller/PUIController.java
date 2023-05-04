package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.Edge;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Segment;
import ch.ladestation.connectncharge.pui.MCP23S17;
import com.github.mbelling.ws281x.LedStrip;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.io.spi.SpiBus;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class PUIController {
    /**
     * Logger instance
     */
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final List<MCP23S17> chips;
    private final LedStrip ledStrip;
    private final Context contextPi4j;

    /**
     * the game this controller interacts with
     */
    private final Game gameInstance;

    /**
     * look-up-table that allows to get the correct edge instance
     * from a chip- and pin number.
     */
    private List<List<Edge>> chipToEdgeLUT;

    public PUIController() {
        this.contextPi4j = setupPi4j();
        this.ledStrip = setupLEDStrip();
        this.chips = setupGPIOExtensionICs();
        addInterruptsToPinViews();
        this.gameInstance = new Game();
        this.gameInstance.instanceSegments();

/////////////////////TMMMMMMMMMMMMMP
        int[] terms = {
            81,
            27,
            11,
            31,
            52,
            47,
            33,
            62,
            77,
            16,
            95,
            18,
            67};

        for (int terminal : terms) {
            toggleSegment(gameInstance.lookUpSegmentIdToSegment(terminal));
        }
        //////////////////TMPPPPPPPPPPPPPPPPPPPPPPPPPPPPP
    }

    /**
     * Will set up and initialise the LED-Strip
     *
     * @return the {@link LedStrip} object
     */
    private static LedStrip setupLEDStrip() {
        LedStrip ledStrip = new Ws281xLedStrip(
            845,
            10,
            800000,
            10,
            false,
            LedStripType.WS2811_STRIP_GRB,
            true
        );
        return ledStrip;
    }

    /**
     * Configures pins of the MCP23S17 ICs to listen for interrupts and
     * adds a listener to every single one of them that calls
     * handleEdgePressed with the correct chip no. & pin no.
     */
    private void addInterruptsToPinViews() {
        try {
            for (int i = 0; i < chips.size(); i++) {
                var pinViews = chips.get(i).getAllPinsAsPulledUpInterruptInput();
                for (var pinView : pinViews) {
                    addEdgePressListenerToPinView(i, pinView);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error when trying to configure MCP23S17 pins: " + e.getMessage());
        }
    }

    /**
     * given a chip index and a {@link MCP23S17.PinView} object, this method will add a listener to the latter
     * that calls the {@link PUIController#handleEdgePressed} method with the corresponding {@link Edge} instance.
     *
     * @param indexOfIC the index into the {@link PUIController#chips} list where the {@link MCP23S17} instance
     *                  that the {@link MCP23S17.PinView} argument belongs to is stored.
     * @param pinView   the {@link MCP23S17.PinView} object the interrupt originated from.
     */
    private void addEdgePressListenerToPinView(int indexOfIC, MCP23S17.PinView pinView) {
        pinView.addListener((state, pin) -> {
            if (!state) {
                handleEdgePressed(gameInstance.lookUpChipAndPinNumberToEdge(indexOfIC, pin.getPinNumber()));
            }
        });
    }

    /**
     * method that gets called every time someone tries to toggle an edge by pushing it down.
     * NOTE: it is only called on release of the edge.
     *
     * @param edge the instance that represents the pressed edge
     */
    private void handleEdgePressed(Edge edge) {
        toggleSegment(edge);
        logger.info("edge " + edge.getSegmentIndex() + " between "
            + edge.getFromNodeId()
            + " & "
            + edge.getToNodeId() + " was pressed");
    }

    /**
     * toggles any passed segment. Synchronized on strip for thread safety.
     *
     * @param segment the instance to be toggled
     */
    public void toggleSegment(Segment segment) {
        synchronized (ledStrip) {
            var from = segment.getStartIndex();
            var to = segment.getEndIndex();
            if (segment.isOn()) {
                for (var i = from; i <= to; ++i) {
                    ledStrip.setPixel(i, 0, 0, 0);
                }
            } else {
                for (var i = from; i <= to; ++i) {
                    ledStrip.setPixel(i, segment.getColor());
                }
            }
            segment.setOn(!segment.isOn());
            ledStrip.render();
        }
    }

    /**
     * Will set up and initialise the MCP23S17 GPIO-Extension ICs
     *
     * @return two fully configured lists of {@link MCP23S17.PinView} objects.
     * that means 2 * 16 extra GPIO Pins set as input, pulled up and interrupt enabled
     * @throws IOException when the creation of the {@link MCP23S17} objects or
     *                     gathering of the {@link MCP23S17.PinView} objects fail
     */
    private List<MCP23S17> setupGPIOExtensionICs() {
        var interruptPinConfig = DigitalInput.newConfigBuilder(contextPi4j)
            .id("interrupt0")
            .name("a MCP interrupt")
            .address(22)
            .pull(PullResistance.PULL_UP)
            .provider("pigpio-digital-input");

        var interruptPinChip0 = contextPi4j.create(interruptPinConfig);
        var interruptPinChip1 = contextPi4j.create(interruptPinConfig.address(23).id("interrupt1"));
        var interruptPinChip2 = contextPi4j.create(interruptPinConfig.address(24).id("interrupt2"));
        var interruptPinChip3 = contextPi4j.create(interruptPinConfig.address(25).id("interrupt3"));
        var interruptPinChip4 = contextPi4j.create(interruptPinConfig.address(27).id("interrupt4"));

        DigitalInput[] interruptPins = {interruptPinChip0,
            interruptPinChip1,
            interruptPinChip2,
            interruptPinChip3,
            interruptPinChip4
        };
        List<MCP23S17> interruptChips;
        try {
            interruptChips = MCP23S17.multipleNewOnSameBusWithTiedInterrupts(
                contextPi4j,
                SpiBus.BUS_1,
                interruptPins,
                5,
                true);
        } catch (IOException e) {
            throw new RuntimeException("Fatal error when instantiating MCP23S17 chips: " + e.getMessage());
        }
        return interruptChips;
    }

    @Override
    protected void finalize() throws Throwable {
        contextPi4j.shutdown();
        super.finalize();
    }

    /**
     * initialises the pi4j context
     */
    private Context setupPi4j() {
        logger.info("Hello Rasbian world !");
        var piGpio = PiGpio.newNativeInstance();
        return Pi4J.newContextBuilder()
            .noAutoDetect()
            .add(
                PiGpioDigitalInputProvider.newInstance(piGpio),
                PiGpioDigitalOutputProvider.newInstance(piGpio),
                PiGpioSpiProvider.newInstance(piGpio)
            )
            .build();
    }
}
