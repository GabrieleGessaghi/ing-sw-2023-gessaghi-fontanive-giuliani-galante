package model.cards.concreteobjectives;

import model.Token;
import org.junit.jupiter.api.Test;

import static model.Configurations.COLUMNS_SHELF;
import static model.Configurations.ROWS_SHELF;
import static org.junit.jupiter.api.Assertions.*;

class StairsTest {
    @Test
    void stairLeft(){
        var stair= new Stairs();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];


        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 0; i < ROWS_SHELF - 1; i++)
            shelf[i][0] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 2; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 3; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 4; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 5; i++)
            shelf[i][4] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void stairRight(){
        var stair= new Stairs();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 0; i < ROWS_SHELF - 1; i++)
            shelf[i][4] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 2; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 3; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 4; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 0; i < ROWS_SHELF - 5; i++)
            shelf[i][0] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void stairLeftUp(){
        var stair= new Stairs();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 1; i < ROWS_SHELF ; i++)
            shelf[i][0] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 1 ; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 2; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 3; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 4; i++)
            shelf[i][4] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void stairRightUp(){
        var stair= new Stairs();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 1; i < ROWS_SHELF ; i++)
            shelf[i][4] = Token.CAT;
        for(int i =  1; i < ROWS_SHELF - 1; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 2; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 3; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 1; i < ROWS_SHELF - 4; i++)
            shelf[i][0] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void wrongScenario(){
        var stair= new Stairs();
        Token[][] shelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for (int i = 0; i < ROWS_SHELF; i++)
            for (int j = 0; j < COLUMNS_SHELF; j++)
                shelf[i][j] = Token.NOTHING;
        shelf[0][0] = Token.CAT;
        shelf[1][0] = Token.CAT;
        shelf[2][0] = Token.CAT;
        shelf[3][0] = Token.CAT;
        shelf[4][0] = Token.CAT;
        shelf[5][0] = Token.CAT;
        shelf[2][1] = Token.CAT;
        shelf[3][1] = Token.CAT;
        shelf[4][1] = Token.CAT;
        shelf[5][1] = Token.CAT;
        assertFalse(stair.isSatisfied(shelf));

    }
}