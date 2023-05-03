package ch.ladestation.connectncharge.model;

import ch.ladestation.connectncharge.services.file.CSVReader;

import java.util.*;

public class Game {
    private static final String HOUSE_FLAG = "H";
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    private Map<Integer, Map<Integer, Edge>> pinToEdgeLUT = new HashMap<>();

    private Map<Integer, Segment> segmentIdLUT = new HashMap<>();

    public void instanceSegments() {
        var records = CSVReader.readCSV();

        int runningTotal = 0;
        var retSegments = new ArrayList<Segment>();
        for (int i = 1; i < records.size(); i++) {
            var record = records.get(i);
            int startIndex = runningTotal;
            runningTotal += Integer.parseInt(record.get(1));
            int endIndex = runningTotal - 1;


            if (record.get(2).equals(HOUSE_FLAG)) {
                int segmentId = Integer.parseInt(record.get(0));
                var segment = new Node(segmentId, startIndex, endIndex);
                nodes.add(segment);
                segmentIdLUT.put(segmentId, segment);
            } else {
                int segmentId = Integer.parseInt(record.get(0));
                int chip = Integer.parseInt(record.get(2));
                int pin = Integer.parseInt(record.get(3));
                int cost = Integer.parseInt(record.get(4));
                int fromNode = Integer.parseInt(record.get(5));
                int toNode = Integer.parseInt(record.get(6));
                var segment = new Edge(segmentId, startIndex, endIndex, cost);
                edges.add(segment);
                segmentIdLUT.put(segmentId, segment);

                populateLUT(chip, pin, segment);

            }
        }
    }

    private void populateLUT(int chip, int pin, Edge segment) {
        pinToEdgeLUT.putIfAbsent(chip, new HashMap<>());
        pinToEdgeLUT.computeIfPresent(chip, (key, oldValue) -> {
            oldValue.put(pin, segment);
            return oldValue;
        });
    }

    public Edge lookUpChipAndPinNumberToEdge(int chipNo, int pinNo) {
        return pinToEdgeLUT.get(chipNo).get(pinNo);
    }

    public Segment lookUpSegmentIdToSegment(int segmentId) {
        return segmentIdLUT.get(segmentId);
    }
}
