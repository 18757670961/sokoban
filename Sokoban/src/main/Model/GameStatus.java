package Model;

import Utils.FileParser;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * GameStatus is a singleton class that stores all useful information of the game
 * it is also stored into game file when saving and loading
 */
public final class GameStatus implements Serializable {

    /**
     * all access to this class object must be through this single reference
     */
    private static GameStatus gameStatus;
    /**
     * The Game name.
     */
    private final String GAME_NAME = "Sokoban";
    /**
     * The Map set name.
     */
    private String mapSetName = "";
    /**
     * The total Moves count.
     */
    private int movesCount = 0;
    /**
     * The Moves count for current level.
     */
    private int movesCountLevel = 0;
    /**
     * The Current level.
     */
    private Level currentLevel = null;
    /**
     * The Level list
     */
    private List<Level> levels = null;
    /**
     * The Serializable level array for game file IO
     * since list type cannot be serialized
     */
    private Level[] serializableLevels = null;
    /**
     * if game is completed
     */
    private boolean gameComplete = false;

    /**
     * Instantiates a new Game status.
     */
    private GameStatus() {}

    /**
     * Create game status.
     *
     * @param status the status read from saved file
     */
    public static void createGameStatus(GameStatus status) {
        gameStatus = status;
    }

    /**
     * Create game status.
     *
     * @param input the input file stream
     */
    public static void createGameStatus(InputStream input) {
        gameStatus = new GameStatus();
        FileParser.parseFile(input);
    }

    /**
     * Gets game status
     *
     * @return the game status
     */
    public static GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Is game completed
     *
     * @return the boolean
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    /**
     * Sets game to be complete.
     */
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
     * Gets total moves count.
     *
     * @return the moves count
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Sets total moves count.
     *
     * @param movesCount the moves count
     */
    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    /**
     * Increment total moves count.
     */
    public void incrementMovesCount() {
        this.movesCount++;
    }

    /**
     * Gets moves count for current level.
     *
     * @return the moves count level
     */
    public int getMovesCountLevel() {
        return movesCountLevel;
    }

    /**
     * Sets moves count for current level.
     *
     * @param movesCountLevel the moves count level
     */
    public void setMovesCountLevel(int movesCountLevel) {
        this.movesCountLevel = movesCountLevel;
    }

    /**
     * Increment moves count for current level.
     */
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
     * Gets level list
     *
     * @return the level list
     */
    public final List<Level> getLevels() {
        return levels;
    }

    /**
     * Sets level list
     *
     * @param newLevels the new level list
     */
    public void setLevels(List<Level> newLevels) {
        levels = newLevels;
    }

    /**
     * Get serializable level array
     *
     * @return the level array
     */
    public Level[] getSerializableLevels() {
        return serializableLevels;
    }

    /**
     * Sets serializable level array
     *
     * @param levels the level array
     */
    public void setSerializableLevels(Level[] levels) {
        serializableLevels = levels;
    }
}
