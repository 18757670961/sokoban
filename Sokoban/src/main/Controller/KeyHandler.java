package Controller;

import Modal.Level;
import Utils.GameIO;
import Utils.GameLogger;
import Modal.GameStatus;
import Modal.History;
import View.GameWindow;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.IOException;

public final class KeyHandler {
    public static Level handleKey(KeyCode code) throws IOException {
        Level currentLevel = GameStatus.getGameStatus().getCurrentLevel();
        // switch replaced with enhanced switch
        switch (code) {
            case W:
            case UP:
                GameEngine.handleMovement(new Point(-1, 0));
                currentLevel.getObjectsGrid().putGameObjectAt('T', currentLevel.getKeeperPosition());
                return null;

            case D:
            case RIGHT:
                GameEngine.handleMovement(new Point(0, 1));
                currentLevel.getObjectsGrid().putGameObjectAt('H', currentLevel.getKeeperPosition());
                return null;

            case S:
            case DOWN:
                GameEngine.handleMovement(new Point(1, 0));
                currentLevel.getObjectsGrid().putGameObjectAt('S', currentLevel.getKeeperPosition());
                return null;

            case A:
            case LEFT:
                GameEngine.handleMovement(new Point(0, -1));
                currentLevel.getObjectsGrid().putGameObjectAt('F', currentLevel.getKeeperPosition());
                return null;

//            case F1:
//                GameIO.saveGameFile(GameWindow.getPrimaryStage()); // save game
//                return null;
//
//            case F2:
//                GameEngine.loadGame(GameWindow.getPrimaryStage()); // load game
//                break;

            case Q:
                return History.traceHistory();

            case E:
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
