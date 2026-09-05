package org.example;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GameFactory {
    private Map<String, Game> gameIdToGamesMap = new ConcurrentHashMap<>();
    Logger log = Logger.getLogger(GameFactory.class.getName());

    public Game createGame(GameConfig gameConfig){
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId, gameConfig);
        gameIdToGamesMap.put(gameId,game);
        return game;
    }

    public Game getGame(String gameId){
        return gameIdToGamesMap.get(gameId);
    }

    public void addPlayer(String gameId, Player player){
        Game game = gameIdToGamesMap.get(gameId);
        if(game == null){
            log.info("Game doesn't exist");
            return;
        }
        game.addPlayer(player);
    }

    public void endGame(String gameId){
        gameIdToGamesMap.remove(gameId);
    }
}
