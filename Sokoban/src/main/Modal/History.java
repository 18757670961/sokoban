package Modal;

import Controller.GameEngine;

import java.util.ArrayList;
import java.util.Stack;

public final class History {
    private static Stack<Level> historyList = new Stack<>();

    private History() {}

    public static Stack<Level> getHistory() {
        return historyList;
    }

    public static void traceHistory() {
        Level previousLevel;
        if (!historyList.isEmpty()) {
            previousLevel = historyList.pop();
            GameEngine.getGameEngine().setCurrentLevel(previousLevel);
        }
    }

    public static void resetHistory() {
        Level previousLevel;
        if (!historyList.isEmpty()) {
            previousLevel = historyList.get(0);
            GameEngine.getGameEngine().setCurrentLevel(previousLevel);
            historyList.clear();
        }
    }
}
