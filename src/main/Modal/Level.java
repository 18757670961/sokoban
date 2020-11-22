package Modal;

import Controller.GameEngine;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public final class Level implements Iterable<GameObject>, Serializable {
    public final GameGrid objectsGrid;
    public final GameGrid diamondsGrid;
    private final String name;
    private final int index;
    private int numberOfDiamonds = 0;
    private Point keeperPosition;

    public Level(String levelName, int levelIndex, List<String> rawLevel) {
        if (GameEngine.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        name = levelName;
        index = levelIndex;
        keeperPosition = new Point(0, 0);

        int rows = rawLevel.size();
        int columns = rawLevel.get(0).trim().length();

        objectsGrid = createGrid(rows, columns);
        diamondsGrid = createGrid(rows, columns);

        parseRawLevel(rawLevel); // method extracted
    }

    // factory method
    private GameGrid createGrid(int rows, int columns) {
        final GameGrid objectsGrid;
        objectsGrid = new GameGrid(rows, columns);
        return objectsGrid;
    }

    private void parseRawLevel(List<String> rawLevel) {
        for (int row = 0; row < rawLevel.size(); row++) {
            for (int col = 0; col < rawLevel.get(row).length(); col++) {
                GameObject curTile = GameObject.toGameObject(rawLevel.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
                curTile = null;
            }
        }
    }

    public boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                // if condition simplified
                if (getObject(row, col) == GameObject.CRATE && getDiamond(row, col) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }
        return cratedDiamondsCount >= numberOfDiamonds;
    }

    private GameObject getDiamond(int row, int col) {
        return diamondsGrid.getGameObjectAt(col, row);
    }

    private GameObject getObject(int row, int col) {
        return objectsGrid.getGameObjectAt(col, row);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Point getKeeperPosition() {
        return keeperPosition;
    }

    public GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        @Override
        public GameObject next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }
            GameObject object = getObject(row, column);
            GameObject diamond = getDiamond(row, column);
            GameObject retObj = object;
            column++;
            // if structure improved
            if (diamond != GameObject.DIAMOND) {
                return retObj;
            }
            if (object == GameObject.CRATE) {
                retObj = GameObject.CRATE_ON_DIAMOND;
            }
            if (object == GameObject.FLOOR) {
                retObj = diamond;
            }
            return retObj;
        }

        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }
}