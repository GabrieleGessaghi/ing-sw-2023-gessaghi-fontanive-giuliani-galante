package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Five tiles of the same type forming an X.
 * @author Niccolò Giuliani
 */
public class XShape implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        boolean flag = false;
        for (int i = 0; i < ROWS - 2 && !flag; i++)
            for (int j = 0; j < COLUMNS - 2&& !flag; j++)
                if(shelf[i][j] != Token.NOTHING && i < 4 && j < 3)
                    if(shelf[i][j] == shelf[i][j + 2] && shelf[i][j] == shelf[i + 1][j + 1] && shelf[i][j] == shelf[i + 2][j] &&
                            shelf[i][j] == shelf[i + 2][j + 2])
                        flag = true;
        return flag;
    }

    public CommonType getName(){
        return CommonType.XSHAPE;
    }
}
