package Utils;

import Model.GameStatus;
import Model.History;
import Model.Level;
import View.WindowFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * GameIO is a singleton class responsible for game file loading and saving using serialization
 */
public final class GameIO {

    /**
     * The fileChooser.
     */
    private static FileChooser fileChooser;
    /**
     * The url of defaultSaveFile.
     */
    private static String defaultSaveFile = "./src/main/resources/level/sampleGame.skb";

    /**
     * create file object from uri
     *
     * @param uri the uri
     * @return the file
     */
    public static File getFile(String uri) {
        return new File(uri);
    }

    /**
     * Load default save file.
     */
    public static void loadDefaultSaveFile() {
        FileInputStream inputStream = null; // clear stream
        try {
            inputStream = new FileInputStream(getFile(defaultSaveFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GameStatus.createGameStatus(inputStream);
    }

    /**
     * Choose game file to be loaded
     *
     * @return the object (GameStatus / FileInputStream)
     */
    public static Object chooseGameFile() {
        try {
            Object fileInput;
            fileInput = loadGameFile(WindowFactory.getPrimaryStage());

            if (fileInput != null) {
                if (fileInput instanceof GameStatus) { // load from binary game save file (serialized)
                    GameStatus.createGameStatus((GameStatus) fileInput);
                } else { // load from textual map data file
                    GameStatus.createGameStatus((FileInputStream) fileInput);
                }
                History.getHistory().clear();
                System.out.println(GameStatus.getGameStatus().getCurrentLevel());
            }

            return fileInput;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * factory method for creating file chooser.
     *
     * @param title the file title
     * @param type  the type (0 for saving file, 1 for loading file)
     */
    public static void createFileChooser(String title, int type) {
        fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (type == 0) {
            // save game
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.dat"));
        } else {
            // load game
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

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                // serialize gameStatus
                serialize(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Serialize GameStatus object
     *
     * @param out the out
     * @throws IOException the io exception
     */
    private static void serialize(ObjectOutputStream out) throws IOException {
        List<Level> levelList = GameStatus.getGameStatus().getLevels();
        Level[] levelArray = new Level[levelList.size()];
        levelList.toArray(levelArray);
        GameStatus.getGameStatus().setSerializableLevels(levelArray); // level list -> level array
        out.writeObject(GameStatus.getGameStatus());
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
            if (fileExtension.equals(".dat")) { // binary file
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    // deserialize gameStatus
                    return deserialize(in);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else { // text file
                return new FileInputStream(file);
            }
        }

        return null;
    }

    /**
     * Deserialize GameStatus object
     *
     * @param in the in
     * @return the game engine
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    private static GameStatus deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
        GameStatus gameStatus = (GameStatus) in.readObject();
        gameStatus.setLevels(Arrays.asList(gameStatus.getSerializableLevels())); // level array -> level list
        gameStatus.setSerializableLevels(null); // clean up level array
        in.close();
        return gameStatus;
    }
}
