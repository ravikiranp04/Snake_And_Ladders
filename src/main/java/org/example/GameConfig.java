package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;

public class GameConfig {
    Map<Integer,Integer> snakesAndLaddersMap = new HashMap<>();
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
                log.info("Empty Board Dimensions");
                return;
            }
            //Input for Dice count
            if(fileScanner.hasNextInt()){
                this.diceCount= fileScanner.nextInt();
            }
            else{
                log.info("Empty Dice Count");
                return;
            }

            //Checking Snakes Data
            Integer snakesCount=0;
            if(fileScanner.hasNextInt()){
                snakesCount= fileScanner.nextInt();
            }
            Integer currSnakesCount =0;
            while(currSnakesCount<snakesCount && fileScanner.hasNextInt()){
                int head = fileScanner.nextInt();
                int tail = fileScanner.nextInt();
                if(head<=tail){
                    log.info("Unsupported Snakes coordinates: "+head +" <= "+tail);
                    return;
                }
                currSnakesCount++;
                this.snakesAndLaddersMap.put(head,tail);
            }

            //Checking Ladders Data
            Integer laddersCount =0;
            if(fileScanner.hasNextInt()){
                laddersCount= fileScanner.nextInt();
            }
            Integer currLaddersCount =0;
            while(currLaddersCount<laddersCount && fileScanner.hasNextInt()){
                Integer bottom = fileScanner.nextInt();
                Integer top = fileScanner.nextInt();
                if(top<=bottom){
                    log.info("Unsupported Ladder coordinates: "+top +" <= "+bottom);
                    return;
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
                log.info("Empty Players Count");
                return;
            }
            fileScanner.nextLine();
            //Player Names Input
            Integer currPlayers=0;

            //Player names input
            while(currPlayers<playersCount && fileScanner.hasNextLine()){
                this.playerNames.add(fileScanner.nextLine());
                currPlayers++;
            }

            if(currPlayers<playersCount){
                log.info("Insufficient Players Data");
                return;
            }
        } catch (FileNotFoundException e){
            log.info("File Not found");
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


