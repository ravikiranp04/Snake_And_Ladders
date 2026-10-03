package org.example;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GameFactory {
    private Map<String, Game> gameIdToGamesMap = new ConcurrentHashMap<>();

    public Game createGame(GameConfig gameConfig){
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId, gameConfig);
        gameIdToGamesMap.put(gameId,game);
        return game;
    }
}
