package Main;

import Utils.GameFile;
import Utils.GameLogger;
import Modal.HighScore;
import View.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

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
