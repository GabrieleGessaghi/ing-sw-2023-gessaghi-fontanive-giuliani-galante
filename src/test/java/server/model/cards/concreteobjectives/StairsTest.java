package server.model.cards.concreteobjectives;

import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.Stairs;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class StairsTest {
    @Test
    void stairLeft(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        var stair = new Stairs();
        Token[][] shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];


        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 0; i < SHELF_ROWS - 1; i++)
            shelf[i][0] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 2; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 3; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 4; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 5; i++)
            shelf[i][4] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void stairRight(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        var stair= new Stairs();
        Token[][] shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 0; i < SHELF_ROWS - 1; i++)
            shelf[i][4] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 2; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 3; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 4; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 0; i < SHELF_ROWS - 5; i++)
            shelf[i][0] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void stairLeftUp(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        var stair= new Stairs();
        Token[][] shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 1; i < SHELF_ROWS; i++)
            shelf[i][0] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 1 ; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 2; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 3; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 4; i++)
            shelf[i][4] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void stairRightUp(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        var stair= new Stairs();
        Token[][] shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                shelf[i][j] = Token.NOTHING;
        for(int i = 1; i < SHELF_ROWS; i++)
            shelf[i][4] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 1; i++)
            shelf[i][3] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 2; i++)
            shelf[i][2] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 3; i++)
            shelf[i][1] = Token.CAT;
        for(int i = 1; i < SHELF_ROWS - 4; i++)
            shelf[i][0] = Token.CAT;
        assertTrue(stair.isSatisfied(shelf));
    }
    @Test
    void wrongScenario(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        var stair= new Stairs();
        Token[][] shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for (int i = 0; i < SHELF_ROWS; i++)
            for (int j = 0; j < SHELF_COLUMNS; j++)
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