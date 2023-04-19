/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw.ladestation_spiel;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStrip;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.pi4j.Pi4J;
import com.pi4j.io.spi.SpiBus;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the maven pi4j archetype
 *
 * @author luca, MNG
 */
public class Main {
    /**
     * amount of pixels in the LED-Strip
     */
    public static final int PIXEL_AMT = 99;
    /**
     * Console for logging
     */
    private static final Console console = new Console();
    /**
     * The nodes of the game, they are a segment of the LED-Strip, some of them are terminals
     */
    private static final ArrayList<Segment> nodes = new ArrayList<>();
    /**
     * The edges of the game, they are a segment of the LED-Strip
     */
    private static final ArrayList<Segment> SEGMENTS = new ArrayList<>();
    /**
     * The BCM Pin-Address for the RaspberryPi where the first MCP23S17's interrupt line is connected
     */
    public static final int BCM_IC_0_INTERRUPT_PIN = 23;
    /**
     * The BCM Pin-Address for the RaspberryPi where the second MCP23S17's interrupt line is connected
     */
    public static final int BCM_IC_1_INTERRUPT_PIN = 24;
    private int totalCostsATM;

    /**
     * Program entry point: get pi4j context and pass it to run()
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        console.box("Hello Rasbian world !");
        Context pi4j = null;
        try {
            var piGpio = PiGpio.newNativeInstance();
            pi4j = Pi4J.newContextBuilder()
                    .noAutoDetect()
                    .add(
                            PiGpioDigitalInputProvider.newInstance(piGpio),
                            PiGpioDigitalOutputProvider.newInstance(piGpio),
                            PiGpioSpiProvider.newInstance(piGpio)
                    )
                    .build();
            new Main().run(pi4j);
        } catch (InvocationTargetException e) {
            console.println("Error: " + e.getTargetException().getMessage());
        } catch (Exception e) {
            console.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pi4j != null) {
                pi4j.shutdown();
            }
        }
    }

    /**
     * With a given pi4j context, run the main program logic
     *
     * @param pi4j The current pi4j context
     * @throws Exception Uncaught Exceptions will be caught  by main()
     */
    private void run(Context pi4j) throws Exception {
        Platforms platforms = pi4j.platforms();

        console.box("Pi4J PLATFORMS");
        console.println();
        platforms.describe().print(System.out);
        console.println();
        console.promptForExit();

        runButtonTestScaledUp(pi4j);

        console.println("ok finished");
        pi4j.shutdown();
    }

    /**
     * Logs every the chipnumber and pin to the console when a button is pressed.
     *
     * @param pi4j the pi4j context
     * @throws InterruptedException when the user presses Ctrl+C
     * @throws IOException          when the SPI-init for the MCP23S17 fails
     */
    private void runButtonTestScaledUp(Context pi4j) throws InterruptedException, IOException {
        var pins = setupGPIOExtensionICs(pi4j);
        var Strip = setupLEDStrip(pi4j);

        InputStream csv = getClass().getResourceAsStream("/LEDSegments.csv");

        totalCostsATM = 0;
        Segment.SegmentStateChange callback = deltaCost -> {
            totalCostsATM += deltaCost;
            console.println("Kosten: " + totalCostsATM);
        };
        var segments = Segment.createSegemntsAccordingToCSV(console, Strip, pins, csv, callback);

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

        for (int terminal: terms) {
            segments.get(terminal-1).toggle();
        }

        console.println("connection stuff done");
        console.waitForExit();
    }

    /**
     * Will set up and initialise the LED-Strip
     *
     * @param pi4j the pi4j {@link Context}
     * @return the {@link LedStrip} object
     */
    private static LedStrip setupLEDStrip(Context pi4j) {
        LedStrip ledStrip = new Ws281xLedStrip(
                845,
                10,
                800000,
                10,
                255,
                0,
                false,
                LedStripType.WS2811_STRIP_GRB,
                true
        );
        return ledStrip;
    }

    /**
     * Will set up and initialise the MCP23S17 GPIO-Extension ICs
     *
     * @param pi4j the pi4j {@link Context} object
     * @return two fully configured lists of {@link MCP23S17.PinView} objects.
     * that means 2 * 16 extra GPIO Pins set as input, pulled up and interrupt enabled
     * @throws IOException when the creation of the {@link MCP23S17} objects or
     *                     gathering of the {@link MCP23S17.PinView} objects fail
     */
    private static ArrayList<ArrayList<MCP23S17.PinView>> setupGPIOExtensionICs(Context pi4j) throws IOException {
        var interruptPinConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("interrupt0")
                .name("a MCP interrupt")
                .address(22)
                .pull(PullResistance.PULL_UP)
                .provider("pigpio-digital-input");

        var interruptPinChip0 = pi4j.create(interruptPinConfig);
        var interruptPinChip1 = pi4j.create(interruptPinConfig.address(23).id("interrupt1"));
        var interruptPinChip2 = pi4j.create(interruptPinConfig.address(24).id("interrupt2"));
        var interruptPinChip3 = pi4j.create(interruptPinConfig.address(25).id("interrupt3"));
        var interruptPinChip4 = pi4j.create(interruptPinConfig.address(27).id("interrupt4"));

        //interruptPinChip0.addListener(stateChange -> {if(stateChange.state().isHigh()){console.print("chip 0 interrupt: ");}});
        //interruptPinChip1.addListener(stateChange -> {if(stateChange.state().isHigh()){console.print("chip 1 interrupt: ");}});
        //interruptPinChip2.addListener(stateChange -> {if(stateChange.state().isHigh()){console.print("chip 2 interrupt: ");}});
        //interruptPinChip3.addListener(stateChange -> {if(stateChange.state().isHigh()){console.print("chip 3 interrupt: ");}});
        //interruptPinChip4.addListener(stateChange -> {if(stateChange.state().isHigh()){console.print("chip 4 interrupt: ");}});
        DigitalInput[] interruptPins = {interruptPinChip0,
                interruptPinChip1,
                interruptPinChip2,
                interruptPinChip3,
                interruptPinChip4};

        var interruptChips = MCP23S17.multipleNewOnSameBusWithTiedInterrupts(
                pi4j,
                SpiBus.BUS_1,
                interruptPins,
                5,
                true);

        var pins = new ArrayList<ArrayList<MCP23S17.PinView>>(5);
        pins.add(interruptChips.get(0).getAllPinsAsPulledUpInterruptInput());
        pins.add(interruptChips.get(1).getAllPinsAsPulledUpInterruptInput());
        pins.add(interruptChips.get(2).getAllPinsAsPulledUpInterruptInput());
        pins.add(interruptChips.get(3).getAllPinsAsPulledUpInterruptInput());
        pins.add(interruptChips.get(4).getAllPinsAsPulledUpInterruptInput());

        return pins;
    }

    /**
     * Delays the current thread by an amount of milliseconds
     *
     * @param milliseconds the desired amount of milliseconds to delay the current thread
     */
    static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
