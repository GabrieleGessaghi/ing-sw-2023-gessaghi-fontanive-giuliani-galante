package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * Two groups each containing 4 tiles of
 * the same type in a 2x2 square. The tiles
 * of one square can be different from
 * those of the other square.
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

    private int fromTokenToInt(Token x){
        return switch (x) {
            case CAT -> 0;
            case BOOK -> 1;
            case TOY -> 2;
            case TROPHY -> 3;
            case FRAME -> 4;
            case PLANT -> 5;
            case NOTHING -> 6;
        };
    }

    public CommonType getName(){
        return CommonType.TWOSQUARES;
    }
}
