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
        Token shelf[][];
        Token type;
        Random random =new Random();
        shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        type = Token.CAT;
        int rand = random.nextInt(20);
        rand = rand+8;
        for(int i = 0; i < ROWS_SHELF; i++){
            for (int j = 0;j < COLUMNS_SHELF; j++){
                shelf[i][j] = Token.NOTHING;
            }
        }
        for(int i = 0; i < rand; i++){
            Random randomIndexRow=new Random();
            int indexRow;
            int indexColumn;
            do {
                indexRow = randomIndexRow.nextInt(5);
                Random randomIndexColumn = new Random();
                indexColumn= randomIndexColumn.nextInt(4);
            }while(shelf[indexRow][indexColumn] == Token.NOTHING)
            shelf[indexRow][indexColumn]=type;
        }

    assertTrue(trueS.isSatisfied(shelf));

    }

}