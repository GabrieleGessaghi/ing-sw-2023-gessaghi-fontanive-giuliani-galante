package model.cards.concreteobjectives;

import model.Configurations;
import model.Token;
import org.junit.jupiter.api.Test;

import static model.Configurations.SHELF_COLUMNS;
import static model.Configurations.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class SixGroupsTest {
    @Test
    public void sixCase(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        SixGroups sixgroups = new SixGroups();
        Token[][] shelf= new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[0][0] = Token.TROPHY;
        shelf[0][1] = Token.TROPHY;
        shelf[0][2] = Token.CAT;
        shelf[0][3] = Token.FRAME;
        shelf[0][4] = Token.BOOK;
        shelf[1][0] = Token.CAT;
        shelf[1][1] = Token.CAT;
        shelf[1][2] = Token.CAT;
        shelf[1][3] = Token.BOOK;
        shelf[1][4] = Token.CAT;
        shelf[2][0] = Token.TOY;
        shelf[2][1] = Token.TOY;
        shelf[2][2] = Token.BOOK;
        shelf[2][3] = Token.TOY;
        shelf[2][4] = Token.TOY;
        shelf[3][0] = Token.TOY;
        shelf[3][1] = Token.TROPHY;
        shelf[3][2] = Token.TOY;
        shelf[3][3] = Token.PLANT;
        shelf[3][4] = Token.BOOK;
        shelf[4][0] = Token.BOOK;
        shelf[4][1] = Token.PLANT;
        shelf[4][2] = Token.TROPHY;
        shelf[4][3] = Token.TROPHY;
        shelf[4][4] = Token.TOY;
        shelf[5][0] = Token.PLANT;
        shelf[5][1] = Token.BOOK;
        shelf[5][2] = Token.FRAME;
        shelf[5][3] = Token.CAT;
        shelf[5][4] = Token.BOOK;
        assertTrue(sixgroups.isSatisfied(shelf));
    }
    @Test
    public void fiveCase(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        SixGroups sixgroups = new SixGroups();
        Token[][] shelf= new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[0][0] = Token.TROPHY;
        shelf[0][1] = Token.TROPHY;
        shelf[0][2] = Token.CAT;
        shelf[0][3] = Token.NOTHING;
        shelf[0][4] = Token.NOTHING;
        shelf[1][0] = Token.CAT;
        shelf[1][1] = Token.CAT;
        shelf[1][2] = Token.CAT;
        shelf[1][3] = Token.BOOK;
        shelf[1][4] = Token.CAT;
        shelf[2][0] = Token.TOY;
        shelf[2][1] = Token.TOY;
        shelf[2][2] = Token.BOOK;
        shelf[2][3] = Token.TOY;
        shelf[2][4] = Token.TOY;
        shelf[3][0] = Token.TOY;
        shelf[3][1] = Token.TROPHY;
        shelf[3][2] = Token.TOY;
        shelf[3][3] = Token.PLANT;
        shelf[3][4] = Token.BOOK;
        shelf[4][0] = Token.BOOK;
        shelf[4][1] = Token.PLANT;
        shelf[4][2] = Token.TROPHY;
        shelf[4][3] = Token.FRAME;
        shelf[4][4] = Token.TOY;
        shelf[5][0] = Token.PLANT;
        shelf[5][1] = Token.BOOK;
        shelf[5][2] = Token.FRAME;
        shelf[5][3] = Token.CAT;
        shelf[5][4] = Token.BOOK;
        assertFalse(sixgroups.isSatisfied(shelf));
    }

}