package model.cards.concreteobjectives;

import controller.Configurations;
import model.Token;
import org.junit.jupiter.api.Test;

import static controller.Configurations.SHELF_COLUMNS;
import static controller.Configurations.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TwoColumnsTest {
    @Test
    public void isSatisfiedTestTrue(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
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
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        ThreeColumns threeC = new ThreeColumns();

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

        assertFalse(threeC.isSatisfied(testShelfTiles));
    }
}
