package model.cards.concreteobjectives;

import controller.Configurations;
import model.Token;
import org.junit.jupiter.api.Test;

import static controller.Configurations.SHELF_COLUMNS;
import static controller.Configurations.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoSquaresTest {
    @Test
    public void isSatisfiedTestTrue(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        TwoSquares twoS = new TwoSquares();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {3, 1, 1, 0, 4},
                {3, 1, 1, 4, 4},
                {3, 4, 2, 0, 5},
                {4, 6, 3, 3, 5},
                {5, 5, 3, 3, 0},
                {6, 1, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertTrue(twoS.isSatisfied(testShelfTiles));
    }

    @Test
    public void isSatisfiedTestFalse(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        TwoSquares twoS = new TwoSquares();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {2, 2, 2, 6, 5},
                {2, 2, 0, 6, 0},
                {4, 5, 0, 6, 0},
                {3, 4, 0, 6, 0},
                {2, 5, 0, 6, 6},
                {1, 4, 0, 6, 6}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertFalse(twoS.isSatisfied(testShelfTiles));
    }
}
