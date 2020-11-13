package com.ae2dms;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * The type Main.
 */
public class Main extends Application {
    private GameWindow gameWindow;
    private String defaultSaveFile = "level/sampleGame.skb";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameWindow = new GameWindow(primaryStage);
        loadDefaultSaveFile();
    }

    /**
     * Load default save file.
     *
     * @param primaryStage the primary stage
     */
    private void loadDefaultSaveFile() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultSaveFile); // variable name changed
        gameWindow.initializeGame(inputStream);
        gameWindow.setEventFilter();
    }
}
