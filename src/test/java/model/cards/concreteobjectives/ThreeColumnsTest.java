package model.cards.concreteobjectives;

import controller.Configurations;
import model.Token;
import org.junit.jupiter.api.Test;
import static controller.Configurations.SHELF_ROWS;
import static controller.Configurations.SHELF_COLUMNS;
import static org.junit.jupiter.api.Assertions.*;

public class ThreeColumnsTest {

    @Test
    public void isSatisfiedTestTrue(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        ThreeColumns threeC = new ThreeColumns();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
            {3, 5, 0, 6, 0},
            {2, 4, 0, 6, 0},
            {1, 5, 0, 6, 0},
            {3, 4, 0, 6, 0},
            {2, 5, 0, 6, 0},
            {1, 4, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertTrue(threeC.isSatisfied(testShelfTiles));
    }

    @Test
    public void isSatisfiedTestFalse(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        ThreeColumns threeC = new ThreeColumns();

        Token[][] testShelfTiles = new Token[SHELF_ROWS][SHELF_COLUMNS];
        int[][] testShelfInt = new int[][]{
                {3, 5, 0, 6, 0},
                {2, 4, 0, 6, 0},
                {4, 5, 0, 6, 0},
                {3, 4, 0, 6, 0},
                {2, 5, 0, 6, 0},
                {1, 4, 0, 6, 0}
        };
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++)
                testShelfTiles[i][j] = Token.values()[testShelfInt[i][j]];

        assertFalse(threeC.isSatisfied(testShelfTiles));
    }
}
