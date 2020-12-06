package Model;

import java.util.Stack;

/**
 * History is a class that stores move history for specific level using Stack ADT
 * it is used for undo, reset function and partial gird loading
 * it stores the level object for each move
 */
public final class History {

    /**
     * The history list
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
     * retrieve the level object of last move
     * set it to current level
     *
     * @return the level
     */
    public static Level traceHistory() {
        Level currentLevel = GameStatus.getGameStatus().getCurrentLevel();
        if (!historyList.isEmpty()) {
            Level previousLevel = historyList.pop();
            GameStatus.getGameStatus().setCurrentLevel(previousLevel);
        }
        return currentLevel;
    }

    /**
     * Reset current level to the first level in the history stack
     *
     * @return the level
     */
    public static Level resetHistory() {
        Level currentLevel = GameStatus.getGameStatus().getCurrentLevel();
        if (!historyList.isEmpty()) {
            Level initialLevel = historyList.get(0);
            int currentIndex = currentLevel.getIndex();
            GameStatus.getGameStatus().getLevels().set(currentIndex - 1, initialLevel); // synchronize the level list
            GameStatus.getGameStatus().setCurrentLevel(initialLevel);
            historyList.clear();
        }
        return currentLevel;
    }
}
