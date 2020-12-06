package Controller;

import Model.GameStatus;
import Model.History;
import Model.Level;
import Utils.GameLogger;
import javafx.scene.input.KeyCode;
import java.awt.*;
import java.io.IOException;

/**
 * utility class for key handling
 */
public final class KeyHandler {

    /**
     * Handle key press event
     *
     * @param code the key code
     * @return the level
     * @throws IOException the io exception
     */
    public static Level handleKey(KeyCode code) throws IOException {
        Level currentLevel = GameStatus.getGameStatus().getCurrentLevel();
        switch (code) {
            case W:
            case UP:
                GameEngine.handleMovement(new Point(-1, 0));
                // change the direction of keeper
                currentLevel.getObjectsGrid().putGameObjectAt('T', currentLevel.getKeeperPosition());
                return null;

            case D:
            case RIGHT:
                GameEngine.handleMovement(new Point(0, 1));
                // change the direction of keeper
                currentLevel.getObjectsGrid().putGameObjectAt('H', currentLevel.getKeeperPosition());
                return null;

            case S:
            case DOWN:
                GameEngine.handleMovement(new Point(1, 0));
                // change the direction of keeper
                currentLevel.getObjectsGrid().putGameObjectAt('S', currentLevel.getKeeperPosition());
                return null;

            case A:
            case LEFT:
                GameEngine.handleMovement(new Point(0, -1));
                // change the direction of keeper
                currentLevel.getObjectsGrid().putGameObjectAt('F', currentLevel.getKeeperPosition());
                return null;

            case Q: // undo
                return History.traceHistory();

            case E: // reset
                return History.resetHistory();

            case Z:
                GameEngine.toPreviousLevel();
                return null;

            case X:
                GameEngine.toNextLevel();
                return null;

            default:
                // TODO: implement something funny.
        }

        if (GameLogger.isDebugActive()) {
            System.out.println(code);
        }

        return null;
    }
}
