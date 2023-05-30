package server.model.cards;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.controller.utilities.ConfigLoader;
import server.model.Player;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.PersonalCard;

import java.util.ArrayList;
import java.util.List;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class PersonalCardTest {

    PersonalCard pc;

    @BeforeEach
    public void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        pc = new PersonalCard(6);
    }

    @AfterEach
    public void teardown() {
        pc = null;
    }

    @Test
    public void constructorTest(){
        Token[][] testTokens = pc.getCorrectTiles();
        assertEquals(Token.NOTHING, testTokens[0][0]);
        assertNotEquals(Token.NOTHING, testTokens[0][2]);
    }

    @Test
    public void getPointsTestOne(){
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