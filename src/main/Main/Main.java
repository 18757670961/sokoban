package Main;

import Debug.GameLogger;
import View.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * The type Main.
 */
public class Main extends Application {
    private GameWindow gameWindow;

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
        GameLogger.createLogger();
    }
}
