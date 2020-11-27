package Controller;

import Modal.*;
import Debug.GameLogger;
import View.GameDialog;
import View.GameWindow;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The type Game engine.
 */

/**
 * singleton class
 */
public final class GameEngine implements Serializable {
    /**
     * The constant gameEngine.
     */
// transfer public fields to private
    private static GameEngine gameEngine;
    /**
     * The Game name.
     */
    private final String GAME_NAME = "Sokoban";
    /**
     * The Map set name.
     */
    private String mapSetName = "";
    /**
     * The Moves count.
     */
    private int movesCount = 0;
    private int movesCountLevel = 0;
    /**
     * The Current level.
     */
    private Level currentLevel = null;
    /**
     * The Levels.
     */
    private List<Level> levels = null;
    /**
     * The Serializable levels.
     */
    private Level[] serializableLevels = null;
    /**
     * The Game complete.
     */
    private boolean gameComplete = false;

    /**
     * Instantiates a new Game engine.
     *
     * @param input      the input
     * @param production the production
     */
    private GameEngine() {}

    /**
     * Create game engine.
     *
     * @param engine the engine
     */
    public static void createGameEngine(GameEngine engine) {
        gameEngine = engine;
    }

    /**
     * Create game engine.
     *
     * @param input the input
     */
    public static void createGameEngine(InputStream input) {
        gameEngine = new GameEngine();
        gameEngine.parseFile(input);
    }

    /**
     * Parse file.
     *
     * @param input the input
     */
    private void parseFile(InputStream input) {
        try {
            levels = FileParser.prepareFileReader(input);
            currentLevel = getNextLevel();
        } catch (NoSuchElementException e) {
            GameLogger.showWarning("Cannot load the default save file: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Gets game engine.
     *
     * @return the game engine
     */
    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * Is game complete boolean.
     *
     * @return the boolean
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    public void toNextLevel() {
        movesCount = 0;
        if (currentLevel.getIndex() < levels.size())
            currentLevel = getNextLevel();
    }

    public void toPreviousLevel() {
        movesCount = 0;
        movesCountLevel = 0;
        History.resetHistory();
        History.getHistory().clear();

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex > 1) {
            currentLevel = levels.get(currentLevelIndex - 2);
        }
    }

    public void handleMovement(Point delta) {
        PositionInfo positionInfo = getPositionInfo(delta);
        if (positionInfo == null) {
            return;
        }

        // method extracted
        try {
            History.getHistory().push(currentLevel.deepClone());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        move(positionInfo);
        checkGameStatus(positionInfo);
    }

    /**
     * Move.
     *
     * @param delta the delta
     */
    private PositionInfo getPositionInfo(Point delta) {
        if (isGameComplete()) {
            return null;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        char keeper = currentLevel.objectsGrid.getGameObjectAt(keeperPosition);
        boolean keeperMoved = false;
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        char keeperTarget = currentLevel.objectsGrid.getGameObjectAt(targetObjectPoint);
        PositionInfo positionInfo = new PositionInfo(delta, keeperPosition, keeper, keeperMoved, targetObjectPoint, keeperTarget);

        if (GameLogger.isDebugActive()) {
            GameLogger.printState(positionInfo);
        }

        return positionInfo;
    }

    /**
     * Move.
     *
     * @param positionInfo the position info
     */
// accept parameter object positionInfo
    private void move(PositionInfo positionInfo) {

        // switch replaced with enhanced switch
        switch (positionInfo.getKeeperTarget()) {

            case 'W' -> {
            }

            case 'C' -> {
                char crateTarget = currentLevel.getTargetObject(positionInfo.getTargetObjectPoint(), positionInfo.getDelta());
                if (crateTarget != ' ') {
                    break;
                }

                moveTargetObject(positionInfo);
                moveKeeper(positionInfo);
                positionInfo.setKeeperMoved(true);
            }

            case ' ' -> {
                moveKeeper(positionInfo);
                positionInfo.setKeeperMoved(true);
                break;
            }

            default -> {
                GameLogger.showSevere("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
            }

        }
    }

    private void moveTargetObject(PositionInfo position) {
        Point delta = position.getDelta();
        Point targetPosition = position.getTargetObjectPoint();
        char target = position.getKeeperTarget();
        putObject(delta, targetPosition, target);
    }

    private void moveKeeper(PositionInfo position) {
        Point delta = position.getDelta();
        Point keeperPosition = position.getKeeperPosition();
        char keeper = position.getKeeper();
        putObject(delta, keeperPosition, keeper);
    }

    private void putObject(Point delta, Point position, char object) {
        GameGrid objectsGrid = currentLevel.objectsGrid;
        objectsGrid.putGameObjectAt(objectsGrid.getGameObjectAt(GameGrid.translatePoint(position, delta)), position);
        objectsGrid.putGameObjectAt(object, GameGrid.translatePoint(position, delta));
    }

    /**
     * Check keeper position.
     *
     * @param positionInfo the position info
     */
    private void checkGameStatus(PositionInfo positionInfo) {
        // if structure improved
        if (!positionInfo.isKeeperMoved())
            return;

        positionInfo.getKeeperPosition().translate((int) positionInfo.getDelta().getX(), (int) positionInfo.getDelta().getY());
        movesCount++;
        movesCountLevel++;

        if (currentLevel.isComplete()) {
            HighScore.updateMap(currentLevel.getIndex(), movesCountLevel);
            GameDialog.showHighScore();

            if (GameLogger.isDebugActive()) {
                System.out.println("Level complete!");
            }

            currentLevel = getNextLevel();
        }
    }

    /**
     * Gets next level.
     *
     * @return the next level
     */
    private Level getNextLevel() {
        movesCountLevel = 0;
        History.resetHistory();
        History.getHistory().clear();

        if (currentLevel == null) {
            return levels.get(0);
        }

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        }

        gameComplete = true;
        return null;
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets current level.
     *
     * @param level the level
     */
    public void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    /**
     * Gets game name.
     *
     * @return the game name
     */
    public String getGameName() {
        return GAME_NAME;
    }

    /**
     * Gets moves count.
     *
     * @return the moves count
     */
    public int getMovesCount() {
        return movesCount;
    }

    public int getMovesCountLevel() {
        return movesCountLevel;
    }

    /**
     * Gets map set name.
     *
     * @return the map set name
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * Sets map set name.
     *
     * @param name the name
     */
    public void setMapSetName(String name) {
        mapSetName = name;
    }

    /**
     * Gets levels.
     *
     * @return the levels
     */
    public final List<Level> getLevels() {
        return levels;
    }

    /**
     * Sets levels.
     *
     * @param newLevels the new levels
     */
    public void setLevels(List<Level> newLevels) {
        levels = newLevels;
    }

    /**
     * Get serializable levels level [ ].
     *
     * @return the level [ ]
     */
    public Level[] getSerializableLevels() {
        return serializableLevels;
    }

    /**
     * Sets serializable levels.
     *
     * @param levels the levels
     */
    public void setSerializableLevels(Level[] levels) {
        serializableLevels = levels;
    }

    @Override
    public String toString() {
        return "GameEngine:" + "\n" + currentLevel + "\n" + levels + "\n" + movesCount + "\n";
    }

    /**
     * avoid singleton destroyed by serialization
     * @return
     */
//    private Object readResolve() {
//        return gameEngine;
//    }
}