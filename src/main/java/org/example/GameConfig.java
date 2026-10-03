package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GameConfig  {
    Map<Integer,Integer> snakesAndLaddersMap = new ConcurrentHashMap<>();
    List<String> playerNames = new ArrayList<>();
    Integer diceCount;
    Integer boardDimensions;
    Logger log = Logger.getLogger(GameConfig.class.getName());

    GameConfig(String inputFileName){
        File inputFile = new File(inputFileName);
        try(Scanner fileScanner = new Scanner(inputFile)){

            // Input for board Dimension
            if(fileScanner.hasNextInt()){
                this.boardDimensions= fileScanner.nextInt();
            }
            else{
                throw new InputMismatchException("Empty Board Dimensions");
            }
            //Input for Dice count
            if(fileScanner.hasNextInt()){
                this.diceCount= fileScanner.nextInt();
            }
            else{
                throw new InputMismatchException("Empty Dice Count");
            }

            if(diceCount!=1){
                throw new InputMismatchException("Invalid Dice Count");
            }

            //Input Snakes Count
            Integer snakesCount=0;
            if(fileScanner.hasNextInt()){
                snakesCount= fileScanner.nextInt();
            }

            //Input Snakes Coordinates
            Integer currSnakesCount =0;
            while(currSnakesCount<snakesCount && fileScanner.hasNextInt()){
                int head = fileScanner.nextInt();
                int tail = fileScanner.nextInt();
                if(head<=tail){
                    throw new InputMismatchException("Unsupported Snakes coordinates: "+head +" <= "+tail);
                }
                currSnakesCount++;
                this.snakesAndLaddersMap.put(head,tail);
            }

            //Input Ladders Count
            Integer laddersCount =0;
            if(fileScanner.hasNextInt()){
                laddersCount= fileScanner.nextInt();
            }

            // Input Ladders Coordinates
            Integer currLaddersCount =0;
            while(currLaddersCount<laddersCount && fileScanner.hasNextInt()){
                Integer bottom = fileScanner.nextInt();
                Integer top = fileScanner.nextInt();
                if(top<=bottom){
                    throw new InputMismatchException("Unsupported Ladder coordinates: "+top +" <= "+bottom);
                }
                currLaddersCount++;
                this.snakesAndLaddersMap.put(bottom,top);
            }

            // Players Count Input
            Integer playersCount=0;
            if(fileScanner.hasNextInt()){
                playersCount= fileScanner.nextInt();
            }
            else{
                throw new InputMismatchException("Empty Players Count");
            }
            fileScanner.nextLine();

            //Player Names Input
            Integer currPlayers=0;
            while(currPlayers<playersCount && fileScanner.hasNextLine()){
                this.playerNames.add(fileScanner.nextLine());
                currPlayers++;
            }

            if(currPlayers<playersCount){
                throw new InputMismatchException("Insufficient Players Data");
            }

        } catch (FileNotFoundException | InputMismatchException e){
            throw new RuntimeException(e);
        }
    }

    public Integer getBoardDimensions() {
        return boardDimensions;
    }

    public Integer getDiceCount() {
        return diceCount;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public Map<Integer, Integer> getSnakesAndLaddersMap() {
        return snakesAndLaddersMap;
    }
}


