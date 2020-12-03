package Controller;

import Utils.GameFile;
import Utils.GameLogger;
import Modal.GameStatus;
import Modal.History;
import View.GameWindow;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.IOException;

public final class KeyHandler {
    public static void handleKey(KeyCode code) throws IOException {
        // switch replaced with enhanced switch
        switch (code) {
            case W:
            case UP:
                GameEngine.handleMovement(new Point(-1, 0));
                reload();
                break;

            case D:
            case RIGHT:
                GameEngine.handleMovement(new Point(0, 1));
                reload();
                break;

            case S:
            case DOWN:
                GameEngine.handleMovement(new Point(1, 0));
                reload();
                break;

            case A:
            case LEFT:
                GameEngine.handleMovement(new Point(0, -1));
                reload();
                break;

            case F1:
                GameFile.saveGameFile(GameWindow.getPrimaryStage()); // save game
                break;

            case F2:
                GameEngine.loadGame(GameWindow.getPrimaryStage()); // load game
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

    private static void reload() throws IOException {
        if (GameStatus.getGameStatus().getMovesCountLevel() == 0) {
            GameWindow.reloadGrid();
        } else {
            GameWindow.reloadPartialGrid(null);
        }
    }
}
