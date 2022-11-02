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
    private static final Console console = new Console();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        console.box("Hello Rasbian world !");
        Context pi4j = null;
        try {
            pi4j = Pi4J.newAutoContext();
            new Main().run(pi4j);
        } catch (InvocationTargetException e) {
            console.println("huwsrhzwejzh???Error: " + e.getTargetException().getMessage());
        } catch (Exception e) {
            console.println("huh???Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pi4j != null) {
                pi4j.shutdown();
            }
        }
    }

    private void run(Context pi4j) throws Exception {
        Platforms platforms = pi4j.platforms();

        console.box("Pi4J PLATFORMS");
        console.println();
        platforms.describe().print(System.out);
        console.println();
        console.promptForExit();

        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                        .id("led")
                        .name("LED Flasher")
                        .address(PIN_LED)
                        .shutdown(DigitalState.LOW)
                        .initial(DigitalState.LOW)
                        .provider("pigpio-digital-output");
        var buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button")
                .name("blue button")
                .address(18)
                .pull(PullResistance.PULL_UP)
                .provider("pigpio-digital-input");


        var led = pi4j.create(ledConfig);
        var button = pi4j.create(buttonConfig);
        int counter = 0;

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

        console.waitForExit();
        pi4j.shutdown();
    }

    private void SspeakerTest() {
        ProcessBuilder pb = new ProcessBuilder("speaker-test", "-tsin","-f 3200","-l1");
        try {
            Process p = pb.start();
            //p.waitFor();
        }catch (Exception e){
            console.println("oh noes, speaker test fucked up: ",e.getMessage());
        }
    }

}
