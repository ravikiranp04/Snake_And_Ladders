package org.example;

import java.util.Random;

public class Dice {


    private Integer diceCount;
    private Random random = new Random();
    Dice(Integer diceCount){
        this.diceCount = diceCount;
    }

    public Integer getDiceCount() {
        return diceCount;
    }

    Integer rollDice(){
        Integer total=0, turns=0;

        //Single dice (Single 6 gives another turn, 3 consecutive 6's loses turn and returns zero)
        if(diceCount==1){
            while(turns<3){
                Integer randNum = random.nextInt(6)+1;
                total+=randNum;
                if(randNum!=6) {
                    break;
                }
                turns++;
            }
            if(total==18){
                return 0;
            }
        }
        //Multiple dice -> Adds up the total sum and returns
        else{
            while(turns<diceCount){
                Integer randNum = random.nextInt(6)+1;
                total+=randNum;
            }
        }
        return total;
    }
}
