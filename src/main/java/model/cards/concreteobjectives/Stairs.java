package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * @author Niccolò Giuliani
 * Five tiles of the same type forming a
 * diagonal.
 */
public class Stairs implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int []counter;
        counter = new int[ROWS];
        int flagRight = 0;
        int flagLeft = 0;
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
            if(counter[i] < counter[i]+1)
                 flagRight=1;
         for(int i=0; i < ROWS-1; i++)
             if(counter[i] > counter[i]+1)
                 flagLeft=1;

        if(flagRight == 0 || flagLeft == 0 )
            return true;
        else
            return false;
    }

    public CommonType getName(){
        return CommonType.STAIRS;
    }
}
