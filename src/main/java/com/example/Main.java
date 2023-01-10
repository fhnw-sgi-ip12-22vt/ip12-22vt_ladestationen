/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

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

        runPrototypeExample(pi4j);

        console.println("ok finished");
        pi4j.shutdown();
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

        LEDStrip ledStrip = setupLEDStrip(pi4j);

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
        ledStrip.allOff();
    }

    /**
     * Will setup and initialise the LED-Strip
     *
     * @param pi4j the pi4j {@link Context}
     * @return the {@link LEDStrip} object
     */
    private static LEDStrip setupLEDStrip(Context pi4j) {
        LEDStrip ledStrip = new LEDStrip(pi4j, PIXEL_AMT, 1.0, SpiBus.BUS_0);
        ledStrip.allOff();
        return ledStrip;
    }

    /**
     * Assigns every node and edge to its segment of the LED-Strip and its GPIO-Extension pin
     *
     * @param pinsIC0  the {@link com.example.MCP23S17.PinView}s of the first MCP23S17 IC
     * @param pinsIC1  the {@link com.example.MCP23S17.PinView}s of the second MCP23S17 IC
     * @param ledStrip the LED-Strip that displays the terminals and edges
     */
    private static void assignEdgesToLEDStripSegmentsAndPins(ArrayList<MCP23S17.PinView> pinsIC0, ArrayList<MCP23S17.PinView> pinsIC1, LEDStrip ledStrip) {
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 0, 0));
        edges.add(new Edge(ledStrip, pinsIC1.get(8), 1, 7));
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 8, 8));
        edges.add(new Edge(ledStrip, pinsIC0.get(11), 9, 14));
        edges.add(new Edge(ledStrip, pinsIC0.get(10), 15, 26));
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 27, 27));
        edges.add(new Edge(ledStrip, pinsIC0.get(9), 28, 30));
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 31, 31));
        edges.add(new Edge(ledStrip, pinsIC1.get(9), 32, 44));
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 45, 45));
        edges.add(new Edge(ledStrip, pinsIC0.get(8), 46, 57));
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 58, 58));
        edges.add(new Edge(ledStrip, pinsIC1.get(1), 59, 62));
        nodes.add(new Edge(ledStrip, LEDStrip.PixelColor.BLUE, 63, 63));
        edges.add(new Edge(ledStrip, pinsIC1.get(0), 64, 73));
        edges.add(new Edge(
                ledStrip,
                new MCP23S17.PinView[]{pinsIC0.get(0), pinsIC0.get(1)},
                74,
                98));
    }

    /**
     * Will setup and initialise the MCP23S17 GPIO-Extension ICs
     *
     * @param pi4j the pi4j {@link Context} object
     * @return two fully configured lists of {@link com.example.MCP23S17.PinView} objects.
     * that means 2 * 16 extra GPIO Pins set as input, pulled up and interrupt enabled
     * @throws IOException when the creation of the {@link MCP23S17} objects or
     *                     gathering of the {@link com.example.MCP23S17.PinView} objects fail
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
