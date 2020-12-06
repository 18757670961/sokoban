package Utils;

/**
 * CheatSheet is a utility class that stores key mapping information
 */
public class CheatSheet {

    /**
     * The Key name
     */
    private final String key;
    /**
     * The Function of pressing this key
     */
    private final String function;

    /**
     * Instantiates a new Cheat sheet.
     *
     * @param key      the key name
     * @param function the function
     */
    public CheatSheet(String key, String function) {
        this.key = key;
        this.function = function;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets function.
     *
     * @return the function
     */
    public String getFunction() {
        return function;
    }
}
