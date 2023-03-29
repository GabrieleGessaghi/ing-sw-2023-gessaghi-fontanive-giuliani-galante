package model.cards.concreteobjectives;

import model.Token;
import org.junit.Test;

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
        shelf[0][0] = shelf[0][COLUMNS_SHELF-1] = shelf[ROWS_SHELF][0] = shelf[ROWS_SHELF][COLUMNS_SHELF] = type;

        for(int i = 1; i < ROWS_SHELF-2; i++){
            for(int j = 1; j < COLUMNS_SHELF-2; j++) {
                Random random = new Random();
                shelf[i][j]=Token.values()[random.nextInt(Token.values().length)];
            }
        }
        assertEquals(true,firstCorner.isSatisfied(shelf));
    }

}