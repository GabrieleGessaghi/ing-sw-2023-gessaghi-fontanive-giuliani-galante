package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Five tiles of the same type forming a
 * diagonal.
 * @author Niccolò Giuliani
 */
public class Stairs implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int []counter;
        counter = new int[ROWS];
        boolean flagRight = false;
        boolean flagLeft = false;
        for(int i = 0; i< ROWS; i++)
            counter[i] = 0;
        for (int i = 0; i < ROWS ; i++){
            for (int j = 0; j < COLUMNS ; j++) {
                if (shelf[i][j] != Token.NOTHING) {
                    counter[i]++;
                }
            }
        }
        for (int i=0; i < ROWS-1; i++)
            if(counter[i] <= counter[i]+1)
                 flagRight = true;
         for(int i=0; i < ROWS-1; i++)
             if(counter[i] >= counter[i]+1)
                 flagLeft = true;

        if(flagRight == false || flagLeft == false )
            return true;
        else
            return false;
    }

    public CommonType getName(){
        return CommonType.STAIRS;
    }
}
