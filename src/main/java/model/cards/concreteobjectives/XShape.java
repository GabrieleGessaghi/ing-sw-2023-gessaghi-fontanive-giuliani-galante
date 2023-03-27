package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * @author Niccolò Giuliani
 * Five tiles of the same type forming an X.
 */
public class XShape implements CommonObjective {
    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int flag = 0;
        for (int i = 0; i < ROWS && flag == 0; i++){
            for (int j = 0; j < COLUMNS && flag == 0; j++){
                if(shelf[i][j] != Token.NOTHING && i < 4 && j < 3){
                    if(shelf[i][j] == shelf[i][j + 2] && shelf[i][j] == shelf[i + 1][j + 1] && shelf[i][j] == shelf[i + 2][j]&&
                    shelf[i][j] == shelf[i + 2][j + 2])
                        flag=1;
                }
            }
        }
        if(flag == 1)
            return true;
        else
            return false;
    }

    public CommonType getName(){
        return CommonType.XSHAPE;
    }
}
