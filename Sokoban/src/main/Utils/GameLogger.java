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
 * GameLogger is a singleton class for logging the game running information
 */
public final class GameLogger extends Logger {

    /**
     * all access to this class object must be through this single reference
     */
    private static GameLogger gameLogger;
    /**
     * The logger.
     */
    private static final Logger logger = Logger.getLogger("GameLogger");
    /**
     * The dateFormat.
     */
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    /**
     * The calendar.
     */
    private static final Calendar calendar = Calendar.getInstance();
    /**
     * if debug mode is activated
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
     * Show information message
     *
     * @param message the message
     */
    public static void showInfo(String message) {
        logger.info(createFormattedMessage(message));
    }

    /**
     * Show warning message
     *
     * @param message the message
     */
    public static void showWarning(String message) {
        logger.warning(createFormattedMessage(message));
    }

    /**
     * Show severe message
     *
     * @param message the message
     */
    public static void showSevere(String message) {
        logger.severe(createFormattedMessage(message));
    }

    /**
     * Is debug activated
     *
     * @return the boolean
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     * Toggle debug mode
     */
    public static void toggleDebug() {
        debug = !debug;
    }

    /**
     * Print state of currently played level
     *
     * @param positionInfo the position info
     */
    public static void printState(PositionInfo positionInfo) {
        System.out.println("Current level state:");
        System.out.println(GameStatus.getGameStatus().getCurrentLevel().toString());
        System.out.println("Keeper pos: " + positionInfo.getKeeperPosition());
        System.out.println("Movement source obj: " + positionInfo.getKeeper());
        System.out.printf("Target object: %s at [%s]", positionInfo.getKeeperTarget(), positionInfo.getTargetObjectPoint());
    }
}