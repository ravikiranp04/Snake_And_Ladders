package org.example;

import java.util.Random;
import java.util.logging.Logger;

public class Dice {

    private static final Logger log = Logger.getLogger(Dice.class.getName());

    private Integer diceCount;
    private Random random = new Random();
    Dice(Integer diceCount){
        this.diceCount = diceCount;
    }

    public Integer getDiceCount() {
        return diceCount;
    }

    Integer rollDice(Integer remainingCells){
        Integer movableCells=0, turns=0;

        //Single dice (Single 6 gives another turn, 3 consecutive 6's loses turn and returns zero)
        if(diceCount==1){
            while(turns<3){
                Integer randNum = random.nextInt(6)+1;
                log.info("Rolled a "+randNum);

                // If the current roll crosses movableCells cells, then the player loses turn and moves the gained steps.
                if(randNum>remainingCells){
                    return movableCells;
                }


                movableCells+=randNum;
                remainingCells-=randNum;

                // If the current roll is not 6, then the player loses turn and moves the gained steps.
                if(randNum!=6) {
                    break;
                }
                turns++;
            }
            //If three consecutive 6's, then player looses turn along with gained steps.
            if(turns==3){
                log.info("Lost turn due to 3 consecutive 6's");
                return 0;
            }
        }
        //Multiple dice -> Adds up the movableCells sum and returns
        else{
            while(turns<diceCount){
                Integer randNum = random.nextInt(6)+1;
                movableCells+=randNum;
            }
        }
        return movableCells;
    }
}
