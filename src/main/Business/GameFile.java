package Business;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// game file IO extracted
public class GameFile {
    private File file;
    private FileChooser fileChooser;

    public GameFile() {
        fileChooser = new FileChooser();
    }

    /**
     * Save game file.
     */
    public void saveGameFile(Stage primaryStage) {
        fileChooser.setTitle("Save File to");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        file = fileChooser.showSaveDialog(primaryStage);

        if (file != null && GameEngine.isDebugActive()) {
            GameEngine.getLogger().info("Saving file: " + file.getName());
        }
    }

    /**
     * Load game file.
     *
     * @throws FileNotFoundException the file not found exception
     */
    public FileInputStream loadGameFile(Stage primaryStage) throws FileNotFoundException {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        file = fileChooser.showOpenDialog(primaryStage);

        if (file != null && GameEngine.isDebugActive()) {
            GameEngine.getLogger().info("Loading save file: " + file.getName());
        }

        return new FileInputStream(file);
    }

    // accept parameter object fileInfo
    public static void readGameFile(FileInfo fileInfo) throws IOException {
        while (true) {
            String line = fileInfo.reader.readLine();

            if (line == null) {
                if (fileInfo.rawLevel.size() != 0) {
                    Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
                    fileInfo.levels.add(parsedLevel);
                }
                break;
            }

            if (line.contains("MapSetName")) {
                GameEngine.setMapSetName(line.replace("MapSetName: ", ""));
                continue;
            }

            if (line.contains("LevelName")) {
                if (fileInfo.parsedFirstLevel) {
                    Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
                    fileInfo.levels.add(parsedLevel);
                    fileInfo.rawLevel.clear();
                } else {
                    fileInfo.parsedFirstLevel = true;
                }

                fileInfo.levelName = line.replace("LevelName: ", "");
                continue;
            }

            line = line.trim();
            line = line.toUpperCase();
            if (line.matches(".*W.*W.*")) {
                fileInfo.rawLevel.add(line);
            }
        }
    }

    /**
     * Load game file list.
     *
     * @param input the input
     * @return the list
     */
    // method name changed
    public static final List<Level> prepareFileReader(InputStream input) {
        List<Level> levels = new ArrayList<>(5);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            int levelIndex = 0;
            readGameFile(new FileInfo(levels, levelIndex, reader, parsedFirstLevel, rawLevel, levelName));

        } catch (IOException e) {
            GameEngine.getLogger().severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            GameEngine.getLogger().severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    private static class FileInfo {
        private final List<Level> levels;
        private int levelIndex;
        private final BufferedReader reader;
        private boolean parsedFirstLevel;
        private final List<String> rawLevel;
        private String levelName;

        private FileInfo(List<Level> levels, int levelIndex, BufferedReader reader, boolean parsedFirstLevel, List<String> rawLevel, String levelName) {
            this.levels = levels;
            this.levelIndex = levelIndex;
            this.reader = reader;
            this.parsedFirstLevel = parsedFirstLevel;
            this.rawLevel = rawLevel;
            this.levelName = levelName;
        }
    }
}
