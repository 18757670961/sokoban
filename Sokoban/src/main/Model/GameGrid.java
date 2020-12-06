package Model;

import Utils.GameLogger;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;

/**
 * The type Game grid.
 */
public class GameGrid implements Iterable, Serializable {
    /**
     * The Columns.
     */
    final int COLUMNS;
    /**
     * The Rows.
     */
    final int ROWS;
    /**
     * The Game objects.
     */
    private final char[][] gameObjects;

    /**
     * Instantiates a new Game grid.
     *
     * @param columns the columns
     * @param rows    the rows
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        // Initialize the array
        gameObjects = createGrid();
    }

    /**
     * Create grid game object [ ] [ ].
     *
     * @return the game object [ ] [ ]
     */
// factory method
    private char[][] createGrid() {
        final char[][] gameObjects;
        gameObjects = new char[COLUMNS][ROWS];
        return gameObjects;
    }

    /**
     * Translate point point.
     *
     * @param sourceLocation the source location
     * @param delta          the delta
     * @return the point
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
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
     * Gets target from source.
     *
     * @param source the source
     * @param delta  the delta
     * @return the target from source
     */
    public char getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Gets game object at.
     *
     * @param col the col
     * @param row the row
     * @return the game object at
     * @throws ArrayIndexOutOfBoundsException the array index out of bounds exception
     */
    public char getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        // if structure improved
        if (!isPointOutOfBounds(col, row)) {
            return gameObjects[col][row];
        }

        if (GameLogger.isDebugActive()) {
            System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
        }

        throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
    }

    /**
     * Gets game object at.
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

//    public boolean removeGameObjectAt(Point position) {
//        return putGameObjectAt(null, position);
//    }

    /**
     * Put game object at boolean.
     *
     * @param gameObject the game object
     * @param x          the x
     * @param y          the y
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
     * Put game object at boolean.
     *
     * @param gameObject the game object
     * @param point      the point
     * @return the boolean
     */
    public boolean putGameObjectAt(char gameObject, Point point) {
        return point != null && putGameObjectAt(gameObject, (int) point.getX(), (int) point.getY());
    }

    /**
     * Is point out of bounds boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * Is point out of bounds boolean.
     *
     * @param p the p
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
//                if (aGameObject == 'x') {
//                    aGameObject = '=';
//                }
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
     * The type Grid iterator.
     */
    public class GridIterator implements Iterator<Character> {
        /**
         * The Row.
         */
        int row = 0;
        /**
         * The Column.
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