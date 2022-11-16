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

/**
 *
 * @author luca
 */
public class Main {

    private static final int PIN_LED = 22; // PIN 15 = BCM 22
    private static final int BUTTON_LED = 18;
    private static final Console console = new Console();

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

        //Configure LED on Pin 22
        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("led")
                .name("red LED")
                .address(PIN_LED)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");
        //Configure LED on Pin 18
        var buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button")
                .name("blue button")
                .address(BUTTON_LED)
                .pull(PullResistance.PULL_UP)
                .provider("pigpio-digital-input");


        var led = pi4j.create(ledConfig);
        var button = pi4j.create(buttonConfig);

        //Listen for button presses & releases: change LED state and when button is being pressed,
        //make a beep with the speaker
        button.addListener(new DigitalStateChangeListener() {
            @Override
            public void onDigitalStateChange(DigitalStateChangeEvent digitalStateChangeEvent) {
                if(digitalStateChangeEvent.state() == DigitalState.LOW){
                    led.low();
                    console.println("button pressed");
                    speakerTest();
                }else{
                    led.high();
                    console.println("button released");
                }
            }
        });

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

}
