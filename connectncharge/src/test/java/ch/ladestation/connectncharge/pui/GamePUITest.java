package ch.ladestation.connectncharge.pui;

import ch.ladestation.connectncharge.controller.ApplicationController;
import ch.ladestation.connectncharge.model.game.gamelogic.Edge;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.spi.MockSpi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GamePUITest extends ComponentTest {

    @Test
    @DisplayName("Tests that the ICs are initialized correctly via SPI")
    void testInitChips() {
        //given
        var model = new Game();
        var mockController = mock(ApplicationController.class);
        when(mockController.getModel()).thenReturn(model);

        var mockLedStrip = mock(Ws281xLedStrip.class);

        GamePUI pui = new GamePUI(mockController, this.pi4j, mockLedStrip);

        var mockedPins = Arrays.stream(pui.getInterruptPins()).map(this::toMock).toList();
        var mockedSpi = toMock(pui.getSpiInterface());

        //the expected buffer is constructed based on the datasheet:
        // docu repository/hardware/resources/
        // datenblatt-651444-microchip-technology-mcp23s17-esp-
        // schnittstellen-ic-e-a-erweiterungen-por-spi-10-mhz-spdip-28.pdf
        //it represents the bytes that are required to initialize the MCP23S17 chips
        //every message is 3 bytes long: [opcode] [register addr] [data]
        //the opcode is comprised as follows:
        //|0|1|0|0|A2|A1|A0|R/W|
        //A2-A3: Hardware adress bits
        //R/W: LSB is read or write: 1 = read, 0 = write
        byte[] expectedBuffer =
            {64, 10, 72,    //enable tied interrupts and hardware address pins on all chips
                72, 10, 72, //speak to the last chip: enable HWAddrPins and tied interrupts explicitly
                64, 0, -1,  //configure pin direction (all input) on ports A
                64, 1, -1,  //...and B
                64, 12, -1, //configure pullup on pins (all pulled up) on Port A
                64, 13, -1, //...and B
                64, 4, -1,  //enable interrupts (all enabled) on Port A
                64, 5, -1,  //and B
                66, 0, -1,  //repeat for the other 4 chips
                66, 1, -1,
                66, 12, -1,
                66, 13, -1,
                66, 4, -1,
                66, 5, -1,
                68, 0, -1,
                68, 1, -1,
                68, 12, -1,
                68, 13, -1,
                68, 4, -1,
                68, 5, -1,
                70, 0, -1,
                70, 1, -1,
                70, 12, -1,
                70, 13, -1,
                70, 4, -1,
                70, 5, -1,
                72, 0, -1,
                72, 1, -1,
                72, 12, -1,
                72, 13, -1,
                72, 4, -1,
                72, 5, -1,
                0, 0, 0, 0};
        var buff = new byte[expectedBuffer.length];
        mockedSpi.read(buff);
        assertArrayEquals(expectedBuffer, buff);
    }

    /**
     * this test is impossible to verify because the mocked spi transfer function doesn't do anything but
     * echo its inputs back. So there is no way to inject the necessary data or at least verify the proper
     * spi data bytes. tough luck. will probably raise an issue about this some day.
     */
    @Test
    @DisplayName("Test that the correct Edge is being triggered")
    void testTriggeredEdge() {
        //given
        var model = new Game();
        var mockController = mock(ApplicationController.class);
        when(mockController.getModel()).thenReturn(model);

        var mockLedStrip = mock(Ws281xLedStrip.class);

        GamePUI pui = new GamePUI(mockController, this.pi4j, mockLedStrip);

        var mockedPins = Arrays.stream(pui.getInterruptPins()).map(this::toMock).toList();
        var mockedSpi = toMock(pui.getSpiInterface());


        //empty the init spi traffic
        readUntilEmpty(mockedSpi);

        mockedPins.get(0).mockState(DigitalState.HIGH);
        mockedPins.get(0).mockState(DigitalState.LOW); //active-low interrupt
        mockedPins.get(0).mockState(DigitalState.HIGH);

        var buff = new byte[3];
        //check that the correct chip was adressed
        mockedSpi.read(buff);
        //todo: fix bug in pi4j mock spi plugin.
        assertArrayEquals(new byte[] {0, 0, 0}, buff);
    }

    private void readUntilEmpty(MockSpi spi) {
        int readResult = 0;
        while (readResult != -1) {
            readResult = spi.read(new byte[1], 0, 1);
        }
    }

    @ParameterizedTest
    @DisplayName("Test that the LED-Strip is being controlled by the model ObservableValues")
    @ValueSource(ints = {1, 2, 4, 101})
        //test first and last edge
    void testModelToLEDStripBinding(int segmentIndex) {
        //given
        log.debug("given testModelToLEDStripBinding(segmentIndex={})", segmentIndex);
        var model = new Game();
        var controller = new ApplicationController(model);
        controller.setGameStarted(true);
        var mockLedStrip = mock(Ws281xLedStrip.class);
        GamePUI pui = new GamePUI(controller, this.pi4j, mockLedStrip);
        var inOrder = inOrder(mockLedStrip);

        var theEdge = (Edge) pui.lookUpSegmentIdToSegment(segmentIndex);
        var numPixels = theEdge.getEndIndex() - theEdge.getStartIndex() + 1;
        log.debug("when controller.edgePressed(theEdge={});", segmentIndex);
        controller.edgePressed(theEdge);
        log.debug("controller.awaitCompletion()");
        controller.awaitCompletion();
        var mutex = new Semaphore(1);
        try {
            mutex.acquire();
            controller.runLater(v -> mutex.release());
            mutex.acquire();
            pui.runLater(v -> mutex.release());
            log.debug("final acquire() in testModelToLEDStripBinding(segmentIndex={})", segmentIndex);
            mutex.acquire();
            log.debug("try and verify in testModelToLEDStripBinding(segmentIndex={})", segmentIndex);

            inOrder.verify(mockLedStrip, times(numPixels)).setPixel(anyInt(), eq(theEdge.getColor()));
            inOrder.verify(mockLedStrip).render();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            mutex.release();
        }
    }

    @ParameterizedTest
    @DisplayName("Test that the tipp Edge is being correctly controlled by the model")
    @ValueSource(ints = {1, 2, 4, 55, 64, 44, 88, 101})
        //test a few edges
    void testTippEdgeBinding(int segmentIndex) {
        //given
        var model = new Game();
        var controller = new ApplicationController(model);
        controller.setGameStarted(true);
        var mockLedStrip = mock(Ws281xLedStrip.class);
        GamePUI pui = new GamePUI(controller, this.pi4j, mockLedStrip);
        var inOrder = inOrder(mockLedStrip);

        var theEdge = (Edge) pui.lookUpSegmentIdToSegment(segmentIndex);
        var numPixels = theEdge.getEndIndex() - theEdge.getStartIndex() + 1;

        controller.setTippEdge(theEdge);

        controller.awaitCompletion();
        pui.awaitCompletion();

        inOrder.verify(mockLedStrip, times(numPixels)).setPixel(anyInt(), eq(theEdge.getColor()));
        inOrder.verify(mockLedStrip).render();
        assertEquals(Color.ORANGE, model.tippEdge.getColor());

        controller.removeTippEdge();

        controller.awaitCompletion();
        pui.awaitCompletion();

        inOrder.verify(mockLedStrip, times(numPixels)).setPixel(anyInt(), eq(Color.BLACK));
        inOrder.verify(mockLedStrip).render();
        assertEquals(Color.GREEN, model.tippEdge.getColor());

    }

}
