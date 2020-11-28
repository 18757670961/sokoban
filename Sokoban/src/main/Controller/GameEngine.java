package Controller;

import Debug.GameLogger;
import Modal.*;
import View.GameDialog;

import java.awt.*;
import java.io.IOException;

/**
 * The type Game engine.
 */

/**
 * singleton class
 */
public final class GameEngine {
    private static GameStatus getStatus() {
        return GameStatus.getGameStatus();
    }

    public static void toNextLevel() {
        getStatus().setMovesCount(0);
        if (getStatus().getCurrentLevel().getIndex() < getStatus().getLevels().size()) {
            History.resetHistory();
            getStatus().setCurrentLevel(getNextLevel());
        }
    }

    public static void toPreviousLevel() {
        getStatus().setMovesCount(0);
        getStatus().setMovesCountLevel(0);
        History.resetHistory();
        History.getHistory().clear();

        int currentLevelIndex = getStatus().getCurrentLevel().getIndex();
        if (currentLevelIndex > 1) {
            getStatus().setCurrentLevel(getStatus().getLevels().get(currentLevelIndex - 2));
        }
    }

    public static void handleMovement(Point delta) {
        PositionInfo positionInfo = getPositionInfo(delta);
        if (positionInfo == null) {
            return;
        }

        // method extracted
        try {
            History.getHistory().push(getStatus().getCurrentLevel().deepClone());
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
    private static PositionInfo getPositionInfo(Point delta) {
        if (GameStatus.getGameStatus().isGameComplete()) {
            return null;
        }

        Point keeperPosition = getStatus().getCurrentLevel().getKeeperPosition();
        char keeper = getStatus().getCurrentLevel().objectsGrid.getGameObjectAt(keeperPosition);
        boolean keeperMoved = false;
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        char keeperTarget = getStatus().getCurrentLevel().objectsGrid.getGameObjectAt(targetObjectPoint);
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
    private static void move(PositionInfo positionInfo) {

        // switch replaced with enhanced switch
        switch (positionInfo.getKeeperTarget()) {

            case 'W' -> {
            }

            case 'C' -> {
                char crateTarget = getStatus().getCurrentLevel().getTargetObject(positionInfo.getTargetObjectPoint(), positionInfo.getDelta());
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

    private static void moveTargetObject(PositionInfo position) {
        Point delta = position.getDelta();
        Point targetPosition = position.getTargetObjectPoint();
        char target = position.getKeeperTarget();
        putObject(delta, targetPosition, target);
    }

    private static void moveKeeper(PositionInfo position) {
        Point delta = position.getDelta();
        Point keeperPosition = position.getKeeperPosition();
        char keeper = position.getKeeper();
        putObject(delta, keeperPosition, keeper);
    }

    private static void putObject(Point delta, Point position, char object) {
        GameGrid objectsGrid = getStatus().getCurrentLevel().objectsGrid;
        objectsGrid.putGameObjectAt(objectsGrid.getGameObjectAt(GameGrid.translatePoint(position, delta)), position);
        objectsGrid.putGameObjectAt(object, GameGrid.translatePoint(position, delta));
    }

    /**
     * Check keeper position.
     *
     * @param positionInfo the position info
     */
    private static void checkGameStatus(PositionInfo positionInfo) {
        // if structure improved
        if (!positionInfo.isKeeperMoved())
            return;

        positionInfo.getKeeperPosition().translate((int) positionInfo.getDelta().getX(), (int) positionInfo.getDelta().getY());
        getStatus().incrementMovesCount();
        getStatus().incrementMovesCountLevel();

        if (getStatus().getCurrentLevel().isComplete()) {
            HighScore.updateMap(getStatus().getCurrentLevel().getIndex(), getStatus().getMovesCountLevel());
            GameDialog.showHighScore();

            if (GameLogger.isDebugActive()) {
                System.out.println("Level complete!");
            }

            getStatus().setCurrentLevel(getNextLevel());
        }
    }

    /**
     * Gets next level.
     *
     * @return the next level
     */
    public static Level getNextLevel() {
        getStatus().setMovesCountLevel(0);
        History.getHistory().clear();

        if (getStatus().getCurrentLevel() == null) {
            return getStatus().getLevels().get(0);
        }

        int currentLevelIndex = getStatus().getCurrentLevel().getIndex();
        if (currentLevelIndex < getStatus().getLevels().size()) {
            return getStatus().getLevels().get(currentLevelIndex);
        }

        getStatus().setGameComplete();
        return null;
    }
}