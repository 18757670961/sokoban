package Utils;

import Controller.GameEngine;
import Utils.GameLogger;
import Modal.GameStatus;
import Modal.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class FileParser {
    /**
     * Parse file.
     *
     * @param input the input
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
     * Load game file list.
     *
     * @param input the input
     * @return the list
     */
// method name changed
    public static final List<Level> prepareFileReader(InputStream input) {
        List<Level> levels = new ArrayList<>(5);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
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
     * @param fileInfo the file info
     * @throws IOException the io exception
     */
// accept parameter object fileInfo
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
// method extracted
    private static void parseFinalLevel(FileInfo fileInfo) {
        if (fileInfo.rawLevel.size() != 0) {
            Level parsedLevel = new Level(fileInfo.levelName, ++fileInfo.levelIndex, fileInfo.rawLevel);
            fileInfo.levels.add(parsedLevel);
        }
    }

    /**
     * Parse level.
     *
     * @param fileInfo the file info
     * @param line     the line
     */
// method extracted
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
     * The type File info.
     */
    private static class FileInfo {
        /**
         * The Levels.
         */
        private final List<Level> levels;
        /**
         * The Level index.
         */
        private int levelIndex;
        /**
         * The Reader.
         */
        private final BufferedReader reader;
        /**
         * The Parsed first level.
         */
        private boolean parsedFirstLevel;
        /**
         * The Raw level.
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
