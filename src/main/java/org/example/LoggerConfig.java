package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {


    public static Logger configure(String gameId) {

        try {

            String date = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Path logDirectory = Paths.get("logs",date);
            Files.createDirectories(logDirectory);

            Path logPath = logDirectory.resolve(
                    date + "_" + gameId + ".log"
            );

            Logger logger = Logger.getLogger("Game-" + gameId);

            logger.setUseParentHandlers(false);
            logger.setLevel(Level.INFO);

            FileHandler fileHandler =
                    new FileHandler(logPath.toString(), false);

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            return logger;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to configure logging for Game-" + gameId,
                    e
            );
        }

    }
}