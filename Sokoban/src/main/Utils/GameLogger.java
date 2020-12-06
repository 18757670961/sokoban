package Utils;

import Controller.PositionInfo;
import Model.GameStatus;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * singleton class
 */
public final class GameLogger extends Logger {

    /**
     * The constant gameLogger.
     */
    private static GameLogger gameLogger;
    /**
     * The constant logger.
     */
    private static final Logger logger = Logger.getLogger("GameLogger");
    /**
     * The constant dateFormat.
     */
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    /**
     * The constant calendar.
     */
    private static final Calendar calendar = Calendar.getInstance();
    /**
     * The constant debug.
     */
    private static boolean debug = false;

    /**
     * Instantiates a new Game logger.
     *
     * @throws IOException the io exception
     */
    private GameLogger() throws IOException {
        super("Sokoban", null);

        File directory = GameIO.getFile("./logs");
        directory.mkdirs();

        FileHandler fileHandler = new FileHandler(directory + "/" + GameStatus.getGameStatus().getGameName() + ".log"); // variable name changed
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    /**
     * Create logger.
     *
     * @throws IOException the io exception
     */
    public static void createLogger() throws IOException {
        if (gameLogger == null) {
            gameLogger = new GameLogger();
        }
    }

    /**
     * Create formatted message string.
     *
     * @param message the message
     * @return the string
     */
    private static String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    /**
     * Show info.
     *
     * @param message the message
     */
    public static void showInfo(String message) {
        logger.info(createFormattedMessage(message));
    }

    /**
     * Show warning.
     *
     * @param message the message
     */
    public static void showWarning(String message) {
        logger.warning(createFormattedMessage(message));
    }

    /**
     * Show severe.
     *
     * @param message the message
     */
    public static void showSevere(String message) {
        logger.severe(createFormattedMessage(message));
    }

    /**
     * Is debug active boolean.
     *
     * @return the boolean
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     * Toggle debug.
     */
    public static void toggleDebug() {
        debug = !debug;
    }

    /**
     * Print state.
     *
     * @param positionInfo the position info
     */
// method extracted
    public static void printState(PositionInfo positionInfo) {
        System.out.println("Current level state:");
        System.out.println(GameStatus.getGameStatus().getCurrentLevel().toString());
        System.out.println("Keeper pos: " + positionInfo.getKeeperPosition());
        System.out.println("Movement source obj: " + positionInfo.getKeeper());
        System.out.printf("Target object: %s at [%s]", positionInfo.getKeeperTarget(), positionInfo.getTargetObjectPoint());
    }
}