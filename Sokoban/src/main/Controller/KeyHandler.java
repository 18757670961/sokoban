package Controller;

import Debug.GameLogger;
import Modal.GameStatus;
import Modal.History;
import View.GameWindow;
import javafx.scene.input.KeyCode;

import java.awt.*;

public final class KeyHandler {
    public static void handleKey(KeyCode code) {
        // switch replaced with enhanced switch
        switch (code) {
            case W:
            case UP:
                GameEngine.handleMovement(new Point(-1, 0));
                GameWindow.reloadPartialGrid();
                break;

            case D:
            case RIGHT:
                GameEngine.handleMovement(new Point(0, 1));
                GameWindow.reloadPartialGrid();
                break;

            case S:
            case DOWN:
                GameEngine.handleMovement(new Point(1, 0));
                GameWindow.reloadPartialGrid();
                break;

            case A:
            case LEFT:
                GameEngine.handleMovement(new Point(0, -1));
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
                GameEngine.toPreviousLevel();
                GameWindow.reloadGrid();
                break;

            case X:
                GameEngine.toNextLevel();
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
