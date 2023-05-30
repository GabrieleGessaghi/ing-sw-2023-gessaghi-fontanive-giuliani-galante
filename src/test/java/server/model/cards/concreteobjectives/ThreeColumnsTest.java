package server.model.cards.concreteobjectives;

import org.junit.jupiter.api.BeforeEach;
import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.ThreeColumns;

import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static org.junit.jupiter.api.Assertions.*;

public class ThreeColumnsTest {

    @BeforeEach
    void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
    }

    @Test
    public void isSatisfiedTestTrue(){
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
