package Model;

import Utils.GameLogger;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;

/**
 * GameGrid is a class that stores the position information of all objects in the map
 */
public class GameGrid implements Iterable, Serializable {

    /**
     * column number
     */
    final int COLUMNS;
    /**
     * row number
     */
    final int ROWS;
    /**
     * 2D array of objects
     */
    private final char[][] gameObjects;

    /**
     * Instantiates a new Game grid.
     *
     * @param columns column number
     * @param rows    row number
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;
        gameObjects = createGrid(); // Initialize the array
    }

    /**
     * factory method for creating grid
     *
     * @return game object grid [ ] [ ]
     */
    private char[][] createGrid() {
        final char[][] gameObjects;
        gameObjects = new char[COLUMNS][ROWS];
        return gameObjects;
    }

    /**
     * calculate target position from given delta
     *
     * @param sourceLocation the source location
     * @param delta          the delta
     * @return the target position
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * Gets target symbol after target point is calculated
     *
     * @param source the source
     * @param delta  the delta
     * @return the target from source
     */
    public char getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Gets dimension.
     *
     * @return the dimension
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    /**
     * Gets game object from coordinate
     *
     * @param col the column index
     * @param row the row index
     * @return the game object symbol
     * @throws ArrayIndexOutOfBoundsException the array index out of bounds exception
     */
    public char getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (!isPointOutOfBounds(col, row)) {
            return gameObjects[col][row];
        }

        if (GameLogger.isDebugActive()) {
            System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
        }

        throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
    }

    /**
     * Gets game object from point
     *
     * @param point the point
     * @return the game object at
     * @throws IllegalArgumentException the illegal argument exception
     */
    public char getGameObjectAt(Point point) throws IllegalArgumentException {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) point.getX(), (int) point.getY());
    }

    /**
     * Put game object at some coordinate
     *
     * @param gameObject the game object
     * @param x
     * @param y
     * @return the boolean
     */
    public boolean putGameObjectAt(char gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }
        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
    }

    /**
     * Put game object at some point
     *
     * @param gameObject the game object
     * @param point      the point
     * @return the boolean
     */
    public boolean putGameObjectAt(char gameObject, Point point) {
        return point != null && putGameObjectAt(gameObject, (int) point.getX(), (int) point.getY());
    }

    /**
     * check if point is out of bounds
     *
     * @param x
     * @param y
     * @return the boolean
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * check if point is out of bounds
     *
     * @param p the point
     * @return the boolean
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(gameObjects.length); // variable name changed

        for (char[] gameObject : gameObjects) {
            for (char aGameObject : gameObject) {
                stringBuilder.append(aGameObject);
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    @Override
    public Iterator<Character> iterator() {
        return new GridIterator();
    }

    /**
     * The iterator of grid
     */
    public class GridIterator implements Iterator<Character> {
        /**
         * The Row index
         */
        int row = 0;
        /**
         * The Column index
         */
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        @Override
        public Character next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}