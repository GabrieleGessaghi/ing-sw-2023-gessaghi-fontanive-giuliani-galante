package model.cards.concreteobjectives;

import model.Token;
import org.junit.jupiter.api.Test;

import static model.Configuration.COLUMNS_SHELF;
import static model.Configuration.ROWS_SHELF;
import static org.junit.jupiter.api.Assertions.*;

class FourGroupsTest {
    @Test
    void FourIslands(){
        var fourGroups = new FourGroups();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];

        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;

        shelf[0][0] = Token.CAT;
        shelf[0][1] = Token.CAT;
        shelf[0][2] = Token.TROPHY;
        shelf[0][3] = Token.TOY;
        shelf[0][4] = Token.TOY;
        shelf[1][0] = Token.CAT;
        shelf[1][1] = Token.CAT;
        shelf[1][2] = Token.FRAME;
        shelf[1][3] = Token.TOY;
        shelf[1][4] = Token.NOTHING;
        shelf[2][0] = Token.NOTHING;
        shelf[2][1] = Token.NOTHING;
        shelf[2][2] = Token.NOTHING;
        shelf[2][3] = Token.TOY;
        shelf[2][4] = Token.NOTHING;
        shelf[3][0] = Token.PLANT;
        shelf[3][1] = Token.PLANT;
        shelf[3][2] = Token.NOTHING;
        shelf[3][3] = Token.NOTHING;
        shelf[3][4] = Token.NOTHING;
        shelf[4][0] = Token.NOTHING;
        shelf[4][1] = Token.PLANT;
        shelf[4][2] = Token.NOTHING;
        shelf[4][3] = Token.CAT;
        shelf[4][4] = Token.CAT;
        shelf[5][0] = Token.PLANT;
        shelf[5][1] = Token.PLANT;
        shelf[5][2] = Token.NOTHING;
        shelf[5][3] = Token.CAT;
        shelf[5][4] = Token.CAT;

        assertTrue(fourGroups.isSatisfied(shelf));
    }
    @Test
    void ThreeIslands(){
        var fourGroups = new FourGroups();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];

        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;

        shelf[0][0] = Token.NOTHING;
        shelf[0][1] = Token.CAT;
        shelf[0][2] = Token.TROPHY;
        shelf[0][3] = Token.TOY;
        shelf[0][4] = Token.TOY;
        shelf[1][0] = Token.CAT;
        shelf[1][1] = Token.CAT;
        shelf[1][2] = Token.FRAME;
        shelf[1][3] = Token.TOY;
        shelf[1][4] = Token.NOTHING;
        shelf[2][0] = Token.NOTHING;
        shelf[2][1] = Token.NOTHING;
        shelf[2][2] = Token.NOTHING;
        shelf[2][3] = Token.TOY;
        shelf[2][4] = Token.NOTHING;
        shelf[3][0] = Token.PLANT;
        shelf[3][1] = Token.PLANT;
        shelf[3][2] = Token.NOTHING;
        shelf[3][3] = Token.NOTHING;
        shelf[3][4] = Token.NOTHING;
        shelf[4][0] = Token.NOTHING;
        shelf[4][1] = Token.PLANT;
        shelf[4][2] = Token.NOTHING;
        shelf[4][3] = Token.CAT;
        shelf[4][4] = Token.CAT;
        shelf[5][0] = Token.PLANT;
        shelf[5][1] = Token.PLANT;
        shelf[5][2] = Token.NOTHING;
        shelf[5][3] = Token.CAT;
        shelf[5][4] = Token.CAT;

        assertFalse(fourGroups.isSatisfied(shelf));
    }
}