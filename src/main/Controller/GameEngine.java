package Controller;

import Modal.GameFile;
import Modal.GameGrid;
import Modal.GameObject;
import Modal.Level;
import Debug.GameLogger;
import javafx.scene.input.KeyCode;

import java.awt.*;
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
    // transfer public fields to private
    private static GameEngine gameEngine;
    private final String GAME_NAME = "Sokoban";
    private String mapSetName = "";
    private int movesCount = 0;
    private Level currentLevel = null;
    private List<Level> levels = null;
    private Level[] serializableLevels = {};
    private boolean gameComplete = false;

    /**
     * Instantiates a new Game engine.
     *
     * @param input      the input
     * @param production the production
     */
    private GameEngine() {}

    private void parseFile(InputStream input) {
        try {
            levels = GameFile.prepareFileReader(input);
            currentLevel = getNextLevel();
        } catch (NoSuchElementException e) {
            GameLogger.showWarning("Cannot load the default save file: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void createGameEngine(InputStream input) {
        gameEngine = new GameEngine();
        gameEngine.parseFile(input);
    }

    public static void createGameEngine(GameEngine engine) {
        gameEngine = engine;
    }

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

    /**
     * Handle key.
     *
     * @param code the code
     */
    public void handleKey(KeyCode code) {
        // switch replaced with enhanced switch
        switch (code) {
            case UP -> getPositionInfo(new Point(-1, 0));

            case RIGHT -> getPositionInfo(new Point(0, 1));

            case DOWN -> getPositionInfo(new Point(1, 0));

            case LEFT -> getPositionInfo(new Point(0, -1));

            case F1 -> getPositionInfo(new Point(0, -1)); // save game

            case F2 -> getPositionInfo(new Point(0, -1)); // load game

            default -> {
            }
            // TODO: implement something funny.
        }

        if (GameLogger.isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * Move.
     *
     * @param delta the delta
     */
    private void getPositionInfo(Point delta) {
        if (isGameComplete()) {
            return;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.objectsGrid.getGameObjectAt(keeperPosition);
        boolean keeperMoved = false;
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.objectsGrid.getGameObjectAt(targetObjectPoint);
        PositionInfo positionInfo = new PositionInfo(delta, keeperPosition, keeper, keeperMoved, targetObjectPoint, keeperTarget);

        if (GameLogger.isDebugActive()) {
            printState(positionInfo);
        }

        // method extracted
        try {
            move(positionInfo);
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }

    // method extracted
    private void printState(PositionInfo positionInfo) {
        System.out.println("Current level state:");
        System.out.println(currentLevel.toString());
        System.out.println("Keeper pos: " + positionInfo.getKeeperPosition());
        System.out.println("Movement source obj: " + positionInfo.getKeeper());
        System.out.printf("Target object: %s at [%s]", positionInfo.getKeeperTarget(), positionInfo.getTargetObjectPoint());
    }

    // accept parameter object positionInfo
    private void move(PositionInfo positionInfo) {

        // switch replaced with enhanced switch
        switch (positionInfo.getKeeperTarget()) {

            case WALL -> {
            }

            case CRATE -> {
                GameObject crateTarget = currentLevel.getTargetObject(positionInfo.getTargetObjectPoint(), positionInfo.getDelta());
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                moveObject(positionInfo.getDelta(), positionInfo.getTargetObjectPoint(), positionInfo.getKeeperTarget());
                moveObject(positionInfo.getDelta(), positionInfo.getKeeperPosition(), positionInfo.getKeeper());
                positionInfo.setKeeperMoved(true);
            }

            case FLOOR -> {
                moveObject(positionInfo.getDelta(), positionInfo.getKeeperPosition(), positionInfo.getKeeper());
                positionInfo.setKeeperMoved(true);
                break;
            }

            default -> {
                GameLogger.showSevere("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
            }

        }

        checkKeeperPosition(positionInfo);
    }

    private void checkKeeperPosition(PositionInfo positionInfo) {
        // if structure improved
        if (!positionInfo.isKeeperMoved())
            return;

        positionInfo.getKeeperPosition().translate((int) positionInfo.getDelta().getX(), (int) positionInfo.getDelta().getY());
        movesCount++;

        if (!currentLevel.isComplete())
            return;

        if (GameLogger.isDebugActive()) {
            System.out.println("Level complete!");
        }

        currentLevel = getNextLevel();
    }

    private void moveObject(Point delta, Point keeperPosition, GameObject keeper) {
        currentLevel.objectsGrid.putGameObjectAt(currentLevel.objectsGrid.getGameObjectAt(GameGrid.translatePoint(keeperPosition, delta)), keeperPosition);
        currentLevel.objectsGrid.putGameObjectAt(keeper, GameGrid.translatePoint(keeperPosition, delta));
    }

    /**
     * Gets next level.
     *
     * @return the next level
     */
    private Level getNextLevel() {
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

    public void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    public String getGameName() {
        return GAME_NAME;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public String getMapSetName() {
        return mapSetName;
    }

    public void setMapSetName(String name) {
        mapSetName = name;
    }

    public final List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> newLevels) {
        levels = newLevels;
    }

    public Level[] getSerializableLevels() {
        return serializableLevels;
    }

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