package model.cards.concreteobjectives;

import model.Token;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static model.Configuration.COLUMNS_SHELF;
import static model.Configuration.ROWS_SHELF;
import static org.junit.jupiter.api.Assertions.*;

class EightanyTest {
    @Test
    void trueScenario(){
        var trueS = new Eightany();
        int indexRow;
        int indexColumn;
        Token shelf[][];
        Token type;
        Random random =new Random();
        shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        type = Token.CAT;
        int rand = random.nextInt(20);
        rand = rand + 8;
        for(int i = 0; i < ROWS_SHELF; i++){
            for (int j = 0;j < COLUMNS_SHELF; j++){
                shelf[i][j] = Token.NOTHING;
            }
        }
        for(int i = 0; i < rand; i++){


            do {
                Random randomIndexRow = new Random();
                indexRow = randomIndexRow.nextInt(6);
                Random randomIndexColumn = new Random();
                indexColumn = randomIndexColumn.nextInt(5);
            }while(shelf[indexRow][indexColumn] != Token.NOTHING);
            shelf[indexRow][indexColumn]=type;
        }

    assertTrue(trueS.isSatisfied(shelf));

    }

    @Test
    void wrongScenario(){
        var wrong = new Eightany();
        Token shelf[][];
        Random random =new Random();
        shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];

        for(int i = 0; i < ROWS_SHELF; i++){
            for (int j = 0;j < COLUMNS_SHELF; j++){
                shelf[i][j] = Token.NOTHING;
            }
        }
        shelf[0][0] = Token.BOOK;
        shelf[1][1] = Token.TROPHY;
        shelf[1][2] = Token.BOOK;
        shelf[1][3] = Token.BOOK;
        shelf[1][4] = Token.BOOK;
        shelf[5][1] = Token.BOOK;
        shelf[4][4] = Token.BOOK;
        shelf[3][3] = Token.BOOK;
        assertFalse(wrong.isSatisfied(shelf));
    }


}