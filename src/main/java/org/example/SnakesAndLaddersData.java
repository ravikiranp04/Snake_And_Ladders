package org.example;

import java.util.Map;

public class SnakesAndLaddersData {

    private Map<Integer,Integer> snakesAndLaddersMap;
    SnakesAndLaddersData(Map<Integer,Integer> snakesAndLaddersMap){
        this.snakesAndLaddersMap=snakesAndLaddersMap;
    }

    public Map<Integer, Integer> getSnakesAndLaddersMap() {
        return snakesAndLaddersMap;
    }

    // Checks weather it is a snake or ladder and returns the teleported new cell. Else, returns -1.
    Integer checkSnakeOrLadder(Integer currCell){
        if(snakesAndLaddersMap.containsKey(currCell)){
            return snakesAndLaddersMap.get(currCell);
        }
        return -1;
    }
}
