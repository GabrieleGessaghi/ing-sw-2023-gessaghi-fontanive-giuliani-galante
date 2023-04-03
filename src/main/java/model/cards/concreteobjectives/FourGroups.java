package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

import static model.Configuration.COLUMNS_SHELF;
import static model.Configuration.ROWS_SHELF;

/**
 * Four groups each containing at least
 * 4 tiles of the same type (not necessarily
 * in the depicted shape).
 * The tiles of one group can be different
 * from those of another group.
 * @author Niccolò Giuliani
 */
public class FourGroups implements CommonObjective {
    private int counterInterIsland;

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int generalCounter = 0;
        generalCounter = checkType(Token.CAT, shelf) + checkType(Token.TOY, shelf) + checkType(Token.BOOK, shelf) +
                checkType(Token.TROPHY, shelf) + checkType(Token.FRAME, shelf) + checkType(Token.PLANT, shelf);
        return generalCounter >= 4;
    }

    private int checkType(Token type, Token[][] shelf){
        int counterPerToken = 0;
        counterInterIsland = 0;
        boolean[][] checked = new boolean[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                checked[i][j] = false;
        for(int i = 0; i < ROWS_SHELF; i++){
            for(int j = 0; j < COLUMNS_SHELF; j++)
                if(shelf[i][j] == type && !checked[i][j]){
                    counterInterIsland = 1;
                    findIsland(shelf, i, j, checked, type);
                    if(counterInterIsland >= 4)
                        counterPerToken ++;
                }
        }
        System.out.println("\n");
        return counterPerToken;
    }

    private void findIsland(Token[][] M, int row, int col,
                     boolean[][] checked, Token type){

        int[] rowIndex = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] colIndex = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };

        checked[row][col] = true;

        for (int i = 0; i < 8; i++)
            if (isOk(M, row + rowIndex[i], col + colIndex[i],
                    checked,  type)) {
                counterInterIsland++;
                findIsland(M, row + rowIndex[i], col + colIndex[i], checked, type);
            }
    }

    boolean isOk(Token[][] M, int row, int col,
                   boolean[][] checked, Token type)
    {
        return (row >= 0) && (row < ROWS_SHELF) && (col >= 0) && (col < COLUMNS_SHELF)
                && (M[row][col] == type && !checked[row][col]);
    }


    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

}
