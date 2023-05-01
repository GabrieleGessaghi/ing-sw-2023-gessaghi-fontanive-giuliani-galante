package server.model.cards.concreteobjectives;

import server.model.Token;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;

/**
 * Two groups each containing 4 tiles of the same type in a 2x2 square.
 * The tiles of one square can be different from those of the other square.
 * @author Niccolò Giuliani
 */
public class TwoSquares implements CommonObjective {

    @Override
    public boolean isSatisfied(Token[][] shelf) {
        boolean flag = false;
        boolean[][] check = new boolean[ROWS][COLUMNS];
        int atLeastTwo = 0;
        for (int i = 0; i < ROWS; i++)
            for (int j=0; j< COLUMNS; j++)
                check[i][j] = false;

        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLUMNS - 1; j++) {
                if (!check[i][j] && shelf[i][j] != Token.NOTHING && i < 5 && j < 4){
                    if (shelf[i][j] == shelf[i][j + 1] && shelf[i][j] == shelf[i + 1][j] && shelf[i][j] == shelf[i +1][j + 1]
                    && !check[i][j + 1] && !check[i + 1][j] && !check[i + 1][j + 1]) {
                        atLeastTwo++;
                        check[i][j] = true;
                        check[i][j + 1] = true;
                        check[i + 1][j] = true;
                        check[i + 1][j + 1] = true;
                    }
                }
            }
        }


        return atLeastTwo >= 2;
    }

    @Override
    public CommonType getName(){
        return CommonType.TWOSQUARES;
    }

    @Override
    public String getDescription() {
        return "Two groups each containing 4 tiles of the same type in a 2x2 square.\n" +
                "The tiles of one square can be different from those of the other square.";
    }
}
