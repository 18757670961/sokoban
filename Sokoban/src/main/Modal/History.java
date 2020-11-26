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
    public static void traceHistory() {
        Level previousLevel;
        if (!historyList.isEmpty()) {
            previousLevel = historyList.pop();
            GameEngine.getGameEngine().setCurrentLevel(previousLevel);
        }
    }

    /**
     * Reset history.
     */
    public static void resetHistory() {
        Level previousLevel;
        if (!historyList.isEmpty()) {
            previousLevel = historyList.get(0);
            GameEngine.getGameEngine().setCurrentLevel(previousLevel);
            historyList.clear();
        }
    }
}
