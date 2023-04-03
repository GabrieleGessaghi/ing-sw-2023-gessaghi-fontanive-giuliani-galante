package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Five columns of increasing or decreasing
 * height. Starting from the first column on
 * the left or on the right, each next column
 * must be made of exactly one more tile.
 * Tiles can be of any type
 * @author Niccolò Giuliani
 */
public class Stairs implements CommonObjective {

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
            if(counter[i] < counter[i]+1)
                if(counter[i] <= counter[i]+1)
                    flagRight = true;
        for(int i=0; i < ROWS-1; i++)
            if(counter[i] > counter[i]+1)
                if(counter[i] >= counter[i]+1)
                    flagLeft = true;

        if( !flagRight || !flagLeft )
            return true;
        else
            return false;
    }
    public CommonType getName(){
        return CommonType.STAIRS;
    }
}