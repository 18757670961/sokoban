package Main;

import Model.HighScore;
import Utils.GameIO;
import Utils.GameLogger;
import View.WindowFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void init() throws FileNotFoundException {
        // preparation for game
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
        WindowFactory.setPrimaryStage(primaryStage); // pass primary stage to window factory
        WindowFactory.createStartMenu(); // show start page
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
