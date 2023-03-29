package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Five tiles of the same type forming a
 * diagonal.
 * @author Niccolò Giuliani
 */
public class Diagonal implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        boolean flag = false;
        int i = 0;
        for (int k = 0; k < 2 && flag == false; k++) {
            i = k;
            if(shelf[k][0] != Token.NOTHING) {
                for (int j = 0; j < COLUMNS && flag == false; j++,i++) {
                    if (shelf[k][0] != shelf[i][j]) {
                        flag = true;

                    }
                }
            }
        }
        if(flag) {
            flag = false;
            for (int k = 0; k < 2 && flag == false; k++) {
                i = k;
                if (shelf[k][4] != Token.NOTHING) {
                    for (int j = COLUMNS-1; j >= 0 && flag == false; j--) {
                        if (shelf[k][4] != shelf[i][j])
                            flag = true;
                        i++;
                    }
                }
            }
        }
        if(flag == true)
            return false;
        else
            return true;
    }

    public CommonType getName(){
        return CommonType.DIAGONAL;
    }
}
