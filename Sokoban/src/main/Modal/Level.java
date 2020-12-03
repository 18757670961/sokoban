package Modal;

import Utils.GameLogger;

import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * The type Level.
 */
public final class Level implements Iterable<Character>, Serializable {
    /**
     * The Objects grid.
     */
    public final GameGrid objectsGrid;
    /**
     * The Diamonds grid.
     */
    public final GameGrid diamondsGrid;
    /**
     * The Keeper position.
     */
    private Point keeperPosition;
    /**
     * The Name.
     */
    private final String name;
    /**
     * The Index.
     */
    private final int index;
    /**
     * The Number of diamonds.
     */
    private int numberOfDiamonds = 0;

    /**
     * Instantiates a new Level.
     *
     * @param levelName  the level name
     * @param levelIndex the level index
     * @param rawLevel   the raw level
     */
    public Level(String levelName, int levelIndex, List<String> rawLevel) {
        if (GameLogger.isDebugActive()) {
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

    /**
     * Create grid game grid.
     *
     * @param rows    the rows
     * @param columns the columns
     * @return the game grid
     */
// factory method
    private GameGrid createGrid(int rows, int columns) {
        final GameGrid objectsGrid;
        objectsGrid = new GameGrid(rows, columns);
        return objectsGrid;
    }

    /**
     * Parse raw level.
     *
     * @param rawLevel the raw level
     */
    private void parseRawLevel(List<String> rawLevel) {
        for (int row = 0; row < rawLevel.size(); row++) {
            for (int col = 0; col < rawLevel.get(row).length(); col++) {
                char curTile = rawLevel.get(row).charAt(col);

                if (curTile == 'G') {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = ' ';
                } else if (curTile == 'S') {
                    keeperPosition = new Point(row, col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
            }
        }
    }

    public Level deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (Level) objectInputStream.readObject();
    }

    /**
     * Is complete boolean.
     *
     * @return the boolean
     */
    public boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                // if condition simplified
                if (getObject(row, col) == 'C' && getDiamond(row, col) == 'G') {
                    cratedDiamondsCount++;
                }
            }
        }
        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * Gets target object.
     *
     * @param source the source
     * @param delta  the delta
     * @return the target object
     */
    public char getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * scan the map for another pipe as exit point
     * @param currentPipe
     * @return
     */
    public Point getAnotherPipe(Point currentPipe) {
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (row == currentPipe.y && col == currentPipe.x) {
                    continue;
                }
                char object = getObject(row, col);
                switch (object) {
                    case 'U':
                        return new Point(col - 1, row);
                    case 'D':
                        return new Point(col + 1, row);
                    case 'L':
                        return new Point(col, row - 1);
                    case 'R':
                        return new Point(col, row + 1);
                }
            }
        }
        return null;
    }

    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    /**
     * Gets diamond.
     *
     * @param row the row
     * @param col the col
     * @return the diamond
     */
    private char getDiamond(int row, int col) {
        return diamondsGrid.getGameObjectAt(col, row);
    }

    /**
     * Gets object.
     *
     * @param row the row
     * @param col the col
     * @return the object
     */
    private char getObject(int row, int col) {
        return objectsGrid.getGameObjectAt(col, row);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets keeper position.
     *
     * @return the keeper position
     */
    public Point getKeeperPosition() {
        return keeperPosition;
    }

    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    @Override
    public Iterator<Character> iterator() {
        return new LevelIterator();
    }

    /**
     * The type Level iterator.
     */
    public class LevelIterator implements Iterator<Character> {

        /**
         * The Column.
         */
        int column = 0;
        /**
         * The Row.
         */
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        @Override
        public Character next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }
            char object = getObject(row, column);
            char diamond = getDiamond(row, column);
            char retObj = object;
            column++;

            // if structure improved
            if (diamond != 'G') {
                return retObj;
            }
            if (object == 'C') {
                retObj = 'O';
            }
            if (object == ' ') {
                retObj = diamond;
            }
            return retObj;
        }

        /**
         * Gets current position.
         *
         * @return the current position
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }
}