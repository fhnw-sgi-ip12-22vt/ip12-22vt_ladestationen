package ch.ladestation.connectncharge.services.file;

import ch.ladestation.connectncharge.model.Player;
import ch.ladestation.connectncharge.util.mvcbase.MvcLogger;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class TextFileEditor {
    private static final MvcLogger LOGGER = new MvcLogger();

    private static final int NUMBER_OF_LEVELS = 5;

    private TextFileEditor() {
    }

    public static Map<Integer, List<Object>> readLevels() throws IOException {
        Map<Integer, List<Object>> levels = new HashMap<>();

        for (int i = 1; i < NUMBER_OF_LEVELS + 1; i++) {
            int levelNumber = i;
            InputStream level = TextFileEditor.class.getResourceAsStream("/textfiles/levels/" + i + ".txt");
            Objects.requireNonNull(level, "error, " + "/textfiles/levels/" + i + ".txt" + "file was null");

            List<String> lines = readFile(level);

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

    private static List<String> readFile(InputStream filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static List<Player> readPlayerDataFromFile(String filePath) {
        List<Player> players = new ArrayList<>();
        if (!createPlayerFile(filePath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    players.add(new Player(line.split(",")[0], line.split(",")[1]));
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }
        }
        return players;
    }

    private static boolean createPlayerFile(String fileName) {
        String filePath =
            File.separator + "home" + File.separator + "pi" + File.separator + "deploy" + File.separator + fileName;

        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                System.out.println("File created successfully.");
                return true;
            } else {
                System.out.println("File already exists.");
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
        return false;
    }

    public static void writeTextFile(String filePath, List<String> lines) {
        try {
            URL resourceUrl = TextFileEditor.class.getResource(filePath);
            if (resourceUrl == null) {
                LOGGER.logError("Resource not found: " + filePath);
                return;
            }

            File file = new File(resourceUrl.toURI());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.logError("An error has occurred while writing the player file. " + e);
        }

        /*try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.logError("An error has occurred while writing the player file. " + e);
        }*/

        /*

            InputStream inputStream = TextFileEditor.class.getResourceAsStream(filePath);
            OutputStream outputStream = new FileOutputStream(filePath);
        try {
            InputStream initialStream = new FileInputStream(
                new File(filePath));
            File targetFile = new File(filePath);
            OutputStream outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = initialStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOGGER.logError("An error has occurred while writing the player file. " + e);
        }*/
    }
}
