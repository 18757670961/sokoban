package Main;

import Controller.GameEngine;
import Debug.GameLogger;
import Modal.HighestScore;
import View.GameWindow;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * The type Main.
 */
public class Main extends Application {
    /**
     * The Game window.
     */
    private GameWindow gameWindow;

    private String defaultSaveFile = "level/sampleGame.skb";

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadDefaultSaveFile();
        HighestScore.loadMap();

        GameWindow.createGameWindow(primaryStage);
        GameLogger.createLogger();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                HighestScore.saveMap();
            }
        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Load default save file.
     *
     * @param primaryStage the primary stage
     */
    private void loadDefaultSaveFile() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultSaveFile); // variable name changed
        GameEngine.createGameEngine(inputStream);
    }
}
