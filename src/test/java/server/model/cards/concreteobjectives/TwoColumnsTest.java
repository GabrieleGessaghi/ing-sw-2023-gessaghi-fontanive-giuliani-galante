package server.model.cards.concreteobjectives;

import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.TwoColumns;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoColumnsTest {
    @Test
    public void isSatisfiedTestTrue(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        TwoColumns twoC = new TwoColumns();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {6, 5, 0, 6, 0},
                {5, 6, 0, 6, 0},
                {4, 3, 0, 6, 0},
                {3, 4, 0, 6, 0},
                {2, 2, 0, 6, 0},
                {1, 1, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertTrue(twoC.isSatisfied(testShelfTiles));
    }

    @Test
    public void isSatisfiedTestFalse(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        TwoColumns twoC = new TwoColumns();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {6, 4, 0, 6, 0},
                {5, 6, 0, 6, 0},
                {4, 5, 0, 6, 0},
                {3, 3, 0, 6, 0},
                {2, 2, 0, 6, 0},
                {1, 2, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertFalse(twoC.isSatisfied(testShelfTiles));
    }
}
