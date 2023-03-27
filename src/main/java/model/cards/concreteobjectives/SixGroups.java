package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Six groups each containing at least
 * 2 tiles of the same type (not necessarily
 * in the depicted shape).
 * The tiles of one group can be different
 * from those of another group.
 * @author Niccolò Giuliani
 */
public class SixGroups implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int counter = 0;
        boolean flag = false;
        boolean [][] check;
        check = new boolean[ROWS][COLUMNS];
        for(int i = 0; i < ROWS ; i++)
            for(int j = 0; j< COLUMNS ; j++)
                check[i][j] = false;
        for (int i = 0; i < ROWS - 1 && counter < 6; i++){
            for (int j = 0; j < COLUMNS - 1 ; j++){
                flag = false;
                if(shelf[i][j] != Token.NOTHING && check[i][j] == false) {
                    if (shelf[i][j] == shelf[i][j + 1]) {
                        counter++;
                        flag = true;
                        check[i][j +1] = true;
                    }
                    if (shelf[i][j] == shelf[i + 1][j] && flag == false)
                        counter++;
                        check[i + 1][j] = true;
                }
            }
        }
        if(counter >= 4)
            return true;
        else
            return false;
    }

    public CommonType getName(){
        return CommonType.SIXGROUPS;
    }
}
