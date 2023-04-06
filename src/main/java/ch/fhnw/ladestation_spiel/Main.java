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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private static final ArrayList<Edge> nodes = new ArrayList<>();
    /**
     * The edges of the game, they are a segment of the LED-Strip
     */
    private static final ArrayList<Edge> edges = new ArrayList<>();
    /**
     * The BCM Pin-Address for the RaspberryPi where the first MCP23S17's interrupt line is connected
     */
    public static final int BCM_IC_0_INTERRUPT_PIN = 23;
    /**
     * The BCM Pin-Address for the RaspberryPi where the second MCP23S17's interrupt line is connected
     */
    public static final int BCM_IC_1_INTERRUPT_PIN = 24;

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

        //runPrototypeExample(pi4j);
        runNewLibraryTest(pi4j);

        console.println("ok finished");
        pi4j.shutdown();
    }

    private void runNewLibraryTest(Context pi4j) throws InterruptedException {
        var LEDStripC = new Ws281xLedStrip(
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
        for (int i = 0; i < 845; i++) {
            LEDStripC.setPixel(i,Color.CYAN);
            LEDStripC.render();
        }
        delay(2000);
        LEDStripC.setStrip(Color.RED);
        LEDStripC.render();
        delay(1000);
        LEDStripC.setStrip(Color.GREEN);
        LEDStripC.render();
        delay(1000);
        LEDStripC.setStrip(Color.BLUE);
        LEDStripC.render();
        delay(3000);
    }

    /**
     * Runs the tech-prototype of the game
     *
     * @param pi4j the pi4j {@link Context}
     * @throws IOException when GPIO-extension setup fails
     */

    private static void runPrototypeExample(Context pi4j) throws IOException {
        var pins = setupGPIOExtensionICs(pi4j);
        var pinsIC0 = pins.get(0);
        var pinsIC1 = pins.get(1);

        LedStrip ledStrip = setupLEDStrip(pi4j);

        assignEdgesToLEDStripSegmentsAndPins(pinsIC0, pinsIC1, ledStrip);

        //light up some of the terminals
        nodes.get(2).toggle();
        nodes.get(4).toggle();
        nodes.get(5).toggle();
        nodes.get(6).toggle();

        //loop while application is running
        while (console.isRunning()) {
            synchronized (ledStrip) {
                ledStrip.render();
            }
            delay(16);
        }
    }

    /**
     * Will setup and initialise the LED-Strip
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
     * Assigns every node and edge to its segment of the LED-Strip and its GPIO-Extension pin
     *
     * @param pinsIC0  the {@link MCP23S17.PinView}s of the first MCP23S17 IC
     * @param pinsIC1  the {@link MCP23S17.PinView}s of the second MCP23S17 IC
     * @param ledStrip the LED-Strip that displays the terminals and edges
     */
    static void assignEdgesToLEDStripSegmentsAndPins(List<MCP23S17.PinView> pinsIC0, List<MCP23S17.PinView> pinsIC1, LedStrip ledStrip){
        //TODO
    }

    /**
     * Will setup and initialise the MCP23S17 GPIO-Extension ICs
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
                .address(BCM_IC_0_INTERRUPT_PIN)
                .pull(PullResistance.PULL_UP)
                .provider("pigpio-digital-input");

        var interruptPinChip0 = pi4j.create(interruptPinConfig);
        var interruptPinChip1 = pi4j.create(interruptPinConfig.address(BCM_IC_1_INTERRUPT_PIN).id("interrupt1"));

        interruptPinChip0.addListener(stateChange -> console.println("chip zero interrupt: " + stateChange.state()));
        interruptPinChip1.addListener(stateChange -> console.println("chip one interrupt: " + stateChange.state()));
        DigitalInput[] interruptPins = {interruptPinChip0, interruptPinChip1};

        var interruptChips = MCP23S17.multipleNewOnSameBusWithTiedInterrupts(
                pi4j,
                SpiBus.BUS_1,
                interruptPins,
                2,
                true);

        var pins = new ArrayList<ArrayList<MCP23S17.PinView>>(2);
        pins.add(interruptChips.get(0).getAllPinsAsPulledUpInterruptInput());
        pins.add(interruptChips.get(1).getAllPinsAsPulledUpInterruptInput());

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
