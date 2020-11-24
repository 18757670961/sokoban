package Modal;

/**
 * The enum Game object.
 */
public enum GameObject {
    /**
     * Wall game object.
     */
    WALL('W'),
    /**
     * Floor game object.
     */
    FLOOR(' '),
    /**
     * Crate game object.
     */
    CRATE('C'),
    /**
     * Diamond game object.
     */
    DIAMOND('D'),
    /**
     * Keeper game object.
     */
    KEEPER('S'),
    /**
     * Crate on diamond game object.
     */
    CRATE_ON_DIAMOND('O'),
    /**
     * Debug object game object.
     */
    DEBUG_OBJECT('=');

    /**
     * The Symbol.
     */
    private final char symbol;

    /**
     * Instantiates a new Game object.
     *
     * @param symbol the symbol
     */
    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * To game object game object.
     *
     * @param character the character
     * @return the game object
     */
// method name changed
    public static GameObject toGameObject(char character) {
        for (GameObject gameObject : GameObject.values()) {
            if (Character.toUpperCase(character) == gameObject.symbol) {
                return gameObject;
            }
        }
        return WALL;
    }

    /**
     * Gets string symbol.
     *
     * @return the string symbol
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     * Gets char symbol.
     *
     * @return the char symbol
     */
    public char getCharSymbol() {
        return symbol;
    }
}