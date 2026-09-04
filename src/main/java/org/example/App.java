package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {
    private static final Logger log = Logger.getLogger(App.class.getName());
    private static final GameFactory gameFactory = new GameFactory();
    private static Map<Integer, Game> activeGames= new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        String srcInputFolder = "src/main/java/org/example/";
        String[] inputFiles = {"input1.txt","input2.txt","input3.txt","input4.txt","input5.txt"};
        Integer gameCount=20, fileIdx=0, inputFilesCount=5;

        Integer dynamicPlayersCount = 3, dynamicPlayerIdx = -1;
        String[] dynamicPlayerList = {"Ramesh", "Suresh", "Vamsi"};

        List<Player> playerNamesInBetweenSamples= new ArrayList<>();
        for(String name: dynamicPlayerList){
            playerNamesInBetweenSamples.add(new Player(name,1));
        }

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

        Thread.sleep(20000);

        for(int i=1;i<=gameCount;i++){
            Integer gameNumber = i;
            dynamicPlayerIdx=(dynamicPlayerIdx+1)%dynamicPlayersCount;
            Player dynamicPlayer = playerNamesInBetweenSamples.get(dynamicPlayerIdx);
            Game game = activeGames.get(gameNumber);

            futures.add(executor.submit(()->addDynamicPlayer(dynamicPlayer,game)));
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
            Game game = gameFactory.createGame(gameConfig);
            activeGames.put(gameNumber,game);

            log.info("Game with game Id: "+ game.getGameId()+ " simulating.");
            started.incrementAndGet();
            game.play();

            gameFactory.endGame(game.getGameId());
            activeGames.remove(gameNumber);
            finished.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warning("Game-" + gameNumber + " interrupted.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Game-" + gameNumber + " simulation failed",e);
        }
    }

    public static  void addDynamicPlayer (Player dynamicPlayer, Game game){
        if(game==null){
            log.info("Game: "+game.getGameId()+" Finished");
            return;
        }
        game.addPlayer(dynamicPlayer);
        log.info("Added "+dynamicPlayer.getName()+" into gameId: "+ game.getGameId());

    }
}
