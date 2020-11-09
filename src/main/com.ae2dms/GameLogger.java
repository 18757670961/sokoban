package main.com.ae2dms;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger extends Logger {

    private static final Logger logger = Logger.getLogger("GameLogger");
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Calendar calendar = Calendar.getInstance();

    public GameLogger() throws IOException {
        super("com.aes2dms.sokoban", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + GameEngine.GAME_NAME + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    @Override
    public void info(String message) {
        logger.info(createFormattedMessage(message));
    }

    @Override
    public void warning(String message) {
        logger.warning(createFormattedMessage(message));
    }

    @Override
    public void severe(String message) {
        logger.severe(createFormattedMessage(message));
    }
}