package org.example.GameStates;

import org.example.Player;

public interface GameState {

    void addPlayer(Player player);
    void addSnake(Integer fromCell, Integer toCell);
    void addLadder(Integer fromCell, Integer toCell);
    void play() throws InterruptedException;


}
