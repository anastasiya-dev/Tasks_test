package by.it.academy.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggerUtil {

    public static Logger startLogging(String className) throws IOException {
        // Create a Logger
        Logger logger = Logger.getLogger(className);

        // Configure logger location
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String dateFormatted = dtf.format(now);

        File path = new File("C:\\work\\mining_logs\\");
        if (!path.exists()) {
            path.mkdir();
        }

        File logFile = new File(path, dateFormatted + "_" + className + ".txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        // Create a file handler object
        FileHandler handler = new FileHandler(logFile.getAbsolutePath());

        // Add file handler as handler of logs
        logger.addHandler(handler);

        // Set Logger level()
        logger.setLevel(Level.CONFIG);

        // Call config method
        logger.config("Set Geeks=CODING");
        return logger;
    }
}
