package Modal;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HighestScore implements Serializable {
    private static File file = GameFile.getFile("./src/main/resources/level/score.dat");
    private static Map<Integer, Integer> highestScoreMap = new HashMap<>();

    public static void loadMap() {
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file)))
            {
                highestScoreMap = (HashMap) in.readObject();
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
            out.writeObject(highestScoreMap);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void updateMap(int levelIndex, int move) {
        if (highestScoreMap.containsKey(levelIndex)) {
            int leastMove = highestScoreMap.get(levelIndex);
            if (move < leastMove) {
                highestScoreMap.put(levelIndex, move);
            }
        } else {
            highestScoreMap.put(levelIndex, move);
        }
    }

    public static int getHighestScore(int levelIndex) {
        if (highestScoreMap.containsKey(levelIndex)) {
            return highestScoreMap.get(levelIndex);
        } else {
            return 0;
        }
    }

    public static Map getHighestScoreList() {
        return highestScoreMap;
    }
}
