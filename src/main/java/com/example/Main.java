/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import com.pi4j.platform.Platforms;
import com.pi4j.util.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.Process;
import java.lang.ProcessBuilder;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.github.mbelling.ws281x.LedStripType;
import com.example.LEDStrip;

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
            pi4j = Pi4J.newAutoContext();
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

        var ledStrib =  new Ws281xLedStrip(
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
        ledStrib.render();
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
        //make a beep with the speaker
        button1.addListener(createListener("one"));
        button2.addListener(createListener("two"));
        button3.addListener(createListener("three"));

        //only stop the program once the user wants it to
        console.waitForExit();
        pi4j.shutdown();
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

    private DigitalStateChangeListener createListener(String log){
        return digitalStateChangeEvent -> {
            if(digitalStateChangeEvent.state() == DigitalState.LOW){
                ledStrip.setPixelColor(5,LEDStrip.PixelColor.PURPLE);
                ledStrip.render();
                console.println("button " + log + " pressed");
            }else{
                ledStrip.allOff();
                console.println("button " + log + " released");
            }
        };
    }

}
