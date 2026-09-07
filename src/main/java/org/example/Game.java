package org.example;

import org.example.GameStates.FinishedState;
import org.example.GameStates.GameState;
import org.example.GameStates.RunningState;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class Game {
    private GameState state;

    private final GameState runningState;
    private final GameState finishedState;

    private String gameId;
    private Integer totalCells;
    private Dice dice;
    private SnakesAndLaddersData snakesAndLaddersData;
    private Queue<Player> playersQueue;

    public Logger getLogger() {
        return log;
    }

    private final Logger log;

    Game(String gameId, GameConfig gameConfig) {
        Integer diceCount = gameConfig.getDiceCount();
        Integer boardDimensions = gameConfig.getBoardDimensions();
        this.dice = new Dice(diceCount);
        this.snakesAndLaddersData = new SnakesAndLaddersData(gameConfig.getSnakesAndLaddersMap());
        this.totalCells = boardDimensions * boardDimensions;
        this.gameId = gameId;
        this.log = LoggerConfig.configure(this.gameId);
        this.playersQueue = new ConcurrentLinkedQueue<>();
        for (String name : gameConfig.getPlayerNames()) {
            playersQueue.offer(new Player(name, 1));
        }


        this.runningState = new RunningState(this);
        this.finishedState = new FinishedState(this);
        this.state = runningState;
    }

    public GameState getRunningState() {
        return runningState;
    }

    public GameState getFinishedState() {
        return finishedState;
    }

    public void setGameState(GameState gameState) {
        this.state = gameState;
    }

    public GameState getGameState() {
        return state;
    }

    public void addPlayer(Player player) {
        state.addPlayer(player);
    }

    public void addSnake(Integer fromCell, Integer toCell) {
        state.addSnake(fromCell, toCell);
    }

    public void addLadder(Integer fromCell, Integer toCell) {
        state.addLadder(fromCell, toCell);
    }

    public void play() throws InterruptedException {
        state.play();
    }

    public void addPlayerToGame(Player player) {
        playersQueue.offer(player);
        log.info("Added " + player.getName() + " into gameId: " + gameId);
    }

    public void addSnakeToGame(Integer fromCell, Integer toCell) {
        if (fromCell < toCell) {
            log.info("Invalid Snake Coordinates");
            return;
        }
        Integer presentCell = snakesAndLaddersData.getSnakesAndLaddersMap().putIfAbsent(fromCell, toCell);

        if (presentCell == null) {
            log.info("Snake added at cell " + fromCell);
            return;
        }

        if (fromCell < presentCell) {
            log.info("Ladder already exists at the cell " + fromCell + ", Snake cannot be added");
        } else {
            log.info("Snake already exists at the cell " + fromCell + ", Hence New Snake cannot be added");
        }

    }

    public void addLadderToGame(Integer fromCell, Integer toCell) {

        if (fromCell > toCell) {
            log.info("Invalid Ladder Coordinates");
            return;
        }
        Integer presentCell = snakesAndLaddersData.getSnakesAndLaddersMap().putIfAbsent(fromCell, toCell);
        if (presentCell == null) {
            log.info("Ladder added at cell " + fromCell);
            return;
        }

        if (fromCell < presentCell) {
            log.info("Ladder already exists at the cell " + fromCell + ", Ladder cannot be added");
        } else {
            log.info("Snake already exists at the cell " + fromCell + ", Hence New Ladder cannot be added");
        }
    }

    public void playGame() throws InterruptedException {
        while (!playersQueue.isEmpty()) {
            Player currentPlayer = playersQueue.poll();
            Integer currCell = currentPlayer.getCurrentCell();
            log.info("-------------------------------------------------------");

            log.info("Rolling Dice: " + currentPlayer.getName() + " (Current Position: " + currCell + " ).");
            //rolling dice
            Integer remainingCells = totalCells - currCell;
            Integer newMovement = dice.rollDice(remainingCells, this);

            //computing new cell
            Integer newCell = currCell + newMovement;

            //Player wins game
            if (checkPlayerWins(newCell)) {
                log.info(currentPlayer.getName() + " rolled a " + newMovement + " and wins the game");
                currentPlayer.setWinStatus(true);
                break;
            }

            log.info(currentPlayer.getName() + " rolled a " + newMovement + " and moved from " + currCell + " to " + newCell);


            // If next cell greater than total cells or player looses turn due to 3 consecutive 6's
            // Roll dice returns zero, if three consecutive 6's occured
            if (newCell.equals(currCell)) {
                playersQueue.offer(currentPlayer);
                continue;
            }

            newCell = teleportWithSnakesAndLadders(newCell, currentPlayer);

            currentPlayer.moveToCell(newCell);
            playersQueue.offer(currentPlayer);


            Thread.sleep(2000);

        }
        setGameState(finishedState);
    }

    boolean checkPlayerWins(Integer newCell) {
        return newCell.equals(totalCells);
    }

    Integer teleportWithSnakesAndLadders(Integer newCell, Player currentPlayer) {

        Integer snakeOrLadderCell = snakesAndLaddersData.checkSnakeOrLadder(newCell);
        while (snakeOrLadderCell != -1) {
            // If lower than current cell, then it's a snake, else it's a ladder
            if (snakeOrLadderCell < newCell) {
                log.info("Snake: " + currentPlayer.getName() + " dropped to cell " + snakeOrLadderCell);
            } else {
                log.info("Ladder: " + currentPlayer.getName() + " jumped to cell " + snakeOrLadderCell);
            }
            newCell = snakeOrLadderCell;
            snakeOrLadderCell = snakesAndLaddersData.checkSnakeOrLadder(newCell);
        }
        return newCell;

    }

    public String getGameId() {
        return gameId;
    }

}

