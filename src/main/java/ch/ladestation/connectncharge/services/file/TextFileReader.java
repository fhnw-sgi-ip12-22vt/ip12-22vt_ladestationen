package ch.ladestation.connectncharge.services.file;

import ch.ladestation.connectncharge.model.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class TextFileReader {

    private static final String LEVEL_DIRECTORY_PATH = "src/main/resources/textfiles/levels";

    private static final int NUMBER_OF_LEVELS = 5;

    private TextFileReader() {
    }

    public static Map<Integer, List<Object>> readLevels() throws IOException {
        Map<Integer, List<Object>> levels = new HashMap<>();

        for (int i = 1; i < NUMBER_OF_LEVELS + 1; i++) {
            int levelNumber = i;
            InputStream level = TextFileReader.class.getResourceAsStream("/textfiles/levels/" + i + ".txt");
            Objects.requireNonNull(level, "error, " + "/textfiles/levels/" + i + ".txt" + "file was null");
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(level, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            List<Integer> terminals = new ArrayList<>();
            List<List<Integer>> solution = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(" ");
                if (parts[0].equals("T")) {
                    terminals.add(Integer.parseInt(parts[1]));
                } else {
                    List<Integer> connection = new ArrayList<>();
                    connection.add(Integer.parseInt(parts[0]));
                    connection.add(Integer.parseInt(parts[1]));
                    solution.add(connection);
                }
            }
            List<Object> levelContent = new ArrayList<>();
            levelContent.add(terminals);
            levelContent.add(solution);
            levels.put(levelNumber, levelContent);

        }

        return levels;
    }

    private static List<String> readLevel(String levelFilePath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(levelFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    public static List<Player> readPlayerDataFromFile(String filePath) {
        List<Player> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String playerName = data[1];
                    String score = data[2];
                    Player player = new Player(playerName, score);
                    players.add(player);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return players;
    }
}
