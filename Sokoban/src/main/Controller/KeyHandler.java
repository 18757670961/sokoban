package Controller;

import Debug.GameLogger;
import Modal.History;
import View.GameWindow;
import javafx.scene.input.KeyCode;

import java.awt.*;

public final class KeyHandler {
    public static void handleKey(KeyCode code) {
        GameEngine gameEngine = GameEngine.getGameEngine();
        // switch replaced with enhanced switch
        switch (code) {
            case W:
            case UP:
                gameEngine.handleMovement(new Point(-1, 0));
                GameWindow.reloadPartialGrid();
                break;

            case D:
            case RIGHT:
                gameEngine.handleMovement(new Point(0, 1));
                GameWindow.reloadPartialGrid();
                break;

            case S:
            case DOWN:
                gameEngine.handleMovement(new Point(1, 0));
                GameWindow.reloadPartialGrid();
                break;

            case A:
            case LEFT:
                gameEngine.handleMovement(new Point(0, -1));
                GameWindow.reloadPartialGrid();
                break;

            case F1:
                GameWindow.saveGame(); // save game
                break;

            case F2:
                GameWindow.loadGame(); // load game
                break;

            case Q:
                GameWindow.reloadPartialGrid(History.traceHistory());
                break;

            case E:
                GameWindow.reloadPartialGrid(History.resetHistory());
                break;

            case Z:
                gameEngine.toPreviousLevel();
                GameWindow.reloadGrid();
                break;

            case X:
                gameEngine.toNextLevel();
                GameWindow.reloadGrid();
                break;

            default:
                // TODO: implement something funny.
        }

        if (GameLogger.isDebugActive()) {
            System.out.println(code);
        }
    }
}
