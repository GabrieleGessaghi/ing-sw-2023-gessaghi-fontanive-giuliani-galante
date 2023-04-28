package model.cards;

import model.Token;

import static controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static controller.utilities.ConfigLoader.SHELF_ROWS;

public class ModelUtil {
    private int counterInterIsland; //counter of the instances of the island
    /**
     * count the islands containing at least four instances a particular type
     * @author Niccolò Giuliani
     * @param type
     * @param shelf
     * @param modality false for FourGroups, true for Player
     * @return the count of the islands
     */
    public int counterIslandType(Token type, Token[][] shelf, boolean modality){
        int counterPerToken = 0; //counter of islands per type
        counterInterIsland = 0;
        boolean[][] checked = new boolean[SHELF_ROWS][SHELF_COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
                checked[i][j] = false;
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
                if (shelf[i][j] == type && !checked[i][j]){
                    counterInterIsland = 1;
                    findIsland(shelf, i, j, checked, type);
                    if(!modality) {
                        if (counterInterIsland >= 4)
                            counterPerToken++;
                    }
                    else if(modality){
                        if(counterInterIsland >= 6)
                            counterPerToken += 8;
                        else if(counterInterIsland == 5)
                            counterPerToken += 5;
                        else if(counterInterIsland == 4)
                            counterPerToken += 3;
                        else if(counterInterIsland== 3)
                            counterPerToken += 2;
                        }

                }
        return counterPerToken;
    }

    /**
     * recursive method to detect the islands starting from an unchecked cell
     * @author Niccolò Giuliani
     * @param M matrix
     * @param row row of the initial cell
     * @param col column of the initial cell
     * @param checked matrix of checked cells
     * @param type type of Token
     */
    private void findIsland(Token[][] M, int row, int col, boolean[][] checked, Token type) {
        //arrays for the position of the neighbors
        int[] rowIndex = new int[] {-1, 0, 0, 1 };
        int[] colIndex = new int[] { 0,-1, 1, 0 };

        checked[row][col] = true;

        for (int i = 0; i < 4; i++)
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
    private boolean isOk(Token[][] M, int row, int col, boolean[][] checked, Token type) {
        return (row >= 0) && (row < SHELF_ROWS) && (col >= 0) && (col < SHELF_COLUMNS)
                && (M[row][col] == type && !checked[row][col]);
    }

    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }




}
