package Business;

public enum GameObject {
    WALL('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    DEBUG_OBJECT('=');

    private final char symbol;

    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    // method name changed
    public static GameObject toGameObject(char character) {
        for (GameObject gameObject : GameObject.values()) {
            if (Character.toUpperCase(character) == gameObject.symbol) {
                return gameObject;
            }
        }
        return WALL;
    }

    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    public char getCharSymbol() {
        return symbol;
    }
}