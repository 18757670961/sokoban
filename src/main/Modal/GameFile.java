package Modal;

import Debug.GameLogger;
import Controller.GameEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// game file IO extracted
public class GameFile {
    private File file;
    private FileChooser fileChooser;

    public GameFile() {
        createFileChooser();
    }

    // factory method
    private void createFileChooser() {
        fileChooser = new FileChooser();
    }

    /**
     * Save game file.
     */
    public void saveGameFile(Stage primaryStage, GameEngine gameEngine) {
        createFileChooser();
        fileChooser.setTitle("Save File to");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.dat"));
        fileChooser.setInitialDirectory(new File("./src/main/resources/level"));
        file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            if (GameEngine.isDebugActive()) {
                GameLogger.showInfo("Saving file: " + file.getName());
            }

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
            {
                // serialize gameEngine
                serialize(gameEngine, out);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void serialize(GameEngine gameEngine, ObjectOutputStream out) throws IOException {
        List<Level> levelList = gameEngine.getLevels();
        Level[] levelArray = new Level[levelList.size()];
        levelList.toArray(levelArray);
        gameEngine.setSerializableLevels(levelArray); // level list -> level array
        out.writeObject(gameEngine);
    }

    /**
     * Load game file.
     *
     * @throws FileNotFoundException the file not found exception
     */
    public Object loadGameFile(Stage primaryStage) throws FileNotFoundException {
        createFileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb", "*.dat"));
        fileChooser.setInitialDirectory(new File("./src/main/resources/level"));
        file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            if (GameEngine.isDebugActive()) {
                GameLogger.showInfo("Loading save file: " + file.getName());
            }

            String fileExtension = file.getName().substring(file.getName().lastIndexOf("."));
            if (fileExtension.equals(".dat")) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file)))
                {
                    // deserialize gameEngine
                    return deserialize(in);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            } else {
                return new FileInputStream(file);
            }
        }

        return null;
    }

    private GameEngine deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
        GameEngine gameEngine = (GameEngine) in.readObject();
        gameEngine.setLevels(Arrays.asList(gameEngine.getSerializableLevels())); // level array -> level list
        gameEngine.setSerializableLevels(null); // clean up level array
        return gameEngine;
    }

    // accept parameter object fileInfo
    public static void readGameFile(FileInfo fileInfo) throws IOException {
        while (true) {
            String line = fileInfo.reader.readLine();

            if (line == null) {
                parseFinalLevel(fileInfo);
                break;
            }

            if (line.contains("MapSetName")) {
                GameEngine.setMapSetName(line.replace("MapSetName: ", ""));
                continue;
            }

            if (line.contains("LevelName")) {
                parseLevel(fileInfo, line);
                continue;
            }

            line = line.trim();
            line = line.toUpperCase();
            if (line.matches(".*W.*W.*")) {
                fileInfo.rawLevel.add(line);
            }
        }
    }

    // method extracted
    private static void parseFinalLevel(FileInfo fileInfo) {
        if (fileInfo.rawLevel.size() != 0) {
            Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
            fileInfo.levels.add(parsedLevel);
        }
    }

    // method extracted
    private static void parseLevel(FileInfo fileInfo, String line) {
        if (fileInfo.parsedFirstLevel) {
            Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
            fileInfo.levels.add(parsedLevel);
            fileInfo.rawLevel.clear();
        } else {
            fileInfo.parsedFirstLevel = true;
        }

        fileInfo.levelName = line.replace("LevelName: ", "");
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
            GameLogger.showSevere("Error trying to load the game file: " + e);
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            GameLogger.showSevere("Cannot open the requested file: " + e);
            System.out.println(e.getMessage());
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
