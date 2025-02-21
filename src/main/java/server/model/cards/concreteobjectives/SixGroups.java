package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;

/**
 * Six groups each containing at least 2 tiles of the same type.
 * The tiles of one group can be different from those of another group.
 * @author Niccolò Giuliani
 */
public class SixGroups implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int counter = 0;
        boolean flag;
        boolean [][] check;
        check = new boolean[ROWS][COLUMNS];
        for (int i = 0; i < ROWS ; i++)
            for (int j = 0; j< COLUMNS ; j++)
                check[i][j] = false;
        for (int i = 0; i < ROWS - 1 && counter < 6; i++){
            for (int j = 0; j < COLUMNS - 1 ; j++){
                flag = false;
                if (shelf[i][j] != Token.NOTHING && !check[i][j]) {
                    if (shelf[i][j] == shelf[i][j + 1]) {
                        counter++;
                        flag = true;
                        check[i][j +1] = true;
                    }
                    if (shelf[i][j] == shelf[i + 1][j] && !flag) {
                        counter++;
                        check[i + 1][j] = true;
                    }
                }
            }
        }
        return counter >= 6;
    }

    @Override
    public CommonType getName(){
        return CommonType.SIXGROUPS;
    }

    @Override
    public String getDescription() {
        return  "Six groups each containing at least 2 tiles of the same type.\nThe tiles of one group can be different from those of another group";
    }
}
