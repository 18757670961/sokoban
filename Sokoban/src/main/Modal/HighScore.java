package Modal;

import Controller.GameFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HighScore implements Serializable {
    private static File file = GameFile.getFile("./src/main/resources/level/score.dat");
    private static Map<Integer, Integer> highScoreMap = new HashMap<>();

    public static void loadMap() {
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file)))
            {
                highScoreMap = (HashMap) in.readObject();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void saveMap() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
        {
            out.writeObject(highScoreMap);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void updateMap(int levelIndex, int move) {
        if (highScoreMap.containsKey(levelIndex)) {
            int leastMove = highScoreMap.get(levelIndex);
            if (move < leastMove) {
                highScoreMap.put(levelIndex, move);
            }
        } else {
            highScoreMap.put(levelIndex, move);
        }
    }

    public static int getHighScore(int levelIndex) {
        if (highScoreMap.containsKey(levelIndex)) {
            return highScoreMap.get(levelIndex);
        } else {
            return 0;
        }
    }

    public static Map getHighScoreList() {
        return highScoreMap;
    }
}
