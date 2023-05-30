package server.model.cards.concreteobjectives;

import org.junit.jupiter.api.BeforeEach;
import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.FourRows;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class FourRowsTest {

    @BeforeEach
    void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
    }


    @Test
    public void fourCase(){
        FourRows fourrows = new FourRows();
        Token [][]shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[0][0] = Token.TROPHY;
        shelf[0][1] = Token.TOY;
        shelf[0][2] = Token.CAT;
        shelf[0][3] = Token.FRAME;
        shelf[0][4] = Token.BOOK;
        shelf[1][0] = Token.CAT;
        shelf[1][1] = Token.CAT;
        shelf[1][2] = Token.CAT;
        shelf[1][3] = Token.CAT;
        shelf[1][4] = Token.CAT;
        shelf[2][0] = Token.TOY;
        shelf[2][1] = Token.TOY;
        shelf[2][2] = Token.TOY;
        shelf[2][3] = Token.TOY;
        shelf[2][4] = Token.TOY;
        shelf[3][0] = Token.PLANT;
        shelf[3][1] = Token.TROPHY;
        shelf[3][2] = Token.TOY;
        shelf[3][3] = Token.PLANT;
        shelf[3][4] = Token.PLANT;
        shelf[4][0] = Token.BOOK;
        shelf[4][1] = Token.PLANT;
        shelf[4][2] = Token.TROPHY;
        shelf[4][3] = Token.TROPHY;
        shelf[4][4] = Token.TROPHY;
        shelf[5][0] = Token.PLANT;
        shelf[5][1] = Token.PLANT;
        shelf[5][2] = Token.FRAME;
        shelf[5][3] = Token.CAT;
        shelf[5][4] = Token.BOOK;
        assertTrue(fourrows.isSatisfied(shelf));
    }
    @Test
    public void threeCase(){
        FourRows fourrows = new FourRows();
        Token [][]shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[0][0] = Token.TROPHY;
        shelf[0][1] = Token.TOY;
        shelf[0][2] = Token.CAT;
        shelf[0][3] = Token.FRAME;
        shelf[0][4] = Token.BOOK;
        shelf[1][0] = Token.CAT;
        shelf[1][1] = Token.CAT;
        shelf[1][2] = Token.CAT;
        shelf[1][3] = Token.CAT;
        shelf[1][4] = Token.CAT;
        shelf[2][0] = Token.TOY;
        shelf[2][1] = Token.TOY;
        shelf[2][2] = Token.TOY;
        shelf[2][3] = Token.TOY;
        shelf[2][4] = Token.TOY;
        shelf[3][0] = Token.PLANT;
        shelf[3][1] = Token.TROPHY;
        shelf[3][2] = Token.TOY;
        shelf[3][3] = Token.PLANT;
        shelf[3][4] = Token.PLANT;
        shelf[4][0] = Token.BOOK;
        shelf[4][1] = Token.PLANT;
        shelf[4][2] = Token.TROPHY;
        shelf[4][3] = Token.TROPHY;
        shelf[4][4] = Token.TOY;
        shelf[5][0] = Token.PLANT;
        shelf[5][1] = Token.PLANT;
        shelf[5][2] = Token.FRAME;
        shelf[5][3] = Token.CAT;
        shelf[5][4] = Token.BOOK;
        assertFalse(fourrows.isSatisfied(shelf));
    }

}