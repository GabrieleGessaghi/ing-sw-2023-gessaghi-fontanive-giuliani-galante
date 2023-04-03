package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;

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
    //TODO: Document class

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int generalCounter;
        generalCounter = checkType(Token.CAT, shelf) + checkType(Token.TOY, shelf) + checkType(Token.BOOK, shelf) +
                checkType(Token.TROPHY, shelf) + checkType(Token.FRAME, shelf) + checkType(Token.PLANT, shelf);
        return generalCounter >= 4;
    }

    private int checkType(Token type, Token[][] shelf){
        int counterPerToken = 0;
        counterInterIsland = 0;
        boolean[][] checked = new boolean[ROWS][COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
                checked[i][j] = false;
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
                if (shelf[i][j] == type && !checked[i][j]){
                    counterInterIsland = 1;
                    findIsland(shelf, i, j, checked, type);
                    if (counterInterIsland >= 4)
                        counterPerToken ++;
                }
        return counterPerToken;
    }

    private void findIsland(Token[][] M, int row, int col, boolean[][] checked, Token type) {
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

    boolean isOk(Token[][] M, int row, int col, boolean[][] checked, Token type) {
        return (row >= 0) && (row < ROWS) && (col >= 0) && (col < COLUMNS)
                && (M[row][col] == type && !checked[row][col]);
    }

    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

}
