package ch.ladestation.connectncharge.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// import ch.ladestation.connectncharge.model.Node;

public class NodeTest {

    @Test
    @DisplayName("Test Node constructor and getters")
    void testNodeConstructorAndGetters() {
        // Node node = new Node(1, 100, 200);
        // assertEquals(1, node.getNumber());
        // assertEquals(100, node.getX());
        // assertEquals(200, node.getY());
    }

    @Test
    @DisplayName("Test Node setters")
    void testNodeSetters() {
        // Node node = new Node(1, 100, 200);
        // node.setNumber(2);
        // node.setX(300);
        // node.setY(400);
        // assertEquals(2, node.getNumber());
        // assertEquals(300, node.getX());
        // assertEquals(400, node.getY());
    }

    @Test
    @DisplayName("Test Node toString method")
    void testNodeToString() {
        // Node node = new Node(1, 100, 200);
        // assertEquals("1", node.toString());
    }

    @Test
    @DisplayName("Test equals method")
    void testNodeEquals() {
        // Node node1 = new Node(1, 100, 200);
        // Node node2 = new Node(1, 200, 300);
        // Node node3 = new Node(2, 100, 200);

        // assertFalse(node1.equals(node2));
        // assertFalse(node1.equals(node3));
    }

    @Test
    @DisplayName("Test hashCode method")
    void testNodeHashCode() {
        // Node node1 = new Node(1, 100, 200);
        // Node node2 = new Node(1, 200, 300);
        // Node node3 = new Node(2, 100, 200);

        // assertNotEquals(node1.hashCode(), node2.hashCode());
        // assertNotEquals(node1.hashCode(), node3.hashCode());
    }
}
