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
import java.lang.reflect.InvocationTargetException;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class of the maven pi4j archetype
 * @author luca, MNG
 */
public class Main {

    private static final int PIN_LED = 22; // PIN 15 = BCM 22
    private static final int PIN_BUTTON = 18;
    private int buttonIds = 0;
    private static final Console console = new Console();
    private static LEDStrip ledStrip;
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
            if(ledStrip != null){
                ledStrip.allOff();
            }
            if (pi4j != null) {
                pi4j.shutdown();
            }
        }
    }
    private MCP23S17 setupMCP(Context pi4j) throws Exception{
        var ChipSelectConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("CS"+(buttonIds++))
                .name("chip select")
                .address(2)
                .provider("pigpio-digital-output");

        var ChipSelect = pi4j.create(ChipSelectConfig);

        return MCP23S17.newWithoutInterrupts(pi4j,SpiBus.BUS_1,ChipSelect);
    }
    private ArrayList<MCP23S17.PinView> getOutputPinsMCP(MCP23S17 IC)throws Exception{
        var ICPinsIter = IC.getPinViewIterator();
        var ICPins = new ArrayList<MCP23S17.PinView>(16);
        for(var pin = ICPinsIter.next();ICPinsIter.hasNext();pin = ICPinsIter.next()){
            pin.setAsOutput();
            ICPins.add(pin);
            console.println("pinview direction output set");
        }
        IC.writeIODIRA();
        IC.writeIODIRB();
        IC.writeOLATA();
        IC.writeOLATB();
        return ICPins;
    }
    private ArrayList<MCP23S17.PinView> getInputPinsMCP(MCP23S17 IC)throws Exception{
        var ICPinsIter = IC.getPinViewIterator();
        var ICPins = new ArrayList<MCP23S17.PinView>(16);
        while(ICPinsIter.hasNext()){
            var pin = ICPinsIter.next();
            pin.setAsInput();
            pin.enablePullUp();
            if(pin.isOutput()){
                pin.isOutput();
                throw new Exception("wtf dude");
            }
            ICPins.add(pin);
            console.println("pinview direction input set");
        }

        IC.writeIODIRA();
        IC.writeIODIRB();
        IC.writeGPPUA();
        IC.writeGPPUB();
        return ICPins;
    }
    private void MCPoff(MCP23S17 IC, MCP23S17.PinView ICPin) throws Exception{
            console.println("off");
            ICPin.set(false);
            IC.writeOLATA();
    }
    private void MCPon(MCP23S17 IC, MCP23S17.PinView ICPin) throws Exception{
            console.println("on");
            ICPin.set(true);
            IC.writeOLATA();
    }
    private void testAllOutputsMCP(ArrayList<MCP23S17> ICs, ArrayList<MCP23S17.PinView>[] ICAllPins) throws Exception{
       int cnt = 0;
        for(int i = 0; i< ICs.size(); i++){
            var ICPins = ICAllPins[i];
            var IC = ICs.get(i);
            for(var pin : ICPins){
                //set all pins to false
                for(var otherpin : ICPins){
                    otherpin.set(false);
                }
                //...except the one we want to test this cylce
                pin.set(true);
                //write to the IC
                IC.writeOLATA();
                IC.writeOLATB();
                delay(50);
                cnt++;
            }
        }
        console.println("tested "+cnt+" pins");
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

        //setup MCP
        var ICtriple = MCP23S17.multipleNewOnSameBus(pi4j,SpiBus.BUS_1,2);
        var ICPins = (ArrayList<MCP23S17.PinView>[]) new ArrayList[2];

        ICPins[0] = getInputPinsMCP(ICtriple.get(0));
        ICPins[1] = getInputPinsMCP(ICtriple.get(1));

        int pixels = 74;
        ledStrip = new LEDStrip(pi4j, pixels, 1.0, SpiBus.BUS_0);
        ledStrip.allOff();
        //ledStrip.setStripColor(LEDStrip.PixelColor.ORANGE);
        ledStrip.render();
        int h = 0;
        var oldstates = new boolean[32];
        Arrays.fill(oldstates, true);

        var pinsToCheck = new MCP23S17.PinView[32];
        for(int i = 0; i<16;++i){
            pinsToCheck[i] = ICPins[0].get(i);
        }
        for(int i = 0; i<16;++i){
            pinsToCheck[i+16] = ICPins[1].get(i);
        }
        //more buttons
        while(h++ < 1000000000){
            for(var i = 0; i < pinsToCheck.length;++i){
                boolean state = pinsToCheck[i].getFromRead();
                if(state != oldstates[i]){
                    console.println("state "+i+" differs: now "+state);
                    if(state){
                        ledStrip.setStripColor(LEDStrip.PixelColor.ORANGE);
                    }else{
                        ledStrip.setStripColor((LEDStrip.PixelColor.PURPLE));
                    }
                    ledStrip.render();
                }
                oldstates[i] = state;
            }
            delay(10);
        }

        //testParallelControlCapabilities(ICtriple, ICPins);
        console.println("ok finished");
        console.waitForExit();
        pi4j.shutdown();
    }
    private void toggleEdge(LEDStrip strip, int start, int end){
        if(strip.getPixelColor(start) > 0){
            for(int i = start; i<=end; ++i){
                strip.setPixelColor(i,0);
            }
        }else{
            for(int i = start; i<=end; ++i){
                strip.setPixelColor(i,LEDStrip.PixelColor.PURPLE);
            }
        }
        strip.render();
    }
    private void testParallelControlCapabilities(ArrayList<MCP23S17> ICtriple, ArrayList<MCP23S17.PinView>[] ICPins) throws Exception {
        int h=0;
        while(h++ < 100) {
            console.println("MmmMmmmMMmm");
            ledStrip.setStripColor(LEDStrip.PixelColor.YELLOW);
            ledStrip.render();

            testAllOutputsMCP(ICtriple, ICPins);

            ledStrip.setStripColor(LEDStrip.PixelColor.GREEN);
            ledStrip.render();

            testAllOutputsMCP(ICtriple, ICPins);
        }
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
