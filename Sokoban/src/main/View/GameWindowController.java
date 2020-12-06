package View;

import Controller.GameEngine;
import Controller.KeyHandler;
import Model.GameStatus;
import Model.History;
import Model.Level;
import Utils.CheatSheet;
import Utils.GameIO;
import Utils.GameLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * GameWindowController is a controller class that specify event handling and data binding for GameWindow
 */
public class GameWindowController implements Initializable {

    /**
     * The Game grid.
     */
    @FXML
    private GridPane gameGrid;
    /**
     * The Map set name.
     */
    @FXML
    private Label mapSetName;
    /**
     * The Level name.
     */
    @FXML
    private Label levelName;
    /**
     * The Move count for a level.
     */
    @FXML
    private Label moveCountLevel;
    /**
     * The total Move count
     */
    @FXML
    private Label moveCountTotal;
    /**
     * The key Cheat sheet.
     */
    @FXML
    private TableView<CheatSheet> cheatSheet;
    /**
     * The Key column
     */
    @FXML
    private TableColumn<CheatSheet, String> keyCol;
    /**
     * The Function column
     */
    @FXML
    private TableColumn<CheatSheet, String> functionCol;

    /**
     * The Primary stage.
     */
    private Stage primaryStage = WindowFactory.getPrimaryStage();
    /**
     * The Media player.
     */
    private MediaPlayer mediaPlayer = new MediaPlayer(new Media(GameIO.getFile("src/main/resources/music/bgm.wav").toURI().toString()));
    /**
     * The Key mapping information
     */
    private final ObservableList<CheatSheet> keyInfo = FXCollections.observableArrayList(
            new CheatSheet("W / UP", "move upwards"),
            new CheatSheet("S / DOWN", "move downwards"),
            new CheatSheet("A / LEFT", "move leftwards"),
            new CheatSheet("D / RIGHT", "move rightwards"),
            new CheatSheet("Q", "undo"),
            new CheatSheet("E", "reset"),
            new CheatSheet("Z", "previous level"),
            new CheatSheet("X", "next level")
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        // set key handler
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                Level level = KeyHandler.handleKey(event.getCode());
                if (getGameStatus().getMovesCountLevel() == 0) { // new level loaded
                    reloadGrid();
                } else {
                    reloadPartialGrid(level);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // set cheat sheet table
        keyCol.setCellValueFactory(new PropertyValueFactory<CheatSheet, String>("key"));
        functionCol.setCellValueFactory(new PropertyValueFactory<CheatSheet, String>("function"));
        cheatSheet.setItems(keyInfo);
        reloadGrid();
    }

    /**
     * Gets game status.
     *
     * @return the game status
     */
    private GameStatus getGameStatus() {
        return GameStatus.getGameStatus();
    }

    /**
     * Resize stage and gridpane
     */
    private void resizeWindow() {
        gameGrid.autosize();
        WindowFactory.getPrimaryStage().sizeToScene();
    }

    /**
     * Reload grid partially (optimize the fluency of player movement)
     *
     * @param level the level
     */
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

        updateInfoBox();
        resizeWindow();
    }

    /**
     * compare each cell of the grid in last move and the grid this time
     * if the symbol in the cell is the same, replace the image with new one
     * otherwise leave the old image same there
     *
     * @param newLevelGridIterator the new level grid iterator
     * @param oldLevelGridIterator the old level grid iterator
     */
    private void addPartialObjects(Level.LevelIterator newLevelGridIterator, Level.LevelIterator oldLevelGridIterator) {
        while (newLevelGridIterator.hasNext()) {
            char oldObject = oldLevelGridIterator.next();
            char newObject = newLevelGridIterator.next();
            Point newObjectPosition = newLevelGridIterator.getCurrentPosition();
            if (oldObject != newObject) { // comparing
                addObjectToGrid(newObject, newObjectPosition);
            }
        }
    }

    /**
     * Reload grid by loading image for each cell
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

        updateInfoBox();
        resizeWindow();
    }

    /**
     * Add object to grid.
     *
     * @param gameObject the game object
     * @param location   the location
     */
    private void addObjectToGrid(char gameObject, Point location) {
        ImageView img = ImageFactory.chooseImage(gameObject); // generate image according to object type
        if (img != null) {
            gameGrid.add(img, location.y, location.x);
        }
    }

    /**
     * Update information box on up right corner
     */
    private void updateInfoBox() {
        mapSetName.setText(getGameStatus().getMapSetName());
        levelName.setText(getGameStatus().getCurrentLevel().getName());
        moveCountLevel.setText(getGameStatus().getMovesCountLevel() + "");
        moveCountTotal.setText(getGameStatus().getMovesCount() + "");
    }

    /**
     * Show about page
     *
     * @param event the event
     */
    @FXML
    void showAbout(ActionEvent event) {
        GameDialog.showAbout();
    }


    /**
     * Show help page
     *
     * @param event the event
     */
    @FXML
    void showHelp(ActionEvent event) {
        try {
            WindowFactory.createUserGuide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Undo.
     *
     * @param event the event
     */
    @FXML
    void undo(ActionEvent event) {
        reloadPartialGrid(History.traceHistory());
    }

    /**
     * Toggle music.
     *
     * @param event the event
     */
    @FXML
    void toggleMusic(ActionEvent event) {
        // check the status of media player first
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }

    /**
     * Toggle debug mode
     *
     * @param event the event
     */
    @FXML
    void toggleDebug(ActionEvent event) {
        GameLogger.toggleDebug();
    }

    /**
     * Reset.
     *
     * @param event the event
     */
    @FXML
    void reset(ActionEvent event) {
        reloadPartialGrid(History.resetHistory());
    }

    /**
     * jump to previous level.
     *
     * @param event the event
     */
    @FXML
    void toPreviousLevel(ActionEvent event) {
        GameEngine.toPreviousLevel();
        reloadGrid();
    }

    /**
     * back to next level.
     *
     * @param event the event
     */
    @FXML
    void toNextLevel(ActionEvent event) {
        GameEngine.toNextLevel();
        reloadGrid();
    }

    /**
     * Save game.
     *
     * @param event the event
     */
    @FXML
    void saveGame(ActionEvent event) {
        GameIO.saveGameFile(primaryStage);
    }

    /**
     * Load game.
     *
     * @param event the event
     */
    @FXML
    void loadGame(ActionEvent event) {
        if (GameIO.chooseGameFile() != null) {
            reloadGrid();
        }
    }

    /**
     * Exit.
     *
     * @param event the event
     */
    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }
}
