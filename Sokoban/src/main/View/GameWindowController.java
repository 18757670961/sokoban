package View;

import Controller.GameEngine;
import Controller.KeyHandler;
import Modal.GameStatus;
import Modal.History;
import Modal.Level;
import Utils.GameIO;
import Utils.GameLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameWindowController implements Initializable {
    private Stage primaryStage = GameWindow.getPrimaryStage();
    private static MediaPlayer mediaPlayer = new MediaPlayer(new Media(GameIO.getFile("src/main/resources/music/bgm.wav").toURI().toString()));
    @FXML private GridPane gameGrid;
    @FXML private MenuItem menuAbout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadGrid();

        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                Level level = KeyHandler.handleKey(event.getCode());
                if (getGameStatus().getMovesCountLevel() == 0) {
                    reloadGrid();
                } else {
                    reloadPartialGrid(level);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    private GameStatus getGameStatus() {
        return GameStatus.getGameStatus();
    }

    private void resizeWindow() {
        gameGrid.autosize();
        GameWindow.getPrimaryStage().sizeToScene();
    }

    public void reloadPartialGrid(Level level) {
        if (getGameStatus().isGameComplete()) {
            GameEngine.navigateToStart();
            return;
        }

        Level currentLevel = getGameStatus().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        Level previousLevel = (level == null ? History.getHistory().peek() : level);
        Level.LevelIterator oldLevelGridIterator = (Level.LevelIterator) previousLevel.iterator();
        addPartialObjects(newLevelGridIterator, oldLevelGridIterator);

        resizeWindow();
    }

    private void addPartialObjects(Level.LevelIterator newLevelGridIterator, Level.LevelIterator oldLevelGridIterator) {
        while (newLevelGridIterator.hasNext()) {
            char oldObject = oldLevelGridIterator.next();
            char newObject = newLevelGridIterator.next();
            Point newObjectPosition = newLevelGridIterator.getCurrentPosition();
            if (oldObject != newObject) {
                addObjectToGrid(newObject, newObjectPosition);
            }
        }
    }

    /**
     * Reload grid.
     */
    public void reloadGrid() {
        if (getGameStatus().isGameComplete()) {
            GameEngine.navigateToStart();
            return;
        }

        Level currentLevel = getGameStatus().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (newLevelGridIterator.hasNext()) {
            char newObject = newLevelGridIterator.next();
            Point newObjectPosition = newLevelGridIterator.getCurrentPosition();
            addObjectToGrid(newObject, newObjectPosition);
        }

        resizeWindow();
    }

    /**
     * Add object to grid.
     *
     * @param gameObject the game object
     * @param location   the location
     */
    private void addObjectToGrid(char gameObject, Point location) {
//        GraphicObject graphicObject = new GraphicObject(gameObject);
//        gameGrid.add(graphicObject, location.y, location.x);
        ImageView img = ImageFactory.chooseImage(gameObject);
        if (img != null) {
            gameGrid.add(img, location.y, location.x);
        }
    }

    @FXML
    void showAbout(ActionEvent event) {
        GameDialog.showAbout();
    }

    @FXML
    void undo(ActionEvent event) {
        reloadPartialGrid(History.traceHistory());
    }

    @FXML
    void toggleMusic(ActionEvent event) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }

    @FXML
    void toggleDebug(ActionEvent event) {
        GameLogger.toggleDebug();
    }

    @FXML
    void reset(ActionEvent event) {
        reloadPartialGrid(History.resetHistory());
    }

    @FXML
    void toPreviousLevel(ActionEvent event) {
        GameEngine.toPreviousLevel();
    }

    @FXML
    void toNextLevel(ActionEvent event) {
        GameEngine.toNextLevel();
    }

    @FXML
    void saveGame(ActionEvent event) {
        GameIO.saveGameFile(primaryStage);
    }

    @FXML
    void loadGame(ActionEvent event) {
        try {
            Object fileInput;
            fileInput = GameIO.loadGameFile(primaryStage);

            if (fileInput != null) {
                if (fileInput instanceof GameStatus) {
                    GameStatus.createGameStatus((GameStatus) fileInput);
                } else {
                    GameStatus.createGameStatus((FileInputStream) fileInput);
                }
                History.getHistory().clear();
                System.out.println(getGameStatus().getCurrentLevel());
                reloadGrid();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }
}
