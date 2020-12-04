package View;

import Controller.GameEngine;
import Controller.KeyHandler;
import Modal.GameStatus;
import Modal.History;
import Modal.Level;
import Utils.GameFile;
import Utils.GameLogger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * The type Game window.
 */
// GUI class extracted
public class GameWindow implements Observer {
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

    public static void createStartMenu() throws IOException {
        Parent parent = FXMLLoader.load(new URL("file:src/main/View/MenuWindow.fxml"));
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sokoban");
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:src/main/resources/image/box.png"));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    /**
     * Instantiates a new Game window.
     *
     * @param primaryStage the primary stage
     */
    public static void createGameWindow() throws IOException {
//        GameStatus.addObserver(this);

        createMenu();
        createPane();

        primaryStage.setTitle(GameStatus.getGameStatus().getGameName());
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
//        primaryStage.setWidth(1920);
//        primaryStage.setHeight(1080);
//        primaryStage.centerOnScreen();
        //primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.getIcons().add(new Image("file:src/main/resources/image/box.png"));
        setEventHandler();
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
        menuItemUndo.setOnAction(actionEvent -> reloadPartialGrid(History.traceHistory()));
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> GameLogger.toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        menuItemResetLevel.setOnAction(actionEvent -> reloadPartialGrid(History.resetHistory()));
        MenuItem menuItemPreviousLevel = new MenuItem("Previous Level");
        menuItemPreviousLevel.setOnAction(actionEvent -> GameEngine.toPreviousLevel());
        MenuItem menuItemNextLevel = new MenuItem("Next Level");
        menuItemNextLevel.setOnAction(actionEvent -> GameEngine.toNextLevel());
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
        menuItemSaveGame.setOnAction(actionEvent -> GameFile.saveGameFile(primaryStage));
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> GameEngine.loadGame(primaryStage));
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> System.exit(0));
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
    public static void setEventHandler() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            try {
                KeyHandler.handleKey(event.getCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                HighScore.saveMap();
//            }
//        });
    }

    private static void resizeWindow() {
        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    public static void reloadPartialGrid(Level level) {
        if (GameStatus.getGameStatus().isGameComplete()) {
            GameEngine.navigateToStart();
            return;
        }

        Level currentLevel = GameStatus.getGameStatus().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        Level previousLevel = (level == null ? History.getHistory().peek() : level);
        Level.LevelIterator oldLevelGridIterator = (Level.LevelIterator) previousLevel.iterator();
        addPartialObjects(newLevelGridIterator, oldLevelGridIterator);

        resizeWindow();
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
        if (GameStatus.getGameStatus().isGameComplete()) {
            GameEngine.navigateToStart();
            return;
        }

        Level currentLevel = GameStatus.getGameStatus().getCurrentLevel();
        Level.LevelIterator newLevelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (newLevelGridIterator.hasNext()) {
            char newObject = newLevelGridIterator.next();
            Point newObjectPosition = newLevelGridIterator.getCurrentPosition();
            addObjectToGrid(newObject, newObjectPosition);
        }

        resizeWindow();
        primaryStage.centerOnScreen();
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
        ImageView img = ImageFactory.chooseImage(gameObject);
        if (img != null) {
            gameGrid.add(img, location.y, location.x);
        }
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

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater( () -> {
//            drawMap();
//            drawInfo();
        });
    }
}
