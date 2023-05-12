package ch.ladestation.connectncharge.controller;

import ch.ladestation.connectncharge.model.Edge;
import ch.ladestation.connectncharge.model.Game;
import ch.ladestation.connectncharge.model.Node;
import ch.ladestation.connectncharge.pui.GamePUI;
import ch.ladestation.connectncharge.services.file.TextFileEditor;
import ch.ladestation.connectncharge.util.mvcbase.ControllerBase;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class ApplicationController extends ControllerBase<Game> {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private Edge blinkingEdge;
    private Map<Integer, List<Object>> levels;
    private int currentLevel = 4;
    private static final int MAX_LEVEL = 5;
    private Node[] terms;
    private GamePUI gamePUI;
    Thread blinkThread;

    public ApplicationController(Game model) {
        super(model);
        model.activatedEdges.onChange((oldValue, newValue) -> {
            if (model.gameStarted.getValue()) {
                updateScore(Arrays.stream(newValue).mapToInt(Edge::getCost).sum());
                checkScore(Arrays.stream(newValue).mapToInt(Edge::getCost).sum());
            }
        });

        model.gameStarted.onChange(((oldValue, newValue) -> {
            if (oldValue && !newValue) {
                deactivateAllNodes();
                loadNextLevel();
            }
        }));
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

        List<Integer> terminals = (List<Integer>) level.get(0);
        List<List<Integer>> solution = (List<List<Integer>>) level.get(1);

        int[] terms = terminals.stream().mapToInt(j -> j).toArray();

        var terminalNodes = Arrays.stream(terms).mapToObj(gamePUI::lookUpSegmentIdToSegment).map(seg -> (Node) seg)
            .toArray(Node[]::new);
        setTerminals(terminalNodes);

        var solutionEdges =
            solution.stream().map((sol) -> gamePUI.lookUpEdge(sol.get(0), sol.get(1))).toArray(Edge[]::new);
        setSolution(solutionEdges);

        deactivateAllEdges();

        blinkThread = new Thread(() -> {
            startBlinkingEdge((Edge) gamePUI.lookUpSegmentIdToSegment(1));
        });
        blinkThread.start();
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
            if (edge == this.blinkingEdge) {
                setValue(model.gameStarted, true);
                setValues(model.terminals, this.terms);
                blinkThread.interrupt();
            }
            return;
        }
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

    public void edgeToggledByApp(Edge edge) {
        toggleEdge(edge);
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

    public void startBlinkingEdge(Edge edge) {
        if (model.gameStarted.getValue()) {
            return;
        }
        this.blinkingEdge = edge;
        async(() -> {
            edgeToggledByApp(edge);
        });
        pauseExecution(Duration.ofSeconds(1));
        async(() -> startBlinkingEdge(edge));
    }


    public void updateScore(int score) {
        setValue(model.currentScore, score);
    }

    public void checkScore(int score) {
        int solutionScore = Arrays.stream(model.solution.getValues()).mapToInt(Edge::getCost).sum();

        if (allTerminalsConnected()) {
            if (score <= solutionScore) {
                finishGame();
            }
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
        if (!model.gameStarted.getValue()) {
            this.terms = terms;
            return;
        }
        setValues(model.terminals, terms);
    }

    public void setSolution(Edge[] edges) {
        setValues(model.solution, edges);
    }

    private void saveUserScore() {

    }

    private void finishGame() {
        saveUserScore();
        increaseCurrentLevel();
        setValue(model.gameStarted, false);
    }
}
