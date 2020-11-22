package Engine;

import Business.GameFile;
import Business.GameGrid;
import Business.GameObject;
import Business.Level;
import Debug.GameLogger;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The type Game engine.
 */
public class GameEngine {
    // transfer public fields to private
    private static final String GAME_NAME = "Sokoban";
    private static boolean debug = false;
    private int movesCount = 0;
    private static String mapSetName;
    private Level currentLevel;
    private List<Level> levels;
    private boolean gameComplete;

    /**
     * Instantiates a new Game engine.
     *
     * @param input      the input
     * @param production the production
     */
    public GameEngine(InputStream input, boolean production) {
        try {
            levels = GameFile.prepareFileReader(input);
            currentLevel = getNextLevel();
            gameComplete = false;
        } catch (NoSuchElementException e) {
            GameLogger.showWarning("Cannot load the default save file: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Is debug active boolean.
     *
     * @return the boolean
     */
    public static boolean isDebugActive() {
        return debug;
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

            default -> {
            }
            // TODO: implement something funny.
        }

        if (isDebugActive()) {
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

        if (GameEngine.isDebugActive()) {
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

        if (isDebugActive()) {
            System.out.println("Level complete!");
        }

        currentLevel = getNextLevel();
    }

    private void moveObject(Point delta, Point keeperPosition, GameObject keeper) {
        currentLevel.objectsGrid.putGameObjectAt(currentLevel.objectsGrid.getGameObjectAt(GameGrid.translatePoint(keeperPosition, delta)), keeperPosition);
        currentLevel.objectsGrid.putGameObjectAt(keeper, GameGrid.translatePoint(keeperPosition, delta));
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
     * Toggle debug.
     */
    public void toggleDebug() {
        debug = !debug;
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    public static String getGameName() {
        return GAME_NAME;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public static String getMapSetName() {
        return mapSetName;
    }

    public static void setMapSetName(String name) {
        mapSetName = name;
    }

    public final List<Level> getLevels() {
        return levels;
    }

}