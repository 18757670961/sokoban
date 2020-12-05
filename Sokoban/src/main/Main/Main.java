package Main;

import Utils.GameIO;
import Utils.GameLogger;
import Modal.HighScore;
import View.GameWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * The type Main.
 */
public class Main extends Application {
    @Override
    public void init() throws FileNotFoundException {
        GameIO.loadDefaultSaveFile();
        HighScore.loadMap();
        try {
            GameLogger.createLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameWindow.setPrimaryStage(primaryStage);
        GameWindow.createStartMenu();
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
