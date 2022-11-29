/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.pi4j.Pi4J;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider;
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.util.ArrayList;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.github.mbelling.ws281x.LedStripType;
import com.example.LEDStrip;
import com.example.MCP23S17;

/**
 * Main class of the maven pi4j archetype
 * @author luca, MNG
 */
public class Main {

    private static final int PIN_LED = 22; // PIN 15 = BCM 22
    private static final int PIN_BUTTON = 18;
    private int buttonIds = 0;
    private static final Console console = new Console();
    private LEDStrip ledStrip;
    /**
     * Program entry point: get pi4j context and pass it to run()
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
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
    private void runMCPExample(Context pi4j) throws Exception{
        var ChipSelectConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("CS"+(buttonIds++))
                .name("chip select")
                .address(2)
                .provider("pigpio-digital-output");

        var ChipSelect = pi4j.create(ChipSelectConfig);

        MCP23S17 IC = MCP23S17.newWithoutInterrupts(pi4j,0,ChipSelect);
        var ICPinsIter = IC.getPinViewIterator();
        var ICPins = new ArrayList<MCP23S17.PinView>(16);
        for(var pin = ICPinsIter.next();ICPinsIter.hasNext();pin = ICPinsIter.next()){
            pin.setAsOutput();
            ICPins.add(pin);
            console.println("pinview direction set");
        }
        for(var pin : ICPins){
            pin.set(true);
            console.println("huh");
        }
        IC.writeIODIRA();
        IC.writeIODIRB();
        IC.writeOLATA();
        IC.writeOLATB();
        for(int i = 0; i < 10;++i){
            console.println("on"+i);
            ICPins.get(5).set(true);
            IC.writeOLATA();
            delay(1000);
            console.println("off"+i);
            ICPins.get(5).set(false);
            IC.writeOLATA();
            delay(1000);
        }
    }
    //TODO: extract main logic from boilerplate
    /**
     * With a given pi4j context, run the main program logic
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

        //Configure LED buttons
        var button1 = createButton(2,pi4j);
        var button2 = createButton(3,pi4j);
        var button3 = createButton(4,pi4j);
        button1.addListener(createListener(()->{console.println("one");}));
        button2.addListener(createListener(()->{console.println("two");}));
        button3.addListener(createListener(()->{console.println("three");}));

        /*var ledStrib =  new Ws281xLedStrip(
                            12,       // leds
                            10,          // Using pin 10 to do SPI, which should allow non-sudo access
                            800000,  // freq hz
                            10,            // dma
                            255,      // brightness
                            0,      // pwm channel
                            false,        // invert
                            LedStripType.WS2811_STRIP_RGB,    // Strip type
                            true    // clear on exit
                        );
        ledStrib.setStrip(255,255,40);
        ledStrib.render();*/


        int pixels = 12;
        ledStrip = new LEDStrip(pi4j, pixels, 1.0,1);
        ledStrip.allOff();
        int h=0;
        while(h++ < 10000) {
            console.println("MmmMmmmMMmm");
            //waitForKey("Set led strip to ORANGE");
            ledStrip.setStripColor(LEDStrip.PixelColor.YELLOW);
            ledStrip.render();

            delay(1000);
            ledStrip.setStripColor(LEDStrip.PixelColor.GREEN);
            ledStrip.render();

            delay(1000);
        }
        /*
        ledStrip = new LEDStrip(pi4j, 12, 1.0,0);

        //set them all off, so nothing is shining
        System.out.println("Starting with setting all leds off");
        ledStrip.allOff();

        System.out.println("setting the LEDs to RED");
        ledStrip.setStripColor(LEDStrip.PixelColor.RED);
        ledStrip.render();
        ledStrip.delay(3000);

        System.out.println("setting the LEDs to Light Blue");
        ledStrip.setStripColor(LEDStrip.PixelColor.LIGHT_BLUE);
        ledStrip.render();
        ledStrip.delay(3000);

        System.out.println("setting the first led to Purple");
        ledStrip.setPixelColor(0, LEDStrip.PixelColor.PURPLE);
        ledStrip.render();
        ledStrip.delay(3000);

        System.out.println("setting the brightness to full and just show the first led as White");
        ledStrip.allOff();
        ledStrip.setBrightness(1);
        ledStrip.setPixelColor(0, LEDStrip.PixelColor.WHITE);
        ledStrip.render();
        ledStrip.delay(3000);

//finishing and closing
        ledStrip.close();
        System.out.println("closing the app");
        System.out.println("Color "+ ledStrip.getPixelColor(0));*/

        //Listen for button presses & releases: change LED state and when button is being pressed,

        //only stop the program once the user wants it to
        console.waitForExit();
        pi4j.shutdown();
    }
    static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    /**
     *make a subprocess that does a speaker test (beep!)
     */
    private void speakerTest() {
        ProcessBuilder pb = new ProcessBuilder("speaker-test", "-tsin","-f 3200","-l1");
        try {
            Process p = pb.start();
        }catch (Exception e){
            console.println("speaker test errored out: ",e.getMessage());
        }
    }
    private DigitalInput createButton(int pin, Context pi4j){
        //Configure button on Pin pin
        var buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button"+(buttonIds++))
                .name("a button")
                .address(pin)
                .pull(PullResistance.PULL_UP)
                .provider("pigpio-digital-input");

        return pi4j.create(buttonConfig);
    }

    private DigitalStateChangeListener createListener(Runnable high, Runnable low){
        return digitalStateChangeEvent -> {
            if(digitalStateChangeEvent.state() == DigitalState.LOW){
                low.run();
            }else{
                high.run();
            }
        };
    }
    private DigitalStateChangeListener createListener(Runnable low){
        return digitalStateChangeEvent -> {
            if(digitalStateChangeEvent.state() == DigitalState.LOW){
                low.run();
            }
        };
    }

}
