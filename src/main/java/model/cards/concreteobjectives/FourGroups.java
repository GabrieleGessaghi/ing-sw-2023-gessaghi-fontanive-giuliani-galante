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
    private int counterInterIsland; //counter of the instances of the island
    //TODO: Document class

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        int generalCounter; //general counter of the groups
        generalCounter = checkType(Token.CAT, shelf) + checkType(Token.TOY, shelf) + checkType(Token.BOOK, shelf) +
                checkType(Token.TROPHY, shelf) + checkType(Token.FRAME, shelf) + checkType(Token.PLANT, shelf);
        return generalCounter >= 4;
    }

    /**
     * count the islands containing at least four instances a particular type
     * @author Niccolò Giuliani
     * @param type
     * @param shelf
     * @return the count of the islands
     */
    private int checkType(Token type, Token[][] shelf){
        int counterPerToken = 0; //counter of islands per type
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

    /**
     * recursive method to detect the islands starting from an unchecked cell
     * @param M matrix
     * @param row row of the initial cell
     * @param col column of the initial cell
     * @param checked matrix of checked cells
     * @param type type of Token
     * @author Niccolò Giuliani
     */
    private void findIsland(Token[][] M, int row, int col, boolean[][] checked, Token type) {
        //arrays for the position of the neighbors
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

    /**
     * check if the cell is equal to the Token type
     * @author Niccolò Giuliani
     * @param M
     * @param row
     * @param col
     * @param checked
     * @param type
     * @return true if the cell is equal the Token type
     */
    boolean isOk(Token[][] M, int row, int col, boolean[][] checked, Token type) {
        return (row >= 0) && (row < ROWS) && (col >= 0) && (col < COLUMNS)
                && (M[row][col] == type && !checked[row][col]);
    }

    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

}
