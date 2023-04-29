package server.model.cards.concreteobjectives;

import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.XShape;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XShapeTest {
    @Test
    public void isSatisfiedTestTrue(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        XShape X = new XShape();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {1, 2, 1, 0, 4},
                {3, 1, 5, 4, 4},
                {1, 4, 1, 0, 5},
                {4, 6, 3, 3, 5},
                {5, 5, 3, 3, 0},
                {6, 1, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertTrue(X.isSatisfied(testShelfTiles));
    }

    @Test
    public void isSatisfiedTestFalse(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        XShape X = new XShape();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {2, 1, 2, 6, 5},
                {1, 2, 0, 6, 0},
                {4, 5, 0, 6, 0},
                {3, 4, 0, 6, 0},
                {2, 5, 0, 6, 6},
                {1, 4, 0, 6, 6}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertFalse(X.isSatisfied(testShelfTiles));
    }
}
