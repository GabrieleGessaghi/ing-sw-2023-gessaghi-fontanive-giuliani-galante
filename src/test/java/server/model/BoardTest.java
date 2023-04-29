package server.model;

import server.controller.utilities.ConfigLoader;
import server.model.Board;
import server.model.Token;
import server.model.exceptions.IllegalMoveException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void constructorTest() {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Board board = new Board(2);

        Token[][] testTokens;
        testTokens = board.getTiles();
        assertEquals(Token.NOTHING, testTokens[0][0]);
        assertNotEquals(Token.NOTHING, testTokens[3][3]);
    }

    @Test
    public void selectTilesTest() {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Board board = new Board(2);

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
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Board board = new Board(2);

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
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Board board = new Board(2);

        Token[][] testTiles = board.getTiles();
        boolean[][] selectedTiles = new boolean[][] {
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true},
                {true, true, true, true, true, true, true, true, true}
        };
        board.removeTiles(selectedTiles);
        testTiles = board.getTiles();
        assertNotEquals(Token.NOTHING, testTiles[3][3]);
    }
}