package model.cards;

import controller.Configurations;
import model.Token;
import org.junit.jupiter.api.Test;

import static controller.Configurations.SHELF_COLUMNS;
import static controller.Configurations.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class PersonalCardTest {

    @Test
    public void constructorTest(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        PersonalCard pc = new PersonalCard(6);

        Token[][] testTokens = pc.getCorrectTiles();
        assertEquals(Token.NOTHING, testTokens[0][0]);
        assertNotEquals(Token.NOTHING, testTokens[0][2]);
    }

    @Test
    public void getPointsTestOne(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        PersonalCard pc = new PersonalCard(6);

        Token[][] testTokens = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testTokensInt = new int[][] {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testTokens[i][j] = Token.values()[testTokensInt[i][j]];

        assertEquals(pc.getPoints(testTokens), 0);
    }

    @Test
    public void getPointsTestTwo(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        PersonalCard pc = new PersonalCard(6);

        Token[][] testTokens = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testTokensInt = new int[][] {
                {0, 0, 4, 0, 1},
                {0, 0, 1, 0, 1},
                {0, 0, 1, 2, 1},
                {0, 0, 1, 1, 1},
                {0, 3, 1, 5, 1},
                {6, 1, 1, 1, 1},
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testTokens[i][j] = Token.values()[testTokensInt[i][j]];

        assertEquals(pc.getPoints(testTokens), 12);
    }
}