package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    private static boolean configured = false;

    public static void configure() {

        if (configured) {
            return;
        }

        try {
            Path logDirectory = Paths.get("logs");
            Files.createDirectories(logDirectory);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            String logFile = "logs/app-" + timestamp + ".log";

            Logger rootLogger = Logger.getLogger("");

            FileHandler fileHandler =
                    new FileHandler(logFile, false);

            fileHandler.setFormatter(new SimpleFormatter());

            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO);

            configured = true;

        } catch (IOException e) {
            System.err.println("Failed to configure logging: "
                    + e.getMessage());
        }
    }
}