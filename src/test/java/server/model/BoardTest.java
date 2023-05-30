package server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.controller.utilities.ConfigLoader;
import server.model.exceptions.IllegalMoveException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;

    @BeforeEach
    public void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        board = new Board(2);
    }

    @AfterEach
    public void teardown() {
        board = null;
    }

    @Test
    public void constructorTest() {
        Token[][] testTokens;
        testTokens = board.getTiles();
        assertEquals(Token.NOTHING, testTokens[0][0]);
        assertNotEquals(Token.NOTHING, testTokens[3][3]);
    }

    @Test
    public void selectTilesTest() {
        Token[] selectedTokens;
        int[][] selectedTiles = new int[][] {
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, 0, 1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1}
        };
        try {
            selectedTokens = board.selectTiles(selectedTiles);
            assertNotEquals(Token.NOTHING, selectedTokens[0]);
            assertNotEquals(Token.NOTHING, selectedTokens[1]);
            assertEquals(Token.NOTHING, selectedTokens[2]);
        } catch (IllegalMoveException e) {
            fail();
        }
    }

    @Test
    public void removeTilesTest() {
        Token[][] testTokens;
        testTokens = board.getTiles();
        assertNotEquals(Token.NOTHING, testTokens[3][3]);
        boolean[][] selectedTiles = new boolean[][] {
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, true, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false}
        };
        board.removeTiles(selectedTiles);
        assertEquals(Token.NOTHING, testTokens[3][3]);
    }

    @Test
    public void automaticResetTest() {
        Token[][] testTiles;
        boolean[][] selectedTiles = new boolean[][] {
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, false, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true}
        };
        board.removeTiles(selectedTiles);
        testTiles = board.getTiles();
        assertNotEquals(Token.NOTHING, testTiles[3][3]);
    }
}