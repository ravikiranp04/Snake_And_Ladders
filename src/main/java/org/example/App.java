package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {
    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws InterruptedException {
        String srcInputFolder = "src/main/java/org/example/";
        String[] inputFiles = {"input1.txt","input2.txt","input3.txt","input4.txt","input5.txt"};
        Integer gameCount=150, fileIdx=0, inputFilesCount=5;

        AtomicInteger started = new AtomicInteger(0);
        AtomicInteger finished = new AtomicInteger(0);


        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futures = new ArrayList<>();
        for(int i=1;i<=gameCount;i++){
            String inputFile = srcInputFolder+inputFiles[fileIdx];
            fileIdx=(fileIdx+1)%inputFilesCount;
            Integer gameNumber = i;
            futures.add(executor.submit(()-> simulateGame(inputFile,gameNumber,started,finished)));
        }

        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                log.warning("A Game simulation thread failed: " + e.getCause());
            }
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        log.info("=========================================");
        log.info("Simulation complete. Games Started: " + started.get() + ", finished: " + finished.get());

    }

    public static void simulateGame(String inputFile, Integer gameNumber, AtomicInteger started, AtomicInteger finished){
        try{
            GameConfig gameConfig = new GameConfig(inputFile);
            Game game = new Game(gameConfig);
            started.incrementAndGet();
            game.play();
            finished.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Game-" + gameNumber + " interrupted.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Game-" + gameNumber + " simulation failed",e);
        }
    }
}
