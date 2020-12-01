package Main;

import Controller.GameEngine;
import Controller.GameFile;
import Debug.GameLogger;
import Modal.GameStatus;
import Modal.HighScore;
import Modal.History;
import View.GameWindow;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 * The type Main.
 */
public class Main extends Application {
    @Override
    public void init() throws FileNotFoundException {
        GameFile.loadDefaultSaveFile();
        HighScore.loadMap();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameWindow.setPrimaryStage(primaryStage);
        GameWindow.createStartMenu();
        GameLogger.createLogger();
    }

    @Override
    public void stop() {
        HighScore.saveMap();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
