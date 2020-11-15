package Business;

import java.awt.*;
import java.util.Iterator;

public class GameGrid implements Iterable {
    final int COLUMNS;
    final int ROWS;

    private final GameObject[][] gameObjects;

    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        // Initialize the array
        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    public GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    public GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        // if structure improved
        if (!isPointOutOfBounds(col, row)) {
            return gameObjects[col][row];
        }

        if (GameEngine.isDebugActive()) {
            System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
        }
        throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
    }

    public GameObject getGameObjectAt(Point point) throws IllegalArgumentException {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) point.getX(), (int) point.getY());
    }

//    public boolean removeGameObjectAt(Point position) {
//        return putGameObjectAt(null, position);
//    }

    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
    }

    public boolean putGameObjectAt(GameObject gameObject, Point point) {
        return point != null && putGameObjectAt(gameObject, (int) point.getX(), (int) point.getY());
    }

    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(gameObjects.length); // variable name changed

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                stringBuilder.append(aGameObject.getCharSymbol());
            }

            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}