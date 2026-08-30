package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;


public class App {
    public static void main(String[] args) {
        LoggerConfig.configure();
        Logger log = Logger.getLogger(App.class.getName());
        Map<Integer,Integer> snakesAndLaddersMap = new HashMap<>();
        String inputFileName = "src/main/java/org/example/input.txt";
        File inputFile = new File(inputFileName);
        try(Scanner fileScanner = new Scanner(inputFile)){
            Integer diceCount , boardDimensions;
            // Input for board Dimension
            if(fileScanner.hasNextInt()){
                boardDimensions= fileScanner.nextInt();
            }
            else{
                log.info("Empty Board Dimensions");
                return;
            }
            //Input for Dice count
            if(fileScanner.hasNextInt()){
                diceCount= fileScanner.nextInt();
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
                snakesAndLaddersMap.put(head,tail);
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
                snakesAndLaddersMap.put(bottom,top);
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
            List<String> playerNames = new ArrayList<>();
            Integer currPlayers=0;

            //Player names input
            while(currPlayers<playersCount && fileScanner.hasNextLine()){
                playerNames.add(fileScanner.nextLine());
                currPlayers++;
            }

            if(currPlayers<playersCount){
                log.info("Insufficient Players Data");
                return;
            }

            //Playing on a board
            Game game1 = new Game(diceCount,boardDimensions, snakesAndLaddersMap, playerNames);
            game1.play();


            Game game2 = new Game(diceCount,boardDimensions, snakesAndLaddersMap, playerNames);
            game2.play();
            log.info("Game Finished");

        } catch (FileNotFoundException e){
            log.info("File Not found");
        }
    }
}
