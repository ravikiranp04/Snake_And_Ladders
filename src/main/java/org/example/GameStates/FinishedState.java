package org.example.GameStates;

import org.example.Game;
import org.example.Player;

public class FinishedState implements GameState{
    public final Game game;

    public FinishedState(Game game){
        this.game=game;
    }
    @Override
    public void addPlayer(Player player) {
        game.getLogger().info("Game Finished. Hence Player "+player.getName()+" cannot be added");
        return;
    }

    @Override
    public void addSnake(Integer fromCell, Integer toCell) {
        game.getLogger().info("Game Finished. Hence Snake cannot be added at cell "+fromCell);
        return;
    }

    @Override
    public void addLadder(Integer fromCell, Integer toCell) {
        game.getLogger().info("Game Finished. Hence Ladder cannot be added at cell "+fromCell);
        return;
    }

    @Override
    public void play() {
        game.getLogger().info("Game already Finished.");
        return;
    }
}
