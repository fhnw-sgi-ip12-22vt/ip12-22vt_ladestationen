package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.game.gamelogic.Edge;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.model.game.gamelogic.Node;
import ch.ladestation.connectncharge.pui.GamePUI;
import ch.ladestation.connectncharge.util.Pi4JContext;
import ch.ladestation.connectncharge.util.mvcbase.ObservableArray;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationControllerTest {

    public static Stream<Arguments> sourceTestHasCycle() {
        int[][] trivialCycle = {{1, 2}, {2, 3}, {3, 1}};
        int[][] trivialNoCycle = {{1, 2}, {2, 3}};
        int[][] graph1noCycle = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 80}, {80, 81}, {81, 82}, {5, 6}, {6, 7}, {8, 9}};
        int[][] graph2yesCycle = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 77}, {77, 88}, {6, 7}, {7, 8}, {4, 1}};
        int[][] graph3multipleDisconectedCycles = {{1, 2}, {2, 3}, {3, 4}, {4, 1}, {11, 22}, {22, 33}, {33, 11}};
        int[][] graph4treeAndDisconnectedCycle =
            {{1, 11}, {11, 10}, {1, 12}, {12, 13}, {13, 14},
                {14, 15}, {1, 3}, {3, 2}, {2, 7}, {2, 5}, {5, 8},
                {8, 4}, {8, 6}, {17, 18}, {18, 19}, {19, 17}};
        int[][] graph5bigCycle =
            {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 10}, {10, 11}, {11, 12}, {12, 1}};


        return Stream.of(
            Arguments.of(trivialNoCycle, false),
            Arguments.of(trivialCycle, true),
            Arguments.of(graph1noCycle, false),
            Arguments.of(graph2yesCycle, true),
            Arguments.of(graph3multipleDisconectedCycles, true),
            Arguments.of(graph4treeAndDisconnectedCycle, true),
            Arguments.of(graph5bigCycle, true)
        );
    }

    @Test
    @DisplayName("Verify that edges are being toggled")
    public void verifyTogglingOfEdges() {
        var edge = mock(Edge.class);
        when(edge.isOn()).thenReturn(false);

        ObservableArray.ValueChangeListener<Edge> mockedListener = mock(ObservableArray.ValueChangeListener.class);

        var model = new Game();
        model.activatedEdges.onChange(mockedListener);

        var cut = new ApplicationController(model);
        cut.setGameStarted(true);
        cut.edgePressed(edge);


        cut.runLater(m -> {
            verify(edge, times(1)).on();
            verify(edge, times(0)).setOn(false);
            verify(edge, times(0)).off();
            assertArrayEquals(new Edge[] {edge}, model.activatedEdges.getValues());
            verify(mockedListener).update(new Edge[0], new Edge[] {edge});
        });
    }

    @ParameterizedTest
    @DisplayName("Test if the hasCycle() method reliably computes whether the graph has a cycle or not")
    @MethodSource("sourceTestHasCycle")
    public void testHasCycle(int[][] fromAndToNodes, boolean result) {
        var mockedGraph = mockGraph(fromAndToNodes);

        assertEquals(result, ApplicationController.hasCycle(mockedGraph));
        //verify that all the nodes were visited
        for (var edge : mockedGraph) {
            verify(edge, atLeast(1)).getFromNode();
            verify(edge, atLeast(1)).getToNode();
        }
    }

    private Edge[] mockGraph(int[][] fromAndToNodes) {
        var ret = new ArrayList<Edge>();
        var nodes = new HashMap<Integer, Node>();

        for (int i = 0; i < fromAndToNodes.length; i++) {
            int fromNode = fromAndToNodes[i][0];
            int toNode = fromAndToNodes[i][1];
            var mockEdge = mock(Edge.class);

            Function<Integer, Node> computeFunc = nodeIndex -> {
                var mockNode = mock(Node.class);
                when(mockNode.getSegmentIndex()).thenReturn(nodeIndex);
                return mockNode;
            };


            var fNode = nodes.computeIfAbsent(fromNode, computeFunc);
            var tNode = nodes.computeIfAbsent(toNode, computeFunc);

            when(mockEdge.getFromNodeId()).thenReturn(fromNode);
            when(mockEdge.getToNodeId()).thenReturn(toNode);
            when(mockEdge.getFromNode()).thenReturn(fNode);
            when(mockEdge.getToNode()).thenReturn(tNode);
            //offset index so it doesn't overlap with nodes
            when(mockEdge.getSegmentIndex()).thenReturn(i + 696969);

            ret.add(mockEdge);
        }
        return ret.toArray(Edge[]::new);
    }

    @Test
    @DisplayName("Test if accepting a hint edge (add edge) removes the hint")
    public void testHintGoesAway() {

        //given
        var model = new Game();
        var controller = new ApplicationController(model);

        var mockLedStrip = mock(Ws281xLedStrip.class);
        GamePUI pui = new GamePUI(controller, Pi4JContext.createMockContext(), mockLedStrip);
        controller.setGPUI(pui);

        controller.loadLevels();

        assertArrayEquals(new Edge[0], model.solution.getValues());

        controller.loadNextLevel();
        controller.awaitCompletion();
        controller.edgePressed(model.blinkingEdge);
        controller.awaitCompletion();
        controller.setCountdownFinished();


        controller.awaitCompletion();

        assertTrue(model.gameStarted.getValue());
        assertTrue(model.isCountdownFinished.getValue());
        var sol = model.solution.getValues();
        assertTrue(sol.length > 0);
        assertFalse(model.ignoringInputs);
        var almost = new Edge[sol.length - 1];
        for (int i = 1; i < sol.length; i++) {
            controller.edgePressed(sol[i]);
            almost[i - 1] = sol[i];
            controller.awaitCompletion();
        }
        controller.awaitCompletion();

        assertArrayEquals(almost, model.activatedEdges.getValues());

    }
}
