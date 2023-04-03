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
        boolean [][]check;
        check = new boolean[ROWS][COLUMNS];
        int []atLeastTwo;
        atLeastTwo = new int[7];
        for (int i = 0; i < ROWS; i++)
            for(int j=0; j< COLUMNS; j++)
                check[i][j] = false;
        for(int i = 0; i < 7; i++)
            atLeastTwo[i]=0;

        for(int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLUMNS - 1; j++) {
                if(!check[i][j] && shelf[i][j] != Token.NOTHING && i < 5 && j < 4){
                    if(shelf[i][j] == shelf[i][j + 1] && shelf[i][j] == shelf[i + 1][j] && shelf[i][j] == shelf[i +1][j + 1]
                    && !check[i][j + 1] && !check[i + 1][j] && !check[i + 1][j + 1]) {
                        atLeastTwo[function(shelf[i][j])]++;
                        check[i][j] = true;
                        check[i][j + 1] = true;
                        check[i + 1][j] = true;
                        check[i + 1][j + 1] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 7 && !flag; i++){
            if(atLeastTwo[i] >= 2)
                flag=true;
        }
        return flag;
    }

    private int function(Token x){
        if(x == Token.CAT)
            return 0;
        else if(x == Token.BOOK)
            return 1;
        else if(x == Token.TOY)
            return 2;
        else if(x == Token.TROPHY)
            return 3;
        else if(x == Token.FRAME)
            return 4;
        else if(x == Token.PLANT)
            return 5;
        else if(x == Token.NOTHING)
            return 6;
        return -1;
    }

    public CommonType getName(){
        return CommonType.TWOSQUARES;
    }
}
