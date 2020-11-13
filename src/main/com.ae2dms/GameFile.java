package com.ae2dms;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
}
