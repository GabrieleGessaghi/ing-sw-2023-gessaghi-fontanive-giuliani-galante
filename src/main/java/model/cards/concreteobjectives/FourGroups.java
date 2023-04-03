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
        boolean[][] visited = new boolean[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                visited[i][j] = false;
        for(int i = 0; i < ROWS_SHELF; i++){
            for(int j = 0; j < COLUMNS_SHELF; j++)
                if(shelf[i][j] == type && !visited[i][j]){
                    counterInterIsland = 1;
                    DFS(shelf, i, j, visited, type);
                    if(counterInterIsland >= 4)
                        counterPerToken ++;
                }
        }
        System.out.println("\n");
        return counterPerToken;
    }

    private void DFS(Token[][] M, int row, int col,
                     boolean[][] visited, Token type){
        int[] rowNbr
                = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] colNbr
                = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };

        // Mark this cell as visited
        visited[row][col] = true;

        // Recur for all connected neighbours
        for (int k = 0; k < 8; k++)
            if (isSafe(M, row + rowNbr[k], col + colNbr[k],
                    visited,  type)) {
                counterInterIsland++;
                DFS(M, row + rowNbr[k], col + colNbr[k],
                        visited, type);
            }
    }

    boolean isSafe(Token[][] M, int row, int col,
                   boolean[][] visited, Token type)
    {
        // row number is in range, column number is in range
        // and value is 1 and not yet visited
        return (row >= 0) && (row < ROWS_SHELF) && (col >= 0)
                && (col < COLUMNS_SHELF)
                && (M[row][col] == type && !visited[row][col]);
    }


    public CommonType getName(){
        return CommonType.FOURGROUPS;
    }

}
