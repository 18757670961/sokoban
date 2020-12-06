package Model;

import Utils.GameIO;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * HighScore is a class that uses Map ADT and file to store the high score of each level or whole game permanently
 */
public class HighScore implements Serializable {

    /**
     * The file object that specify the location of high score file
     */
    private static File file = GameIO.getFile("./src/main/resources/level/score.dat");
    /**
     * The Map that store the high score and related level index
     */
    private static Map<Integer, Integer> highScoreMap = new HashMap<>();

    /**
     * Load high score Map from local file
     */
    public static void loadMap() {
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                highScoreMap = (HashMap) in.readObject(); // deserialize Map object from file
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save high score Map to local file
     */
    public static void saveMap() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(highScoreMap); // serialize Map object
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update high score map.
     *
     * @param levelIndex the level index
     * @param move       the move count
     */
    public static void updateMap(int levelIndex, int move) {
        if (highScoreMap.containsKey(levelIndex)) {
            int leastMove = highScoreMap.get(levelIndex);
            if (move < leastMove) { // compare current move count with the archived least move
                highScoreMap.put(levelIndex, move);
            }
        } else { // no record for level index
            highScoreMap.put(levelIndex, move);
        }
    }

    /**
     * Gets high score for specific level
     *
     * @param levelIndex the level index
     * @return the high score
     */
    public static int getHighScore(int levelIndex) {
        if (highScoreMap.containsKey(levelIndex)) {
            return highScoreMap.get(levelIndex);
        } else {
            return 0;
        }
    }

    /**
     * Gets high score Map
     *
     * @return the high score Map
     */
    public static Map getHighScoreList() {
        return highScoreMap;
    }
}
