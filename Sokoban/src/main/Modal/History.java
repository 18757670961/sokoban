package Modal;

import Controller.GameEngine;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The type History.
 */
public final class History {
    /**
     * The constant historyList.
     */
    private static Stack<Level> historyList = new Stack<>();

    /**
     * Instantiates a new History.
     */
    private History() {}

    /**
     * Gets history.
     *
     * @return the history
     */
    public static Stack<Level> getHistory() {
        return historyList;
    }

    /**
     * Trace history.
     */
    public static Level traceHistory() {
        Level currentLevel = GameEngine.getGameEngine().getCurrentLevel();
        if (!historyList.isEmpty()) {
            Level previousLevel = historyList.pop();
            GameEngine.getGameEngine().setCurrentLevel(previousLevel);
        }
        return currentLevel;
    }

    /**
     * Reset history.
     */
    public static Level resetHistory() {
        Level currentLevel = GameEngine.getGameEngine().getCurrentLevel();
        if (!historyList.isEmpty()) {
            Level initialLevel = historyList.get(0);
            int currentIndex = GameEngine.getGameEngine().getCurrentLevel().getIndex();
            GameEngine.getGameEngine().getLevels().set(currentIndex - 1, initialLevel);
            GameEngine.getGameEngine().setCurrentLevel(initialLevel);
            historyList.clear();
        }
        return currentLevel;
    }
}