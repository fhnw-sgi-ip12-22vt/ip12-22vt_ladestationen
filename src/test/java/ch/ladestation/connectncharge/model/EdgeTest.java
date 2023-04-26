package ch.ladestation.connectncharge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EdgeTest {

    // private Edge edge;
    // private Node node1;
    // private Node node2;

    @BeforeEach
    public void setUp() {
        // node1 = new Node(1, 0, 0);
        // node2 = new Node(2, 0, 0);
        // edge = new Edge(1, node1, node2, 10);
    }

    @Test
    public void testConstructorWithValues() {
        // assertEquals(node1, edge.getNode1());
        // assertEquals(node2, edge.getNode2());
        // assertEquals(10, edge.getValue());
    }

    @Test
    public void testConstructorWithFlags() {
        // edge = new Edge(true, false, node1, node2);
        // assertTrue(edge.isSelected());
        // assertFalse(edge.isSolution());
        // assertEquals(node1, edge.getNode1());
        // assertEquals(node2, edge.getNode2());
    }

    @Test
    public void testGetColorWhenTipp() {
        // edge.setTipp(true);
        // assertEquals(Color.GREEN, edge.getColor());
    }

    @Test
    public void testGetColorWhenSelected() {
        // edge.setSelected(true);
        // assertEquals(Color.RED, edge.getColor());
    }

    @Test
    public void testGetColorWhenNotTippOrSelected() {
        // assertEquals(Color.BLACK, edge.getColor());
    }

    @Test
    public void testIsSelected() {
        // assertFalse(edge.isSelected());
        // edge.setSelected(true);
        // assertTrue(edge.isSelected());
    }

    @Test
    public void testIsSolution() {
        // assertFalse(edge.isSolution());
        // edge.setSolution(true);
        // assertTrue(edge.isSolution());
    }

    @Test
    public void testIsTipp() {
        // assertFalse(edge.isTipp());
        // edge.setTipp(true);
        // assertTrue(edge.isTipp());
    }

    @Test
    public void testGetNode1() {
        // assertEquals(node1, edge.getNode1());
    }

    @Test
    public void testSetNode1() {
        // Node newNode = new Node(3, 0, 0);
        // edge.setNode1(newNode);
        // assertEquals(newNode, edge.getNode1());
    }

    @Test
    public void testGetNode2() {
        // assertEquals(node2, edge.getNode2());
    }

    @Test
    public void testSetNode2() {
        // Node newNode = new Node(4, 0, 0);
        // edge.setNode2(newNode);
        // assertEquals(newNode, edge.getNode2());
    }

    @Test
    public void testGetValue() {
        // assertEquals(10, edge.getValue());
    }

    @Test
    public void testSetValue() {
        // edge.setValue(20);
        // assertEquals(20, edge.getValue());
    }
}
