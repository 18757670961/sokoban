package Business;

import Debug.GameLogger;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.IOException;
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
    private static GameLogger logger;
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
            logger = new GameLogger();
            levels = GameFile.prepareFileReader(input);
            currentLevel = getNextLevel();
            gameComplete = false;
        } catch (IOException e) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + Arrays.toString(e.getStackTrace()));
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
            case UP -> move(new Point(-1, 0));

            case RIGHT -> move(new Point(0, 1));

            case DOWN -> move(new Point(1, 0));

            case LEFT -> move(new Point(0, -1));

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
    private void move(Point delta) {
        if (isGameComplete()) {
            return;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.objectsGrid.getGameObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.objectsGrid.getGameObjectAt(targetObjectPoint);

        if (GameEngine.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        // method extracted
        moveKeeper(delta, keeperPosition, keeper, targetObjectPoint, keeperTarget);
    }

    private void moveKeeper(Point delta, Point keeperPosition, GameObject keeper, Point targetObjectPoint, GameObject keeperTarget) {
        boolean keeperMoved = false;

        // switch replaced with enhanced switch
        switch (keeperTarget) {

            case WALL -> {
            }

            case CRATE -> {
                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                moveTargetCrate(delta, keeperPosition, keeper, targetObjectPoint, keeperTarget);
                keeperMoved = true;
            }

            case FLOOR -> {
                moveTargetFloor(delta, keeperPosition, keeper);
                keeperMoved = true;
                break;
            }

            default -> {
                logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
            }

        }

        checkKeeperPosition(delta, keeperPosition, keeperMoved);
    }

    private void checkKeeperPosition(Point delta, Point keeperPosition, boolean keeperMoved) {
        // if structure improved
        if (!keeperMoved)
            return;

        keeperPosition.translate((int) delta.getX(), (int) delta.getY());
        movesCount++;

        if (!currentLevel.isComplete())
            return;

        if (isDebugActive()) {
            System.out.println("Level complete!");
        }

        currentLevel = getNextLevel();
    }

    private void moveTargetFloor(Point delta, Point keeperPosition, GameObject keeper) {
        currentLevel.objectsGrid.putGameObjectAt(currentLevel.objectsGrid.getGameObjectAt(GameGrid.translatePoint(keeperPosition, delta)), keeperPosition);
        currentLevel.objectsGrid.putGameObjectAt(keeper, GameGrid.translatePoint(keeperPosition, delta));
    }

    private void moveTargetCrate(Point delta, Point keeperPosition, GameObject keeper, Point targetObjectPoint, GameObject keeperTarget) {
        moveTargetFloor(delta, targetObjectPoint, keeperTarget);
        moveTargetFloor(delta, keeperPosition, keeper);
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
            return levels.get(currentLevelIndex + 1);
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

    public static GameLogger getLogger() {
        return logger;
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