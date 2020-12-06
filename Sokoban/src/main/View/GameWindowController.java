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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameWindowController implements Initializable {
    @FXML private GridPane gameGrid;
    @FXML private Label mapSetName;
    @FXML private Label levelName;
    @FXML private Label moveCountLevel;
    @FXML private Label moveCountTotal;
    @FXML private TableView<CheatSheet> cheatSheet;
    @FXML private TableColumn<CheatSheet, String> keyCol;
    @FXML private TableColumn<CheatSheet, String> functionCol;

    private Stage primaryStage = GameWindow.getPrimaryStage();
    private MediaPlayer mediaPlayer = new MediaPlayer(new Media(GameIO.getFile("src/main/resources/music/bgm.wav").toURI().toString()));
    private final ObservableList<CheatSheet> keyInfo = FXCollections.observableArrayList (
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

        keyCol.setCellValueFactory(new PropertyValueFactory<CheatSheet, String>("key"));
        functionCol.setCellValueFactory(new PropertyValueFactory<CheatSheet, String>("function"));
        cheatSheet.setItems(keyInfo);
        reloadGrid();
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

        updateInfoBox();
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
//        GraphicObject graphicObject = new GraphicObject(gameObject);
//        gameGrid.add(graphicObject, location.y, location.x);
        ImageView img = ImageFactory.chooseImage(gameObject);
        if (img != null) {
            gameGrid.add(img, location.y, location.x);
        }
    }

    private void updateInfoBox() {
        mapSetName.setText(getGameStatus().getMapSetName());
        levelName.setText(getGameStatus().getCurrentLevel().getName());
        moveCountLevel.setText(getGameStatus().getMovesCountLevel() + "");
        moveCountTotal.setText(getGameStatus().getMovesCount() + "");
    }

    @FXML
    void showAbout(ActionEvent event) {
        GameDialog.showAbout();
    }


    @FXML
    void showHelp(ActionEvent event) {
        try {
            GameWindow.createUserGuide();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        reloadGrid();
    }

    @FXML
    void toNextLevel(ActionEvent event) {
        GameEngine.toNextLevel();
        reloadGrid();
    }

    @FXML
    void saveGame(ActionEvent event) {
        GameIO.saveGameFile(primaryStage);
    }

    @FXML
    void loadGame(ActionEvent event) {
        if (GameIO.chooseGameFile() != null) {
            reloadGrid();
        }
    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }
}
