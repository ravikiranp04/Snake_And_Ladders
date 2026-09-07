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
        Integer gameCount=1, fileIdx=0, inputFilesCount=5;

        Integer dynamicPlayersCount = 3, dynamicPlayerIdx = -1;
        String[] dynamicPlayerList = {"Ramesh", "Suresh", "Vamsi"};

        Integer[][] snakesToBeAdded = {{50,2},{10,5},{33,7},{3,4}};
        Integer[][] laddersToBeAdded = {{35,73},{65,67},{93,95},{25,16}};


        AtomicInteger started = new AtomicInteger(0);
        AtomicInteger finished = new AtomicInteger(0);


        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<?>> futures = new ArrayList<>();

        //Running multiple games
        for(int i=1;i<=gameCount;i++){
            String inputFile = srcInputFolder+inputFiles[fileIdx];
            fileIdx=(fileIdx+1)%inputFilesCount;
            Integer gameNumber = i;

            futures.add(executor.submit(()-> simulateGame(inputFile,gameNumber,started,finished)));
        }

        Thread.sleep(20000);

        //Simulating dynamic player entry into the games
        for(int i=1;i<=gameCount;i++){
            Integer gameNumber = i;
            dynamicPlayerIdx=(dynamicPlayerIdx+1)%dynamicPlayersCount;
            Player dynamicPlayer = new Player(dynamicPlayerList[dynamicPlayerIdx],1);
            Game game = activeGames.get(gameNumber);

            futures.add(executor.submit(()->addDynamicPlayer(dynamicPlayer,game)));
        }

        Thread.sleep(10000);

        //Simulating dynamic snake or ladders added into the game
        for(int i=1;i<=gameCount;i++){
            Integer gameNumber = i;
            Game game = activeGames.get(gameNumber);
            for(int j=0;j<4;j++){
                Integer snakeStart = snakesToBeAdded[j][0], snakeEnd = snakesToBeAdded[j][1];
                Integer ladderStart = laddersToBeAdded[j][0], ladderEnd = laddersToBeAdded[j][1];
                futures.add(executor.submit(()->addDynamicSnake(snakeStart,snakeEnd,game)));
                futures.add(executor.submit(()->addDynamicLadder(ladderStart,ladderEnd,game)));
                Thread.sleep(1000);
            }
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
            // Active Games map is used to  track current running game with game Number temporarily.
            activeGames.put(gameNumber,game);

            log.info("Game with game Id: "+ game.getGameId()+ " simulating.");
            started.incrementAndGet();
            game.play();


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

        game.addPlayer(dynamicPlayer);
    }

    public static void addDynamicSnake(Integer fromCell, Integer toCell, Game game){
        game.addSnake(fromCell,toCell);
    }

    public static void addDynamicLadder(Integer fromCell, Integer toCell, Game game){
        game.addLadder(fromCell,toCell);
    }
}
