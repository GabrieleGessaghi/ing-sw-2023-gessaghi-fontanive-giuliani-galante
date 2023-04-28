package server.model.cards;

import server.model.Token;
import server.controller.utilities.ConfigLoader;

/**
 * Provides functions to help counting points.
 * @author Niccolò Giuliani
 */
public class TokenTools {
    private int counterInterIsland; //counter of the instances of the island
    /**
     * if modality is true count adjent point per Player, if modality is false count number of islands of at least four items dor FourGroups
     * @author Niccolò Giuliani
     * @param type type of Token
     * @param shelf shelf to check
     * @param modality false for FourGroups, true for Player
     * @return the count of the islands
     */
    public int counterIslandType(Token type, Token[][] shelf, boolean modality){
        int counterPerToken = 0; //counter of islands per type
        counterInterIsland = 0;
        boolean[][] checked = new boolean[ConfigLoader.SHELF_ROWS][ConfigLoader.SHELF_COLUMNS];
        for (int i = 0; i < ConfigLoader.SHELF_ROWS; i++)
            for (int j = 0; j < ConfigLoader.SHELF_COLUMNS; j++)
                checked[i][j] = false;
        for (int i = 0; i < ConfigLoader.SHELF_ROWS; i++)
            for (int j = 0; j < ConfigLoader.SHELF_COLUMNS; j++)
                if (shelf[i][j] == type && !checked[i][j]){
                    counterInterIsland = 1;
                    findIsland(shelf, i, j, checked, type);
                    if(!modality) {
                        if (counterInterIsland >= 4)
                            counterPerToken++;
                    }
                    else {
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
     * @param M Matrix to check
     * @param row row to check
     * @param col column to check
     * @param checked matrix of checked cells
     * @param type type of token
     * @return true if the cell is equal the Token type
     */
    private boolean isOk(Token[][] M, int row, int col, boolean[][] checked, Token type) {
        return (row >= 0) && (row < ConfigLoader.SHELF_ROWS) && (col >= 0) && (col < ConfigLoader.SHELF_COLUMNS)
                && (M[row][col] == type && !checked[row][col]);
    }

    /**
     * method for TwoRows and FourRows
     * @author Niccolò Giuliani
     * @param shelf shelf to check
     * @param modality modality false for FourRows, modality true for TwoRows
     * @return number of rows that satisfy the objective
     */
   public int rowsChecker(Token[][] shelf, boolean modality){
       int[] counter = new int[7];
       int differentType;
       int atLeast = 0;
       for (int i = 0; i < ConfigLoader.SHELF_ROWS ; i++){
           for (int w = 0; w < 7 ; w++)
               counter[w]=0;
           differentType = 0;
           for (int j = 0; j < ConfigLoader.SHELF_COLUMNS ; j++)
               switch (shelf[i][j]) {
                   case CAT -> counter[0]++;
                   case BOOK -> counter[1]++;
                   case TOY -> counter[2]++;
                   case TROPHY -> counter[3]++;
                   case FRAME -> counter[4]++;
                   case PLANT -> counter[5]++;
                   case NOTHING -> counter[6]++;

               }
           for (int n = 0; n < 7; n++)
               if(counter[n] > 0)
                   differentType++;
           if(!modality) {
               if (differentType <= 3 && counter[6] == 0)
                   atLeast++;
           }else{
               if (differentType == 5)
                   atLeast++;
           }

       }
       return atLeast;
   }

    /**
     * method for TwoColumns and ThreeColumns
     * @param shelf shelf to check
     * @param modality modality false for ThreeColumns, modality true for TwoColumns
     * @return number of columns that satisfy the objective
     */
   public int columnsChecker(Token[][] shelf, boolean modality){
       int[] counter = new int[7];
       int differentType;
       int atLeast = 0;
       for (int i = 0; i < ConfigLoader.SHELF_COLUMNS; i++) {
           for (int w=0; w < 7 ; w++)
               counter[w]=0;
           differentType = 0;
           for (int j = 0; j < ConfigLoader.SHELF_ROWS ; j++)
               switch (shelf[j][i]) {
                   case CAT -> counter[0]++;
                   case BOOK -> counter[1]++;
                   case TOY -> counter[2]++;
                   case TROPHY -> counter[3]++;
                   case FRAME -> counter[4]++;
                   case PLANT -> counter[5]++;
                   case NOTHING -> counter[6]++;
               }
           for (int n = 0; n < 7; n++)
               if (counter[n] > 0)
                   differentType++;
           if(!modality) {
               if (differentType <= 3 && counter[6] == 0)
                   atLeast++;
           } else {
               if (differentType == 6 && counter[6] == 0)
                   atLeast++;
           }
       }
       return atLeast;
   }
}
