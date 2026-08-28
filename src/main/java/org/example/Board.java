package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

public class Board {

    private static final Logger log = Logger.getLogger(Board.class.getName());

    private Integer totalCells;
    private Dice dice;
    private SnakesAndLaddersData snakesAndLaddersData;
    private Queue<Player> playersQueue;
    Board(Integer diceCount, Integer boardDimensions, Map<Integer,Integer> snakesAndLaddersMap, List<String> playerNames){
        this.dice=new Dice(diceCount);
        this.snakesAndLaddersData  = new SnakesAndLaddersData(snakesAndLaddersMap);
        this.totalCells = boardDimensions*boardDimensions;

        this.playersQueue  = new LinkedList<>();
        for(String name: playerNames){
            playersQueue.offer(new Player(name,0));
        }
    }
    void play(){
        while(!playersQueue.isEmpty()){
            Player currentPlayer = playersQueue.poll();
            Integer currCell = currentPlayer.getCurrentCell();
            log.info("-------------------------------------------------------");

            log.info("Rolling Dice: "+currentPlayer.getName());
            //rolling dice
            Integer remainingCells = totalCells-currCell;
            Integer newMovement = dice.rollDice(remainingCells);

            //computing new cell
            Integer newCell = currCell+ newMovement;

            //Player wins game
            if(newCell==totalCells){
                log.info(currentPlayer.getName()+" wins the game");
                currentPlayer.moveToCell(newCell);
                currentPlayer.setWinStatus(true);
                continue;
            }
            // If next cell greater than total cells or player looses turn due to 3 consecutive 6's
            // Roll dice returns zero, if three consecutive 6's occured
            if(newCell==currCell || newCell>totalCells){
                log.info(currentPlayer.getName()+" rolled a "+ newMovement+" and moved from "+ currCell+" to "+currCell);
                playersQueue.offer(currentPlayer);
                continue;
            }

            log.info(currentPlayer.getName()+" rolled a "+ newMovement+" and moved from "+ currCell+" to "+newCell);

            //Checking for Snakes and Ladders teleporting (If -1, then no snakes or ladders exist at that cell)
            Integer snakeOrLadderCell = snakesAndLaddersData.checkSnakeOrLadder(newCell);
            while(snakeOrLadderCell!=-1){
                // If lower than current cell, then it's a snake, else it's a ladder
                if(snakeOrLadderCell<newCell){
                    log.info("Snake: " +currentPlayer.getName()+" dropped to cell "+snakeOrLadderCell);
                }
                else{
                    log.info("Ladder: " +currentPlayer.getName()+" jumped to cell "+snakeOrLadderCell);
                }
                newCell=snakeOrLadderCell;
                snakeOrLadderCell = snakesAndLaddersData.checkSnakeOrLadder(newCell);
            }

            currentPlayer.moveToCell(newCell);
            playersQueue.offer(currentPlayer);
        }
    }
}
