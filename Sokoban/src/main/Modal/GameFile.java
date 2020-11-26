package Modal;

import Debug.GameLogger;
import Controller.GameEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * singleton class
 */
// game file IO extracted
public final class GameFile {
    /**
     * The constant fileChooser.
     */
    private static FileChooser fileChooser;

    /**
     * Load file file.
     *
     * @param uri the uri
     * @return the file
     */
    public static File getFile(String uri) { return new File(uri); }

    /**
     * Create file chooser.
     */
// factory method
    public static void createFileChooser(String title, int type) {
        fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (type == 0) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.dat"));
        } else {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb", "*.dat"));
        }
        fileChooser.setInitialDirectory(getFile("./src/main/resources/level"));
    }

    /**
     * Save game file.
     *
     * @param primaryStage the primary stage
     */
    public static void saveGameFile(Stage primaryStage) {
        createFileChooser("Save File to", 0);
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            if (GameLogger.isDebugActive()) {
                GameLogger.showInfo("Saving file: " + file.getName());
            }

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
            {
                // serialize gameEngine
                serialize(out);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Serialize.
     *
     * @param out the out
     * @throws IOException the io exception
     */
    private static void serialize(ObjectOutputStream out) throws IOException {
        List<Level> levelList = GameEngine.getGameEngine().getLevels();
        Level[] levelArray = new Level[levelList.size()];
        levelList.toArray(levelArray);
        GameEngine.getGameEngine().setSerializableLevels(levelArray); // level list -> level array
        out.writeObject(GameEngine.getGameEngine());
        out.flush();
        out.close();
    }

    /**
     * Load game file.
     *
     * @param primaryStage the primary stage
     * @return the object
     * @throws FileNotFoundException the file not found exception
     */
    public static Object loadGameFile(Stage primaryStage) throws FileNotFoundException {
        createFileChooser("Open Save File", 1);
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            if (GameLogger.isDebugActive()) {
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

    /**
     * Deserialize game engine.
     *
     * @param in the in
     * @return the game engine
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    private static GameEngine deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
        GameEngine gameEngine = (GameEngine) in.readObject();
        gameEngine.setLevels(Arrays.asList(gameEngine.getSerializableLevels())); // level array -> level list
        gameEngine.setSerializableLevels(null); // clean up level array
        in.close();
        return gameEngine;
    }
}
