package org.example.GameStates;

import org.example.Game;
import org.example.Player;

public class RunningState implements GameState{

    public final Game game;

    public RunningState(Game game){
        this.game=game;
    }
    @Override
    public void addPlayer(Player player) {
        game.addPlayerToGame(player);
    }

    @Override
    public void addSnake(Integer fromCell, Integer toCell) {
        game.addSnakeToGame(fromCell,toCell);
    }

    @Override
    public void addLadder(Integer fromCell, Integer toCell) {
        game.addLadderToGame(fromCell,toCell);
    }

    @Override
    public void play() throws InterruptedException {
        game.playGame();
    }
}
