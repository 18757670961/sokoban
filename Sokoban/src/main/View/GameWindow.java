package View;

import Controller.GameEngine;
import Controller.GameFile;
import Controller.KeyHandler;
import Debug.GameLogger;
import Modal.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The type Game window.
 */
// GUI class extracted
public class GameWindow {
    /**
     * The Primary stage.
     */
    private static Stage primaryStage;
    /**
     * The Game grid.
     */
    private static GridPane gameGrid;
    /**
     * The Root.
     */
    private static GridPane root;
    /**
     * The Menu.
     */
    private static MenuBar menu;
    /**
     * The Mp.
     */
    private static MediaPlayer mediaPlayer = new MediaPlayer(new Media(GameFile.getFile("src/main/resources/music/bgm.wav").toURI().toString()));

    /**
     * Instantiates a new Game window.
     *
     * @param primaryStage the primary stage
     */
    public static void createGameWindow(Stage ps) {
        primaryStage = ps;
        createMenu();
        createPane();

        primaryStage.setTitle(GameEngine.getGameEngine().getGameName());
        primaryStage.setScene(new Scene(root));
        //primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.getIcons().add(new Image("file:src/main/resources/image/box.png"));
        primaryStage.show();

        setEventFilter();
        reloadGrid();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * Create menu.
     */
    private static void createMenu() {
        menu = new MenuBar();
        Menu menuFile = createMenuFile();
        Menu menuLevel = createMenuLevel();
        Menu menuAbout = createMenuAbout();
        menu.getMenus().addAll(menuFile, menuLevel, menuAbout);
    }

    /**
     * Create menu about menu.
     *
     * @return the menu
     */
    private static Menu createMenuAbout() {
        MenuItem menuItemAbout = new MenuItem("About This Game");
        menuItemAbout.setOnAction(actionEvent -> GameDialog.showAbout());
        Menu menuAbout = new Menu("About");
        menuAbout.getItems().addAll(menuItemAbout);
        return menuAbout;
    }

    /**
     * Create menu level menu.
     *
     * @return the menu
     */
    private static Menu createMenuLevel() {
        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setOnAction(actionEvent -> undo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        menuItemResetLevel.setOnAction(actionEvent -> resetLevel());
        MenuItem menuItemPreviousLevel = new MenuItem("Previous Level");
        menuItemPreviousLevel.setOnAction(actionEvent -> GameEngine.getGameEngine().toPreviousLevel());
        MenuItem menuItemNextLevel = new MenuItem("Next Level");
        menuItemNextLevel.setOnAction(actionEvent -> GameEngine.getGameEngine().toNextLevel());
        Menu menuLevel = new Menu("Level");
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemPreviousLevel, menuItemNextLevel, menuItemResetLevel);
        return menuLevel;
    }

    /**
     * Create menu file menu.
     *
     * @return the menu
     */
    private static Menu createMenuFile() {
        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setOnAction(actionEvent -> saveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> loadGame());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame, new SeparatorMenuItem(), menuItemExit);
        return menuFile;
    }

    /**
     * Create pane.
     */
    private static void createPane() {
        gameGrid = new GridPane();
        root = new GridPane();
        root.add(menu, 0, 0);
        root.add(gameGrid, 0, 1);
    }

    /**
     * Sets event filter.
     */
    public static void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyHandler.handleKey(event.getCode());
        });
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                HighScore.saveMap();
            }
        });
    }

    /**
     * Reload grid.
     */
    public static void reloadPartialGrid() {
        if (GameEngine.getGameEngine().isGameComplete()) {
            doBeforeExit();
            return;
        }

        Level currentLevel = GameEngine.getGameEngine().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        Level previousLevel = History.getHistory().peek();
        Level.LevelIterator oldLevelGridIterator = (Level.LevelIterator) previousLevel.iterator();
        addPartialObjects(newLevelGridIterator, oldLevelGridIterator);

        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    public static void reloadPartialGrid(Level level) {
        if (GameEngine.getGameEngine().isGameComplete()) {
            doBeforeExit();
            return;
        }

        Level currentLevel = GameEngine.getGameEngine().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        Level previousLevel = level;
        Level.LevelIterator oldLevelGridIterator = (Level.LevelIterator) previousLevel.iterator();
        addPartialObjects(newLevelGridIterator, oldLevelGridIterator);

        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    private static void addPartialObjects(Level.LevelIterator newLevelGridIterator, Level.LevelIterator oldLevelGridIterator) {
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
    public static void reloadGrid() {
        if (GameEngine.getGameEngine().isGameComplete()) {
            doBeforeExit();
            return;
        }

        Level currentLevel = GameEngine.getGameEngine().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (newLevelGridIterator.hasNext()) {
            char newObject = newLevelGridIterator.next();
            Point newObjectPosition = newLevelGridIterator.getCurrentPosition();
            addObjectToGrid(newObject, newObjectPosition);
        }

        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    private static void doBeforeExit() {
        HighScore.updateMap(0, GameEngine.getGameEngine().getMovesCount());
        GameDialog.showVictoryMessage();
    }

    /**
     * Add object to grid.
     *
     * @param gameObject the game object
     * @param location   the location
     */
    private static void addObjectToGrid(char gameObject, Point location) {
//        GraphicObject graphicObject = new GraphicObject(gameObject);
//        gameGrid.add(graphicObject, location.y, location.x);
        ImageView img = setImage(gameObject);
        gameGrid.add(img, location.y, location.x);
    }

    private static ImageView setImage(char obj) {
        String file;

        switch (obj) {
            case 'W':
                file = "file:src/main/resources/image/wall.bmp";
                break;
            case ' ':
                file = "file:src/main/resources/image/floor.bmp";
                break;
            case 'C':
                file = "file:src/main/resources/image/box.png";
                break;
            case 'D':
                file = "file:src/main/resources/image/goal.png";
                break;
            case 'S':
                file = "file:src/main/resources/image/playerD.png";
                break;
            case 'O':
                file = "file:src/main/resources/image/boxP.png";
                break;
            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameLogger.showSevere(message);
                throw new AssertionError(message);
        }

        ImageView img = new ImageView(new Image(file));
        return img;
    }

    /**
     * Close game.
     */
    private static void closeGame() {
        System.exit(0);
    }

    /**
     * Save game.
     */
    public static void saveGame() {
        GameFile.saveGameFile(primaryStage);
    }

    /**
     * Load game.
     */
    public static void loadGame() {
        try {
            Object fileInput;
            fileInput = GameFile.loadGameFile(primaryStage);

            if (fileInput != null) {
                if (fileInput instanceof GameEngine) {
                    GameEngine.createGameEngine((GameEngine) fileInput);
                } else {
                    GameEngine.createGameEngine((FileInputStream) fileInput);
                }
                History.getHistory().clear();
                reloadGrid();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Undo.
     */
    private static void undo() {
        reloadPartialGrid(History.traceHistory());
    }

    /**
     * Reset level.
     */
    private static void resetLevel() {
        reloadPartialGrid(History.resetHistory());
    }

    /**
     * Toggle music.
     */
    private static void toggleMusic() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }

    /**
     * Toggle debug.
     */
    private static void toggleDebug() {
        GameLogger.toggleDebug();
        reloadGrid();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}