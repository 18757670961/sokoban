package Model;

import Utils.GameLogger;

import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Level is a class that stores the realted information for specific level
 * and deals with low-level grid manipulation, object relocation and map parsing
 */
public final class Level implements Iterable<Character>, Serializable {

    /**
     * The Objects grid.
     */
    public final GameGrid objectsGrid;
    /**
     * The Diamonds grid (for checking if any object is on diamond / pressure pad)
     */
    public final GameGrid diamondsGrid;
    /**
     * The Keeper position.
     */
    private Point keeperPosition;
    /**
     * The Gate position.
     */
    private Point gatePosition;
    /**
     * The Pressure Pad position.
     */
    private Point padPosition;
    /**
     * The Level Name.
     */
    private final String name;
    /**
     * The Index of level in level list
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
     * @param rawLevel   the raw level (pure string representation)
     */
    public Level(String levelName, int levelIndex, List<String> rawLevel) {
        if (GameLogger.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        name = levelName;
        index = levelIndex;
        keeperPosition = new Point(0, 0);
        gatePosition = new Point(0, 0);
        padPosition = new Point(0, 0);

        int rows = rawLevel.size();
        int columns = rawLevel.get(0).trim().length();
        objectsGrid = createGrid(rows, columns);
        diamondsGrid = createGrid(rows, columns);

        parseRawLevel(rawLevel);
    }

    /**
     * factory method for creating game grid.
     *
     * @param rows    the row number
     * @param columns the column number
     * @return the game grid
     */
    private GameGrid createGrid(int rows, int columns) {
        final GameGrid objectsGrid;
        objectsGrid = new GameGrid(rows, columns);
        return objectsGrid;
    }

    /**
     * Parse raw level into grid
     *
     * @param rawLevel the raw level
     */
    private void parseRawLevel(List<String> rawLevel) {
        for (int row = 0; row < rawLevel.size(); row++) {
            for (int col = 0; col < rawLevel.get(row).length(); col++) {
                char curTile = rawLevel.get(row).charAt(col);

                if (curTile == 'G') { // diamond (destination)
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = ' ';
                } else if (curTile == 'S') { // keeper
                    keeperPosition = new Point(row, col);
                } else if (curTile == '&') { // pressure pad
                    padPosition = new Point(row, col);
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = ' ';
                } else if (curTile == '$') { // gate
                    gatePosition = new Point(row, col);
                } else if (curTile == 'O') { // crate on diamond
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt('G', row, col);
                    curTile = 'C';
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
            }
        }
    }

    /**
     * Deep clone level for history storage (avoid problems caused by reference when copying)
     * serialize level object first and then deserialize back
     *
     * @return the level
     * @throws IOException            the io exception
     * @throws ClassNotFoundException the class not found exception
     */
    public Level deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(this); // serialize
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (Level) objectInputStream.readObject(); // deserialize
    }

    /**
     * Check and set the state of gate
     */
    public void checkGate() {
        if (gatePosition.x == 0 && gatePosition.y == 0) { // no gate
            return;
        }
        if (objectsGrid.getGameObjectAt(padPosition) == ' ') { // pressure pad nonactivated
            if (objectsGrid.getGameObjectAt(gatePosition) == 'C') { // crate on gate
                objectsGrid.putGameObjectAt('#', gatePosition);
            } else if (objectsGrid.getGameObjectAt(gatePosition) != '#') {
                objectsGrid.putGameObjectAt('$', gatePosition);
            }
        } else { // pressure pad activated
            if (objectsGrid.getGameObjectAt(gatePosition) == '$') { // open gate
                objectsGrid.putGameObjectAt(' ', gatePosition);
            } else if (objectsGrid.getGameObjectAt(gatePosition) == '#') { // release crate on gate
                objectsGrid.putGameObjectAt('C', gatePosition);
            }
        }
    }

    /**
     * Is level completed
     *
     * @return the boolean
     */
    public boolean isComplete() {
        int cratedDiamondsCount = 0; // number of crate on diamond
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (getObject(row, col) == 'C' && getDiamond(row, col) == 'G') {
                    cratedDiamondsCount++;
                }
            }
        }
        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * scan the map for another pipe as exit point
     *
     * @param currentPipe the current pipe
     * @return another pipe
     */
    public Point getAnotherPipe(Point currentPipe) {
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                if (row == currentPipe.y && col == currentPipe.x) {
                    continue; // skip entrance pipe
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

    /**
     * Gets target object symbol
     *
     * @param source the source point
     * @param delta  the delta
     * @return the target object
     */
    public char getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Gets objects grid.
     *
     * @return the objects grid
     */
    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    /**
     * Gets diamond at specific coordinate
     *
     * @param row the row index
     * @param col the col index
     * @return the diamond symbol
     */
    private char getDiamond(int row, int col) {
        return diamondsGrid.getGameObjectAt(col, row);
    }

    /**
     * Gets object at specific coordinate
     *
     * @param row the row index
     * @param col the col index
     * @return the object symbol
     */
    private char getObject(int row, int col) {
        return objectsGrid.getGameObjectAt(col, row);
    }

    /**
     * Gets level name.
     *
     * @return the level name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets level index.
     *
     * @return the level index
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

    /**
     * Gets gate position.
     *
     * @return the gate position
     */
    public Point getGatePosition() {
        return gatePosition;
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
     * The iterator of level
     */
    public class LevelIterator implements Iterator<Character> {

        /**
         * The Column index
         */
        int column = 0;
        /**
         * The Row index
         */
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        @Override
        public Character next() {
            if (column >= objectsGrid.COLUMNS) { // boundary check
                column = 0;
                row++;
            }
            char object = getObject(row, column);
            char diamond = getDiamond(row, column);
            char retObj = object;
            column++;

            if (diamond == 'G') {
                if (object == 'C') { // crate on diamond
                    retObj = 'O';
                }
                if (object == ' ') { // nothing on diamond
                    retObj = diamond;
                }
            }

            if (diamond == '&') {
                if (object == 'C') { // crate locked on gate
                    retObj = 'P';
                }
                if (object == ' ') { // noting on gate
                    retObj = diamond;
                }
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