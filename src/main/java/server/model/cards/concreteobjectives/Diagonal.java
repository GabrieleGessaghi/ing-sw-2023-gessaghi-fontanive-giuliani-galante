package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;

/**
 * Five tiles of the same type forming a diagonal.
 * @author Niccolò Giuliani
 */
public class Diagonal implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int counter = 0;
        boolean flag = false;
        int i = 0;
        for (int k = 0; k < 2; k++) {
            flag = false;
            i = k;
            for (int j = 0; j < COLUMNS ; j++,i++) {
                if (shelf[k][0] != shelf[i][j])
                    flag = true;
            }
            if(shelf[k][0] == Token.NOTHING || shelf[k][0] == null) {
                flag = true;
            }
            if(flag)
              counter++;
        }

        for (int k = 0; k < 2; k++) {
            flag = false;
            i = k;
            for (int j = COLUMNS - 1; j >= 0 ; j--,i++) {
                if (shelf[k][4] != shelf[i][j] || shelf[k][4] == null)
                    flag = true;
            }
            if(shelf[k][4] == Token.NOTHING)
                flag = true;
            if(flag)
                counter++;
        }
        return counter < 4;
    }

    @Override
    public CommonType getName(){
        return CommonType.DIAGONAL;
    }

    @Override
    public String getDescription() {
        return "Five tiles of the same type forming a diagonal.";
    }
}
