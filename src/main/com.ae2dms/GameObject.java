package com.ae2dms;

public enum GameObject {
    WALL('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    DEBUG_OBJECT('=');

    public final char symbol;
    GameObject(final char symbol) {
        this.symbol = symbol;
    }
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
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