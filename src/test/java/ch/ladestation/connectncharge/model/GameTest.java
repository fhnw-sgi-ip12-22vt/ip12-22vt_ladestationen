package ch.ladestation.connectncharge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {

    // private Game game;

    @BeforeEach
    void setUp() {
        // game = new Game();
        // game.instanceGame();
    }

    @Test
    @DisplayName("Test getNodes method")
    void testGetNodes() {
        // assertEquals(29, game.getNodes().size());
        // Node node = game.getNodes().get(0);
        // assertEquals(190, node.getX());
        // assertEquals(160, node.getY());
    }

    @Test
    @DisplayName("Test getEdges method")
    void testGetEdges() {
        // assertEquals(78, game.getEdges().size());
        // Edge edge = game.getEdges().get(0);
        // assertEquals(6, edge.getValue());
    }

    @Test
    @DisplayName("Test getSolution method")
    void testGetSolution() {
        // assertEquals(13, game.getSolution(1).size());
        // Edge edge = game.getSolution(1).get(0);
        // assertEquals(1, edge.getValue());
    }

    @Test
    @DisplayName("Test getNodesToConnect method")
    void testGetNodesToConnect() {
        // assertEquals(5, game.getNodesToConnect(1).size());
        // assertEquals(1, (int) game.getNodesToConnect(1).get(0));
        // assertEquals(4, (int) game.getNodesToConnect(1).get(3));
    }

    @Test
    @DisplayName("Test getLevels method")
    void testGetLevels() {
        // assertEquals(3, game.getLevels().size());
        // String[] level = game.getLevels().get(0);
        // assertEquals(10, level.length);
        // assertEquals("1", level[0]);
        // assertEquals("2", level[1]);
        // assertEquals("-", level[2]);
        // assertEquals("5", level[3]);
        // assertEquals("6", level[4]);
        // assertEquals("7", level[5]);
        // assertEquals("-", level[6]);
        // assertEquals("-", level[7]);
        // assertEquals("15", level[8]);
        // assertEquals("16", level[9]);
    }

    @Test
    @DisplayName("Test getNumberOfLevels method")
    void testGetNumberOfLevels() {
        // assertEquals(3, game.getNumberOfLevels());
    }

    @Test
    @DisplayName("Test checkFinish method with correct edges")
    void testCheckFinishWithCorrectEdges() {
        // // Create a list of correct edges for a test level
        // List<Edge> solution = Arrays.asList(
        // new Edge(false, true, new Node(1), new Node(2)),
        // new Edge(false, true, new Node(2), new Node(3)),
        // new Edge(false, true, new Node(2), new Node(5)),
        // new Edge(false, true, new Node(3), new Node(4)),
        // new Edge(false, true, new Node(3), new Node(5)),
        // new Edge(false, true, new Node(4), new Node(6)),
        // new Edge(false, true, new Node(5), new Node(6))
        // );

        // // Create a list of selected edges for the same test level
        // List<Edge> selectedEdges = Arrays.asList(
        // new Edge(true, false, new Node(1), new Node(2)),
        // new Edge(false, false, new Node(2), new Node(3)),
        // new Edge(true, false, new Node(2), new Node(5)),
        // new Edge(false, false, new Node(3), new Node(4)),
        // new Edge(true, false, new Node(3), new Node(5)),
        // new Edge(false, false, new Node(4), new Node(6)),
        // new Edge(true, false, new Node(5), new Node(6))
        // );

        // // Check that checkFinish returns true for the correct edges
        // assertTrue(game.checkFinish(selectedEdges));
    }

    @Test
    @DisplayName("Test checkFinish method with incorrect edges")
    void testCheckFinishWithIncorrectEdges() {
        // // Create a list of correct edges for a test level
        // List<Edge> solution = Arrays.asList(
        // new Edge(false, true, new Node(1), new Node(2)),
        // new Edge(false, true, new Node(2), new Node(3)),
        // new Edge(false, true, new Node(2), new Node(5)),
        // new Edge(false, true, new Node(3), new Node(4)),
        // new Edge(false, true, new Node(3), new Node(5)),
        // new Edge(false, true, new Node(4), new Node(6)),
        // new Edge(false, true, new Node(5), new Node(6))
        // );

        // // Create a list of selected edges for the same test level, but with one
        // incorrect edge
        // List<Edge> selectedEdges = Arrays.asList(
        // new Edge(true, false, new Node(1), new Node(2)),
        // new Edge(false, false, new Node(2), new Node(3)),
        // new Edge(true, false, new Node(2), new Node(5)),
        // new Edge(false, false, new Node(3), new Node(4)),
        // new Edge(true, false, new Node(3), new Node(5)),
        // new Edge(false, false, new Node(4), new Node(6)),
        // new Edge(false, false, new Node(5), new Node(6))
        // );

        // // Check that checkFinish returns false for the incorrect edges
        // assertFalse(game.checkFinish(selectedEdges));
    }
}
