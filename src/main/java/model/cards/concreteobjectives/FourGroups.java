package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Four groups each containing at least
 * 4 tiles of the same type (not necessarily
 * in the depicted shape).
 * The tiles of one group can be different
 * from those of another group.
 * @author Niccolò Giuliani
 */
public class FourGroups implements CommonObjective {
    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int counter = 0;
        boolean flag;
        boolean [][] check;
        check = new boolean[ROWS][COLUMNS];
        for(int i = 0; i < ROWS ; i++)
            for(int j = 0; j < COLUMNS ; j++)
                check[i][j] = false;
        for (int i = 0; i < ROWS - 3 && counter < 6; i++){
            for (int j = 0; j < COLUMNS - 3 ; j++){
                flag=false;
                if(shelf[i][j] != Token.NOTHING && !check[i][j]) {
                    if (shelf[i][j] == shelf[i][j + 1] && shelf[i][j] == shelf[i][j + 2] && shelf[i][j] == shelf[i][j + 3] &&
                            !check[i][j + 1] && !check[i][j + 2] && !check[i][j + 3]){
                        flag=true;
                        counter++;
                        check[i][j] = true;
                        check[i][j + 1] = true;
                        check[i][j + 2] = true;
                        check[i][j + 3] = true;
                    }

                    if (shelf[i][j] == shelf[i + 1][j] && shelf[i][j] == shelf[i + 2][j] && shelf[i][j] == shelf[i + 3][j] && !flag &&
                            !check[i + 1][j] && !check[i + 2][j] && !check[i + 3][j]) {
                        counter++;
                        check[i][j] = true;
                        check[i + 1][j] = true;
                        check[i + 2][j] = true;
                        check[i + 3][j] = true;
                    }
                }
            }
        }
        return counter >= 4;
    }

    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

}
