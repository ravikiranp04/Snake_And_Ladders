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

    Integer checkSnakeOrLadder(Integer currCell){
        if(snakesAndLaddersMap.containsKey(currCell)){
            return snakesAndLaddersMap.get(currCell);
        }
        return -1;
    }
}
