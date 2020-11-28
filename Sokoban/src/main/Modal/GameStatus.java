package Modal;

import Controller.FileParser;
import Controller.GameEngine;
import Debug.GameLogger;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public final class GameStatus implements Serializable {
    private static GameStatus gameStatus;
    private final String GAME_NAME = "Sokoban";
    private String mapSetName = "";
    private int movesCount = 0;
    private int movesCountLevel = 0;
    private Level currentLevel = null;
    private List<Level> levels = null;
    private Level[] serializableLevels = null;
    private boolean gameComplete = false;

    private GameStatus() {}

    public static void createGameStatus(GameStatus status) {
        gameStatus = status;
    }

    public static void createGameStatus(InputStream input) {
        gameStatus = new GameStatus();
        FileParser.parseFile(input);
    }

    /**
     * Gets game engine.
     *
     * @return the game engine
     */
    public static GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Is game complete boolean.
     *
     * @return the boolean
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    public void setGameComplete() {
        gameComplete = true;
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets current level.
     *
     * @param level the level
     */
    public void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    /**
     * Gets game name.
     *
     * @return the game name
     */
    public String getGameName() {
        return GAME_NAME;
    }

    /**
     * Gets moves count.
     *
     * @return the moves count
     */
    public int getMovesCount() {
        return movesCount;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    public void incrementMovesCount() {
        this.movesCount++;
    }

    public int getMovesCountLevel() {
        return movesCountLevel;
    }

    public void setMovesCountLevel(int movesCountLevel) {
        this.movesCountLevel = movesCountLevel;
    }

    public void incrementMovesCountLevel() {
        this.movesCountLevel++;
    }

    /**
     * Gets map set name.
     *
     * @return the map set name
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * Sets map set name.
     *
     * @param name the name
     */
    public void setMapSetName(String name) {
        mapSetName = name;
    }

    /**
     * Gets levels.
     *
     * @return the levels
     */
    public final List<Level> getLevels() {
        return levels;
    }

    /**
     * Sets levels.
     *
     * @param newLevels the new levels
     */
    public void setLevels(List<Level> newLevels) {
        levels = newLevels;
    }

    /**
     * Get serializable levels level [ ].
     *
     * @return the level [ ]
     */
    public Level[] getSerializableLevels() {
        return serializableLevels;
    }

    /**
     * Sets serializable levels.
     *
     * @param levels the levels
     */
    public void setSerializableLevels(Level[] levels) {
        serializableLevels = levels;
    }
}
