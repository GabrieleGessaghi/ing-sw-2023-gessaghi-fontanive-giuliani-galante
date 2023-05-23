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
        boolean flag = false;
        int i = 0;
        for (int k = 0; k < 2 && !flag; k++) {
            i = k;
            if (shelf[k][0] != Token.NOTHING) {
                for (int j = 0; j < COLUMNS && !flag; j++,i++) {
                    if (shelf[k][0] == shelf[i][j]) {
                        flag = true;

                    }
                }
            }
        }
        if (!flag) {
            for (int k = 0; k < 2 && !flag; k++) {
                i = k;
                if (shelf[k][4] != Token.NOTHING) {
                    for (int j = COLUMNS - 1; j >= 0 && !flag; j--,i++) {
                        if (shelf[k][4] == shelf[i][j])
                            flag = true;
                    }
                }
            }
        }
        return flag;
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
