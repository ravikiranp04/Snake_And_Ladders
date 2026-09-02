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

    private static FileHandler fileHandler;

    public static void configure(String gameId) {

        try {
            Path logDirectory = Paths.get("logs/sept1/");
            Files.createDirectories(logDirectory);

            String date = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String logFile = "logs" + date + "_" + gameId + ".log";

            Logger rootLogger = Logger.getLogger("");

            if (fileHandler != null) {
                rootLogger.removeHandler(fileHandler);
                fileHandler.close();
            }

            fileHandler = new FileHandler(logFile, false);
            fileHandler.setFormatter(new SimpleFormatter());

            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO);

        } catch (IOException e) {
            System.err.println(
                    "Failed to configure logging: " + e.getMessage()
            );
        }
    }
}