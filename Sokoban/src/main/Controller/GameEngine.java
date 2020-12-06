package Controller;

import Model.*;
import Utils.GameIO;
import Utils.GameLogger;
import View.GameDialog;
import View.WindowFactory;
import java.awt.*;
import java.io.IOException;

/**
 * GameEngine is a class that handles the core events in this game, such as player movement and level switching.
 * As the main part of controller, it is responsible for the data manipulation and view update notification
 */
public final class GameEngine {

    /**
     * jump to the next level
     */
    public static void toNextLevel() {
        getStatus().setMovesCount(0);
        getStatus().setMovesCountLevel(0);
        if (getCurrentLevel().getIndex() < getStatus().getLevels().size()) {
            History.resetHistory(); // reset current level before jump
            getStatus().setCurrentLevel(getNextLevel());
        }
    }

    /**
     * back to previous level
     */
    public static void toPreviousLevel() {
        getStatus().setMovesCount(0);
        getStatus().setMovesCountLevel(0);
        History.resetHistory();
        History.getHistory().clear();

        int currentLevelIndex = getCurrentLevel().getIndex();
        if (currentLevelIndex > 1) { // if previous level exceeds bound, do nothing
            getStatus().setCurrentLevel(getStatus().getLevels().get(currentLevelIndex - 2));
        }
    }

    /**
     * Handle the movemnet of player and objects
     *
     * @param delta the delta of movement
     */
    public static void handleMovement(Point delta) {
        PositionInfo positionInfo = getPositionInfo(delta);
        if (positionInfo == null) {
            return;
        }

        try { // store the status into history before movement
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
     * get related information of keeper and target
     *
     * @param delta the delta of movement
     * @return the position information
     */
    private static PositionInfo getPositionInfo(Point delta) {
        if (GameStatus.getGameStatus().isGameComplete()) {
            return null; // if game is completed, do nothing
        }

        Point keeperPosition = getCurrentLevel().getKeeperPosition();
        char keeper = getCurrentLevel().getObjectsGrid().getGameObjectAt(keeperPosition);
        boolean keeperMoved = false;
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        char keeperTarget = getCurrentLevel().getObjectsGrid().getGameObjectAt(targetObjectPoint);
        PositionInfo positionInfo = new PositionInfo(delta, keeperPosition, keeper, keeperMoved, targetObjectPoint, keeperTarget);

        if (GameLogger.isDebugActive()) {
            GameLogger.printState(positionInfo);
        }

        return positionInfo;
    }

    /**
     * Move the keeper and target accordingly (in grid)
     *
     * @param positionInfo the position information (parameter object)
     */
    private static void move(PositionInfo positionInfo) {

        // decide movement according to the target type
        switch (positionInfo.getKeeperTarget()) {

            case '$':
            case '#':
            case 'W':
                break;

            case 'C':
                // get the target of crate
                char crateTarget = getCurrentLevel().getTargetObject(positionInfo.getTargetObjectPoint(), positionInfo.getDelta());
                if (crateTarget != ' ') { // crate blocked after movement
                    break;
                }
                moveTargetObject(positionInfo);
                moveKeeper(positionInfo);
                positionInfo.setKeeperMoved(true);
                break;

            case 'U':
            case 'D':
            case 'L':
            case 'R':
                // get the position of another pipe as the exit of current entrance pipe
                Point targetPipe = getCurrentLevel().getAnotherPipe(positionInfo.getTargetObjectPoint());
                if (getCurrentLevel().getObjectsGrid().getGameObjectAt(targetPipe) != 'C' && targetPipe != null) {
                    positionInfo.setDelta(positionInfo.getKeeperPosition(), targetPipe); // the delta between keeper and exit pipe
                    moveKeeper(positionInfo);
                    positionInfo.setKeeperMoved(true);
                }
                break;

            case ' ':
                moveKeeper(positionInfo);
                positionInfo.setKeeperMoved(true);
                break;

            default:
                GameLogger.showSevere("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }
    }

    /**
     * Move target object.
     *
     * @param position the position information
     */
    private static void moveTargetObject(PositionInfo position) {
        Point delta = position.getDelta();
        Point targetPosition = position.getTargetObjectPoint();
        char target = position.getKeeperTarget();
        putObject(delta, targetPosition, target);
    }

    /**
     * Move keeper.
     *
     * @param position the position inforamtion
     */
    private static void moveKeeper(PositionInfo position) {
        Point delta = position.getDelta();
        Point keeperPosition = position.getKeeperPosition();
        char keeper = position.getKeeper();
        putObject(delta, keeperPosition, keeper);
        getCurrentLevel().checkGate(); // check the state of gate after keeper is moved
    }

    /**
     * Put object on specific location in grid
     *
     * @param delta    the delta
     * @param position the position information
     * @param object   the object being placed
     */
    private static void putObject(Point delta, Point position, char object) {
        GameGrid objectsGrid = getCurrentLevel().getObjectsGrid();
        Point targetPoint = GameGrid.translatePoint(position, delta); // calculate the position of target location
        objectsGrid.putGameObjectAt(objectsGrid.getGameObjectAt(targetPoint), position);
        objectsGrid.putGameObjectAt(object, targetPoint);
    }

    /**
     * Check game status (if current level is completed)
     *
     * @param positionInfo the position information
     */
    private static void checkGameStatus(PositionInfo positionInfo) {
        if (!positionInfo.isKeeperMoved())
            return;

        // update keeper position info
        positionInfo.getKeeperPosition().translate((int) positionInfo.getDelta().getX(), (int) positionInfo.getDelta().getY());
        getStatus().incrementMovesCount();
        getStatus().incrementMovesCountLevel();

        // current level completed
        if (getStatus().getCurrentLevel().isComplete()) {
            // update high score list
            HighScore.updateMap(getCurrentLevel().getIndex(), getStatus().getMovesCountLevel());
            GameDialog.showHighScore();

            if (GameLogger.isDebugActive()) {
                System.out.println("Level complete!");
            }

            getStatus().setCurrentLevel(getNextLevel()); // jump to next level
        }
    }

    /**
     * retrieve next level from level list
     *
     * @return the next level
     */
    public static Level getNextLevel() {
        // clean up move count and history
        getStatus().setMovesCountLevel(0);
        History.getHistory().clear();

        if (getCurrentLevel() == null) { // when game start
            return getStatus().getLevels().get(0);
        }

        int currentLevelIndex = getCurrentLevel().getIndex();
        if (currentLevelIndex < getStatus().getLevels().size()) {
            return getStatus().getLevels().get(currentLevelIndex);
        }

        // no level left
        getStatus().setGameComplete();
        return null;
    }

    /**
     * jump to start page after game is completed
     */
    public static void navigateToStart() {
        // clean up and update
        HighScore.updateMap(0, GameStatus.getGameStatus().getMovesCount());
        GameDialog.showVictoryMessage();
        History.getHistory().clear();
        GameIO.loadDefaultSaveFile();
        try {
            WindowFactory.createStartMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get game status
     *
     * @return the game status
     */
    private static GameStatus getStatus() {
        return GameStatus.getGameStatus();
    }

    /**
     * get current level
     *
     * @return the current level
     */
    private static Level getCurrentLevel() {
        return getStatus().getCurrentLevel();
    }
}