package model.cards.concreteobjectives;

import model.Token;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static model.Configuration.COLUMNS_SHELF;
import static model.Configuration.ROWS_SHELF;
import static org.junit.jupiter.api.Assertions.*;

class CornersTest {

    @Test
    void trueScenario(){
        var firstCorner = new Corners();
        Token[][] shelf;
        Token type;

        shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];

        Random randomType = new Random();
        type=Token.values()[randomType.nextInt(Token.values().length)];

        for(int i = 0; i < ROWS_SHELF-1; i++){
            for(int j = 0; j < COLUMNS_SHELF-1; j++) {
                Random random = new Random();
                shelf[i][j]=Token.values()[random.nextInt(Token.values().length)];
            }
        }
        shelf[0][0] = type;
        shelf[0][COLUMNS_SHELF-1] = type;
        shelf[ROWS_SHELF-1][0] = type;
        shelf[ROWS_SHELF-1][COLUMNS_SHELF-1] = type;

        assertEquals(true,firstCorner.isSatisfied(shelf));
    }

    void WrongScenario(){
        var firstCorner = new Corners();
        Token[][] shelf;
        Token type;

        shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];


        for(int i = 0; i < ROWS_SHELF; i++){
            for(int j = 0; j < COLUMNS_SHELF; j++) {
                Random random = new Random();
                shelf[i][j]=Token.values()[random.nextInt(Token.values().length)];
            }
        }
        shelf[0][0]=Token.CAT;
        shelf[6][0]=Token.BOOK;
        assertEquals(false,firstCorner.isSatisfied(shelf));
    }


}