package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class Game {


    private String gameId;
    private Integer totalCells;
    private Dice dice;
    private SnakesAndLaddersData snakesAndLaddersData;
    private Queue<Player> playersQueue;
    private final Logger log;
    GameStatus gameStatus;

    Game(String gameId, GameConfig gameConfig){
        Integer diceCount = gameConfig.getDiceCount();
        Integer boardDimensions = gameConfig.getBoardDimensions();
        this.dice= new Dice(diceCount);
        this.snakesAndLaddersData  = new SnakesAndLaddersData(gameConfig.getSnakesAndLaddersMap());
        this.totalCells = boardDimensions*boardDimensions;
        this.gameId = gameId;
        this.log=LoggerConfig.configure(this.gameId);
        this.playersQueue  = new ConcurrentLinkedQueue<>();
        for(String name: gameConfig.getPlayerNames()){
            playersQueue.offer(new Player(name,1));
        }

        this.gameStatus = GameStatus.STARTING;
    }

    public String getGameId() {
        return gameId;
    }

    public void addPlayer(Player player){
        playersQueue.offer(player);
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    void play() throws InterruptedException{
        setGameStatus(GameStatus.RUNNING);
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

            log.info(currentPlayer.getName()+" rolled a "+ newMovement+" and moved from "+ currCell+" to "+newCell);


            // If next cell greater than total cells or player looses turn due to 3 consecutive 6's
            // Roll dice returns zero, if three consecutive 6's occured
            if(newCell.equals(currCell)){
//                log.info(currentPlayer.getName()+" rolled a "+ newMovement+" and moved from "+ currCell+" to "+currCell);
                playersQueue.offer(currentPlayer);
                continue;
            }

//            log.info(currentPlayer.getName()+" rolled a "+ newMovement+" and moved from "+ currCell+" to "+newCell);

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

            //Player wins game
            if(newCell.equals(totalCells)){
                log.info(currentPlayer.getName()+" wins the game");
                currentPlayer.setWinStatus(true);
                break;
            }
            playersQueue.offer(currentPlayer);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
        setGameStatus(GameStatus.FINISHED);
    }
}
