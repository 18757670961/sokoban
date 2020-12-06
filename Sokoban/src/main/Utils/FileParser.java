package Utils;

import Controller.GameEngine;
import Model.GameStatus;
import Model.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * FileParser is a utility class for reading and parsing map information
 * line by line from file input stream
 */
public class FileParser {

    /**
     * start file parsing
     *
     * @param input the file input
     */
    public static void parseFile(InputStream input) {
        try {
            GameStatus.getGameStatus().setLevels(prepareFileReader(input));
            GameStatus.getGameStatus().setCurrentLevel(GameEngine.getNextLevel());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * prepare for file reading
     *
     * @param input the file input
     * @return the list
     */
    public static final List<Level> prepareFileReader(InputStream input) {
        List<Level> levels = new ArrayList<>(5);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false; // if the first level is being parsed
            List<String> rawLevel = new ArrayList<>(); // string representation of level
            String levelName = "";
            int levelIndex = 0;
            readGameFile(new FileInfo(levels, levelIndex, reader, parsedFirstLevel, rawLevel, levelName));
        } catch (IOException e) {
            GameLogger.showSevere("Error trying to load the game file: " + e);
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            GameLogger.showSevere("Cannot open the requested file: " + e);
            System.out.println(e.getMessage());
        }

        return levels;
    }

    /**
     * Read game file.
     *
     * @param fileInfo the information of file being read (parameter object)
     * @throws IOException the io exception
     */
    public static void readGameFile(FileInfo fileInfo) throws IOException {
        while (true) {
            String line = fileInfo.reader.readLine();

            if (line == null) {
                parseFinalLevel(fileInfo);
                break;
            }

            if (line.contains("MapSetName")) {
                GameStatus.getGameStatus().setMapSetName(line.replace("MapSetName: ", ""));
                continue;
            }

            if (line.contains("LevelName")) {
                parseLevel(fileInfo, line);
                continue;
            }

            line = line.trim();
            line = line.toUpperCase();
            if (line.matches(".*W.*W.*")) {
                fileInfo.rawLevel.add(line);
            }
        }
    }

    /**
     * Parse final level.
     *
     * @param fileInfo the file info
     */
    private static void parseFinalLevel(FileInfo fileInfo) {
        if (fileInfo.rawLevel.size() != 0) {
            Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
            fileInfo.levels.add(parsedLevel); // insert parsed level into level list
        }
    }

    /**
     * Parse level
     *
     * @param fileInfo the file info
     * @param line     the line being read
     */
    private static void parseLevel(FileInfo fileInfo, String line) {
        if (fileInfo.parsedFirstLevel) {
            Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
            fileInfo.levels.add(parsedLevel);
            fileInfo.rawLevel.clear();
        } else {
            fileInfo.parsedFirstLevel = true;
        }

        fileInfo.levelName = line.replace("LevelName: ", "");
    }

    /**
     * Inner class for level information passing
     */
    private static class FileInfo {
        /**
         * The Level list
         */
        private final List<Level> levels;
        /**
         * The Level index in list
         */
        private int levelIndex;
        /**
         * The Reader.
         */
        private final BufferedReader reader;
        /**
         * if the first level is being parsed
         */
        private boolean parsedFirstLevel;
        /**
         * The Raw level list
         */
        private final List<String> rawLevel;
        /**
         * The Level name.
         */
        private String levelName;

        /**
         * Instantiates a new File info.
         *
         * @param levels           the levels
         * @param levelIndex       the level index
         * @param reader           the reader
         * @param parsedFirstLevel the parsed first level
         * @param rawLevel         the raw level
         * @param levelName        the level name
         */
        private FileInfo(List<Level> levels, int levelIndex, BufferedReader reader, boolean parsedFirstLevel, List<String> rawLevel, String levelName) {
            this.levels = levels;
            this.levelIndex = levelIndex;
            this.reader = reader;
            this.parsedFirstLevel = parsedFirstLevel;
            this.rawLevel = rawLevel;
            this.levelName = levelName;
        }
    }
}
