package model.cards.concreteobjectives;

import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.TwoRows;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoRowsTest {
    @Test
    public void isSatisfiedTestTrue(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        TwoRows twoR = new TwoRows();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {1, 2, 3, 4, 5},
                {1, 3, 2, 4, 5},
                {3, 4, 2, 0, 5},
                {4, 6, 3, 4, 5},
                {5, 5, 0, 6, 0},
                {6, 1, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertTrue(twoR.isSatisfied(testShelfTiles));
    }

    @Test
    public void isSatisfiedTestFalse(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        TwoRows twoR = new TwoRows();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {1, 3, 2, 6, 5},
                {2, 4, 0, 6, 0},
                {4, 5, 0, 6, 0},
                {3, 4, 0, 6, 0},
                {2, 5, 0, 6, 0},
                {1, 4, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertFalse(twoR.isSatisfied(testShelfTiles));
    }
}
