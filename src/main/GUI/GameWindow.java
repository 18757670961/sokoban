package GUI;

import Engine.GameEngine;
import Business.GameFile;
import Business.GameObject;
import Business.Level;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

// GUI class extracted
public class GameWindow {
    private Stage primaryStage;
    private GameEngine gameEngine;
    private GridPane gameGrid;
    private GridPane root;
    private MenuBar menu;

    public GameWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createMenu();
        createPane();
    }

    private void createMenu() {
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
    private Menu createMenuAbout() {
        MenuItem menuItemAbout = new MenuItem("About This Game");
        menuItemAbout.setOnAction(actionEvent -> showAbout());
        Menu menuAbout = new Menu("About");
        menuAbout.getItems().addAll(menuItemAbout);
        return menuAbout;
    }

    /**
     * Create menu level menu.
     *
     * @return the menu
     */
    private Menu createMenuLevel() {
        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setOnAction(actionEvent -> undo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        menuItemResetLevel.setOnAction(actionEvent -> resetLevel());
        Menu menuLevel = new Menu("Level");
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel);
        return menuLevel;
    }

    /**
     * Create menu file menu.
     *
     * @return the menu
     */
    private Menu createMenuFile() {
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

    private void createPane() {
        gameGrid = new GridPane();
        root = new GridPane();
        root.add(menu, 0, 0);
        root.add(gameGrid, 0, 1);
        primaryStage.setTitle(GameEngine.getGameName());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Initialize game.
     *
     * @param input the input
     */
    public void initializeGame(InputStream input) {
        gameEngine = new GameEngine(input, true);
        reloadGrid();
    }

    /**
     * Sets event filter.
     */
    public void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameEngine.handleKey(event.getCode());
            reloadGrid();
        });
    }

    /**
     * Reload grid.
     */
    private void reloadGrid() {
        if (gameEngine.isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();

        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }

        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    /**
     * Show victory message.
     */
    private void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameEngine.getMapSetName() + " in " + gameEngine.getMovesCount() + " moves!";
        MotionBlur motionBlur = new MotionBlur(2, 3); // vairable name changed

        GameDialog dialog = new GameDialog(primaryStage, dialogTitle, dialogMessage, motionBlur);
    }

    /**
     * Add object to grid.
     *
     * @param gameObject the game object
     * @param location   the location
     */
    private void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        gameGrid.add(graphicObject, location.y, location.x);
    }

    /**
     * Close game.
     */
    private void closeGame() {
        System.exit(0);
    }

    /**
     * Save game.
     */
    private void saveGame() {
        GameFile saveFile = new GameFile();
        saveFile.saveGameFile(primaryStage);
    }

    /**
     * Load game.
     */
    private void loadGame() {
        try {
            InputStream fileInput;
            GameFile loadFile = new GameFile();
            fileInput = loadFile.loadGameFile(primaryStage);

            if (fileInput != null) {
                initializeGame(fileInput);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Undo.
     */
    private void undo() {
        closeGame();
    }

    /**
     * Reset level.
     */
    private void resetLevel() {
    }

    /**
     * Show about.
     */
    private void showAbout() {
        String title = "About this game";
        String message = "Game created by Shuguang LYU (Desmond)\n";

        GameDialog dialog = new GameDialog(primaryStage, title, message, null);
    }

    /**
     * Toggle music.
     */
    private void toggleMusic() {
        // TODO
    }

    /**
     * Toggle debug.
     */
    private void toggleDebug() {
        gameEngine.toggleDebug();
        reloadGrid();
    }
}
