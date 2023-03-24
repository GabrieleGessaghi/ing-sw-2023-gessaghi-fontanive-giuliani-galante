package model.cards.concreteobjectives;

import model.Token;
import model.cards.CommonObjective;
import model.cards.CommonType;

/**
 * @author Niccolò Giuliani
 * Two groups each containing 4 tiles of
 * the same type in a 2x2 square. The tiles
 * of one square can be different from
 * those of the other square.
 */
public class TwoSquares implements CommonObjective {
    @Override
    public int getPoints(Token[][] shelf) {
        int flag = 0;
        boolean [][]check;
        check = new boolean[ROWS][COLUMNS];
        int []atLeastTwo;
        atLeastTwo = new int[7];
        for (int i = 0; i < ROWS; i++)
            for(int j=0; j< COLUMNS; j++)
                check[i][j] = false;
        for(int i = 0; i < 7; i++)
            atLeastTwo[i]=0;

        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(check[i][j] == false && shelf[i][j] != Token.NOTHING && i < 5 && j < 4){
                    if(shelf[i][j] == shelf[i][j + 1] && shelf[i][j] == shelf[i + 1][j] && shelf[i][j] == shelf[i +1][j + 1]
                    && check[i][j + 1] == false && check[i + 1][j] == false && check [i + 1][j + 1] == false)
                        atLeastTwo[function(shelf[i][j])]++;
                        check[i][j]=true;
                        check[i][j+1]=true;
                        check[i + 1][j]=true;
                        check[i + 1][j + 1]=true;
                }
            }
        }
        for (int i = 0; i < 7 && flag == 0; i++){
            if(atLeastTwo[i] >= 2)
                flag=1;
        }

        if (flag == 1)
            return 1;
        else
            return 0;
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

    public CommonType name(){
        return CommonType.TWOSQUARES;
    }
}
