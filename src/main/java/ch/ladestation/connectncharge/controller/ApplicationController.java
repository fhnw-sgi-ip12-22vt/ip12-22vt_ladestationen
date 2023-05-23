package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.game.gamelogic.Edge;
import ch.ladestation.connectncharge.model.game.gamelogic.Game;
import ch.ladestation.connectncharge.model.game.gamelogic.Node;
import ch.ladestation.connectncharge.model.game.gamelogic.Hint;
import ch.ladestation.connectncharge.pui.GamePUI;
import ch.ladestation.connectncharge.services.file.TextFileEditor;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;
import com.github.mbelling.ws281x.Color;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ApplicationController extends ControllerBase<Game> {
    private static final int MAX_LEVEL = 5;
    private final Logger logger = Logger.getLogger(getClass().getName());
    public boolean firstBootup = true;
    private Map<Integer, List<Object>> levels;
    private int currentLevel = 1;
    private GamePUI gamePUI;
    private boolean isToBeRemoved = false;
    private Edge tippEdge;
    private ScheduledExecutorService blinkingEdgeScheduler;

    public ApplicationController(Game model) {
        super(model);

        model.activatedEdges.onChange((oldValue, newValue) -> {
            if (!model.gameStarted.getValue()) {
                return;
            }

            updateScore(Arrays.stream(newValue).mapToInt(Edge::getCost).sum());
            checkScore(Arrays.stream(newValue).mapToInt(Edge::getCost).sum());

            setValue(model.hasCycle, hasCycle());

        });

        model.hasCycle.onChange((oldValue, newValue) -> {
            if (newValue) {
                addHint(Hint.HINT_CYCLE);
            } else {
                removeHint(Hint.HINT_CYCLE);
            }
        });

        model.gameStarted.onChange(((oldValue, newValue) -> {
            if (oldValue && !newValue && !model.isFinished.getValue()) {
                increaseCurrentLevel();
                loadNextLevel();
                setValue(model.isCountdownFinished, false);
            }
        }));

        model.isTippOn.onChange(((oldValue, newValue) -> {
            if (newValue) {
                addHint(isToBeRemoved ? Hint.HINT_REMOVE_EDGE : Hint.HINT_PICK_EDGE);
            } else if (oldValue) {
                model.tippEdge.setColor(Color.GREEN);
                removeHint(isToBeRemoved ? Hint.HINT_REMOVE_EDGE : Hint.HINT_PICK_EDGE);
            }
        }));

        model.isCountdownFinished.onChange((oldValue, newValue) -> {
            if (!oldValue && newValue) {
                instanceTerminals();
                toggleIgnoreInputs();
            }
        });

        model.isFinished.onChange(((oldValue, newValue) -> {
            if (!oldValue && newValue) {
                model.ignoringInputs = true;
            } else if (oldValue && !newValue) {
                model.ignoringInputs = false;
            }
        }));

        model.activeHints.onChange((oldValue, newValue) -> {
            if (!Arrays.stream(model.activeHints.getValues()).toList().isEmpty()) {
                setValue(model.activeHint,
                    Arrays.stream(model.activeHints.getValues()).min(Comparator.comparingInt(Hint::getPriority)).get());
            } else {
                setValue(model.activeHint, Hint.HINT_EMPTY_HINT);
            }
        });
    }

    public void setGPUI(GamePUI gamePUI) {
        this.gamePUI = gamePUI;
    }


    public void loadLevels() {
        try {
            levels = TextFileEditor.readLevels();
        } catch (IOException ioException) {
            throw new RuntimeException("error: levels could not be read!");
        }
    }

    public void loadNextLevel() {
        List<Object> level = levels.get(currentLevel);

        List<List<Integer>> solution = (List<List<Integer>>) level.get(1);

        var solutionEdges =
            solution.stream().map((sol) -> gamePUI.lookUpEdge(sol.get(0), sol.get(1))).toArray(Edge[]::new);
        setSolution(solutionEdges);

        model.blinkingEdge = (Edge) gamePUI.lookUpSegmentIdToSegment(90);
        startBlinkingEdge();

    }

    public void toggleIgnoreInputs() {
        model.ignoringInputs = !model.ignoringInputs;
    }

    private void instanceTerminals() {
        List<Object> level = levels.get(currentLevel);
        List<Integer> terminals = (List<Integer>) level.get(0);
        int[] terms = terminals.stream().mapToInt(j -> j).toArray();
        var terminalNodes =
            terminals.stream().map(gamePUI::lookUpSegmentIdToSegment).map(seg -> (Node) seg).toArray(Node[]::new);
        setTerminals(terminalNodes);
    }

    public void setCountdownFinished() {
        setValue(model.isCountdownFinished, true);
    }

    private void increaseCurrentLevel() {
        if (currentLevel + 1 > MAX_LEVEL) {
            currentLevel = 1;
        } else {
            currentLevel++;
        }
    }

    public void edgePressed(Edge edge) {
        if (!model.gameStarted.getValue()) {
            if (edge == model.blinkingEdge) {
                setValue(model.isEdgeBlinking, false);
                blinkingEdgeScheduler.shutdown();
                setValue(model.gameStarted, true);
                toggleIgnoreInputs();
            }
            return;
        }
        if (model.ignoringInputs) {
            return;
        }
        if (edge != null && model.isTippOn.getValue()) {
            if (edge.equals(model.tippEdge) && !isToBeRemoved) {
                deactivateEdge(edge);
            }
        }
        removeTippEdge();
        toggleEdge(edge);
    }

    private void toggleEdge(Edge edge) {
        if (edge != null) {
            if (!edge.isOn()) {
                activateEdge(edge);
            } else {
                deactivateEdge(edge);
            }
        }
    }

    private void activateEdge(Edge edge) {
        edge.on();
        Edge[] oldValues = model.activatedEdges.getValues();
        Edge[] newValues = new Edge[oldValues.length + 1];
        System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
        newValues[newValues.length - 1] = edge;
        setValues(model.activatedEdges, newValues);
    }

    private void deactivateEdge(Edge edge) {
        edge.off();
        Edge[] oldValues = model.activatedEdges.getValues();
        Edge[] newValues = Arrays.stream(oldValues).filter(curr -> curr != edge).toArray(Edge[]::new);
        setValues(model.activatedEdges, newValues);
    }

    private void deactivateAllEdges() {
        setValues(model.activatedEdges, new Edge[0]);
    }

    private void deactivateAllNodes() {
        setValues(model.terminals, new Node[0]);
    }

    public void startBlinkingEdge() {
        blinkingEdgeScheduler = Executors.newScheduledThreadPool(1);
        blinkingEdgeScheduler.scheduleAtFixedRate(() -> toggleValue(model.isEdgeBlinking), 0, 1, TimeUnit.SECONDS);
    }


    public void updateScore(int score) {
        setValue(model.currentScore, score);
    }

    public void checkScore(int score) {
        int solutionScore = Arrays.stream(model.solution.getValues()).mapToInt(Edge::getCost).sum();

        if (allTerminalsConnected()) {
            if (score <= solutionScore) {
                try {
                    finishGame();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                addHint(Hint.HINT_SOLUTION_NOT_FOUND);
            }
        } else {
            removeHint(Hint.HINT_SOLUTION_NOT_FOUND);
        }
    }

    private boolean allTerminalsConnected() {
        Set<Node> visitedNodes = new HashSet<>();
        List<Edge> edges = Arrays.asList(model.activatedEdges.getValues());

        if (!edges.isEmpty()) {
            Edge firstEdge = edges.get(0);
            Node startNode = firstEdge.getFromNode();
            if (startNode == null) {
                startNode = firstEdge.getToNode();
            }

            Stack<Node> stack = new Stack<>();
            stack.push(startNode);

            while (!stack.isEmpty()) {
                Node currentNode = stack.pop();
                visitedNodes.add(currentNode);

                boolean allTerminalsConnected = true;
                for (Node terminal : Arrays.asList(model.terminals.getValues())) {
                    if (!visitedNodes.contains(terminal)) {
                        allTerminalsConnected = false;
                        break;
                    }
                }
                if (allTerminalsConnected) {
                    return true;
                }

                for (Edge edge : edges) {
                    if (edge.getFromNode() == currentNode && !visitedNodes.contains(edge.getToNode())) {
                        stack.push(edge.getToNode());
                    } else if (edge.getToNode() == currentNode && !visitedNodes.contains(edge.getFromNode())) {
                        stack.push(edge.getFromNode());
                    }
                }
            }
        }

        return false;
    }

    public void setTerminals(Node[] terms) {
        setValues(model.terminals, terms);
    }

    public void setSolution(Edge[] edges) {
        setValues(model.solution, edges);
    }

    public void handleTipp() {
        setTippEdge();
    }

    public void setTippEdge() {
        List<Edge> edgesToSelect;
        List<Edge> edgesToRemove;

        edgesToSelect = Arrays.stream(model.solution.getValues())
            .filter(solEdge -> !Arrays.stream(model.activatedEdges.getValues()).toList().contains(solEdge)).toList();

        edgesToRemove = Arrays.stream(model.activatedEdges.getValues())
            .filter((activatedEdge) -> !Arrays.stream(model.solution.getValues()).toList().contains(activatedEdge))
            .toList();

        if (!edgesToSelect.isEmpty()) {
            tippEdge = getRandomEdge(edgesToSelect);
            isToBeRemoved = false;
        } else {
            tippEdge = getRandomEdge(edgesToRemove);
            isToBeRemoved = true;
        }

        tippEdge.setColor(isToBeRemoved ? Color.RED : Color.ORANGE);
        model.tippEdge = tippEdge;

        setValue(model.isTippOn, true);
    }

    private Edge getRandomEdge(List<Edge> edges) {
        return edges.stream().skip(new Random().nextInt(edges.size())).findFirst().get();
    }

    public void removeTippEdge() {
        setValue(model.isTippOn, false);
    }

    private boolean hasCycle() {
        // Create an adjacency list to store the nodes and their neighbors
        Map<Node, List<Node>> adjList = new HashMap<>();

        // Create a list of selected edges
        List<Edge> selectedEdges = Arrays.stream(model.activatedEdges.getValues()).toList();

        // If there are less than 2 selected edges, no cycle can be formed
        if (selectedEdges.size() < 2) {
            return false;
        }

        // Create the adjacency list by adding the nodes and their neighbors from the
        // selected edges
        for (Edge edge : selectedEdges) {
            Node node1 = edge.getFromNode();
            Node node2 = edge.getToNode();
            if (!adjList.containsKey(node1)) {
                adjList.put(node1, new ArrayList<>());
            }
            if (!adjList.containsKey(node2)) {
                adjList.put(node2, new ArrayList<>());
            }
            adjList.get(node1).add(node2);
            adjList.get(node2).add(node1);
        }

        // Create a set to keep track of visited nodes and a map to keep track of their
        // parent node in the DFS tree
        Set<Node> visited = new HashSet<>();
        Map<Node, Node> parent = new HashMap<>();

        //get all the edges that haven't been visited yet
        var islands =
            selectedEdges.stream().flatMap(e -> Stream.of(e.getFromNode(), e.getToNode())).distinct().toList();
        while (islands.size() > 0) {
            // Create a stack to perform depth-first search starting from the first node in
            // the first selected edge
            Stack<Node> stack = new Stack<>();
            Node startNode = islands.get(0);
            stack.push(startNode);
            parent.put(startNode, null);
            while (!stack.empty()) {
                Node currNode = stack.pop();
                visited.add(currNode);
                List<Node> neighbors = adjList.get(currNode);
                for (Node neighbor : neighbors) {
                    // If the neighbor node has not been visited, add it to the stack and set its
                    // parent to the current node
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                        parent.put(neighbor, currNode);
                    } else if (parent.get(currNode) != neighbor) {
                        return true;
                    }
                }
            }
            islands = selectedEdges.stream().flatMap(e -> Stream.of(e.getFromNode(), e.getToNode()))
                .filter(n -> !visited.contains(n)).distinct().toList();
        }

        // No cycle is formed
        return false;
    }

    private void finishGame() throws IOException {
        setValue(model.isFinished, true);
    }

    public void playAgain() {
        setValue(model.isFinished, false);
        deactivateAllEdges();
        deactivateAllNodes();
        setValue(model.gameStarted, false);
    }

    public void setEndTime(String endTime) {
        setValue(model.endTime, endTime);
    }

    public void addHint(Hint hint) {
        if (!Arrays.stream(model.activeHints.getValues()).toList().contains(hint)) {
            Hint[] oldValues = model.activeHints.getValues();
            Hint[] newValues = new Hint[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[newValues.length - 1] = hint;
            setValues(model.activeHints, newValues);
        }
    }

    public void removeHint(Hint hint) {
        if (Arrays.stream(model.activeHints.getValues()).toList().contains(hint)) {
            Hint[] oldValues = model.activeHints.getValues();
            Hint[] newValues = Arrays.stream(oldValues).filter(curr -> curr != hint).toArray(Hint[]::new);
            setValues(model.activeHints, newValues);
        }
    }
}
