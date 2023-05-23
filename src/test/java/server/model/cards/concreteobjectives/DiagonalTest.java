package server.model.cards.concreteobjectives;

import server.controller.utilities.ConfigLoader;
import server.model.Token;
import org.junit.jupiter.api.Test;
import server.model.cards.concreteobjectives.Diagonal;

import java.util.Random;

import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class DiagonalTest {

    @Test
    void leftDiagonalOne(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        var leftDiagonal = new Diagonal();
        Token[][] shelf;
        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[0][0]=Token.CAT;
        shelf[1][1]=Token.CAT;
        shelf[2][2]=Token.CAT;
        shelf[3][3]=Token.CAT;
        shelf[4][4]=Token.CAT;
        assertTrue(leftDiagonal.isSatisfied(shelf));
    }

    @Test
    void leftDiagonalTwo(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        var leftDiagonal = new Diagonal();
        Token[][] shelf;
        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[1][0]=Token.CAT;
        shelf[2][1]=Token.CAT;
        shelf[3][2]=Token.CAT;
        shelf[4][3]=Token.CAT;
        shelf[5][4]=Token.CAT;
        assertTrue(leftDiagonal.isSatisfied(shelf));
    }

    @Test
    void rightDiagonalOne(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        var rightDiagonal = new Diagonal();
        Token[][] shelf;
        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[0][4]=Token.CAT;
        shelf[1][3]=Token.CAT;
        shelf[2][2]=Token.CAT;
        shelf[3][1]=Token.CAT;
        shelf[4][0]=Token.CAT;
        assertTrue(rightDiagonal.isSatisfied(shelf));
    }
    @Test
    void rightDiagonalTwo(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        var rightDiagonal = new Diagonal();
        Token[][] shelf;
        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[1][4]=Token.CAT;
        shelf[2][3]=Token.CAT;
        shelf[3][2]=Token.CAT;
        shelf[4][1]=Token.CAT;
        shelf[5][0]=Token.CAT;
        assertTrue(rightDiagonal.isSatisfied(shelf));
    }

    @Test
    void wrongScenario(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        var wrong = new Diagonal();
        Token[][] shelf;
        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        shelf[4][2]=Token.PLANT;
        shelf[5][2]=Token.BOOK;
        assertFalse(wrong.isSatisfied(shelf));
    }
}