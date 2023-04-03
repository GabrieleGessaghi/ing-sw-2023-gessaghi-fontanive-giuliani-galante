package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void constructorTest() {
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        Board twoPlayersBoard = new Board(2);
        Board threePlayersBoard = new Board(3);
        Board fourPlayersBoard = new Board(4);
        Token[][] testTokens;
        testTokens = twoPlayersBoard.getTiles();
        assertEquals(testTokens[0][0], Token.NOTHING);
    }
}