package com.ae2dms;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * The type Main.
 */
public class Main extends Application {
    private Stage primaryStage;
    private GameEngine gameEngine;
    private GridPane gameGrid;
    private GridPane root;
    private File saveFile;
    private MenuBar menu;

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
        this.primaryStage = primaryStage;
        menu = new MenuBar();

        // 3 methods extracted
        Menu menuFile = createMenuFile();
        Menu menuLevel = createMenuLevel();
        Menu menuAbout = createMenuAbout();
        menu.getMenus().addAll(menuFile, menuLevel, menuAbout);

        createPane(primaryStage); // method extracted
        loadDefaultSaveFile(primaryStage);
    }

    private void createPane(Stage primaryStage) {
        gameGrid = new GridPane();
        root = new GridPane();
        root.add(menu, 0, 0);
        root.add(gameGrid, 0, 1);
        primaryStage.setTitle(GameEngine.getGameName());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
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

    /**
     * Load default save file.
     *
     * @param primaryStage the primary stage
     */
    private void loadDefaultSaveFile(Stage primaryStage) {
        this.primaryStage = primaryStage;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("level/sampleGame.skb"); // variable name changed
        initializeGame(inputStream);
        setEventFilter();
    }

    /**
     * Initialize game.
     *
     * @param input the input
     */
    private void initializeGame(InputStream input) {
        gameEngine = new GameEngine(input, true);
        reloadGrid();
    }

    /**
     * Sets event filter.
     */
    private void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameEngine.handleKey(event.getCode());
            reloadGrid();
        });
    }

    /**
     * Save game file.
     */
    private void saveGameFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File to");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showSaveDialog(primaryStage);

        if (saveFile != null && GameEngine.isDebugActive()) {
            GameEngine.getLogger().info("Saving file: " + saveFile.getName());
        }
    }

    /**
     * Load game file.
     *
     * @throws FileNotFoundException the file not found exception
     */
    private void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (GameEngine.isDebugActive()) {
                GameEngine.getLogger().info("Loading save file: " + saveFile.getName());
            }
            initializeGame(new FileInputStream(saveFile));
        }
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
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getcurrentposition());
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

        newDialog(dialogTitle, dialogMessage, motionBlur);
    }

    /**
     * New dialog.
     *
     * @param dialogTitle         the dialog title
     * @param dialogMessage       the dialog message
     * @param dialogMessageEffect the dialog message effect
     */
    private void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
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
    public void closeGame() {
        System.exit(0);
    }

    /**
     * Save game.
     */
    public void saveGame() {
        saveGameFile();
    }

    /**
     * Load game.
     */
    public void loadGame() {
        try {
            loadGameFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Undo.
     */
    public void undo() {
        closeGame();
    }

    /**
     * Reset level.
     */
    public void resetLevel() {
    }

    /**
     * Show about.
     */
    public void showAbout() {
        String title = "About this game";
        String message = "Game created by Shuguang LYU (Desmond)\n";

        newDialog(title, message, null);
    }

    /**
     * Toggle music.
     */
    public void toggleMusic() {
        // TODO
    }

    /**
     * Toggle debug.
     */
    public void toggleDebug() {
        gameEngine.toggleDebug();
        reloadGrid();
    }
}
