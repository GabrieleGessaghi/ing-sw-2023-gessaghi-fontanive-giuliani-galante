package model.cards.concreteobjectives;

import controller.Configurations;
import model.Token;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static controller.Configurations.SHELF_COLUMNS;
import static controller.Configurations.SHELF_ROWS;
import static org.junit.jupiter.api.Assertions.*;

class CornersTest {

    @Test
    void trueScenario(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        var firstCorner = new Corners();
        Token[][] shelf;
        Token type;

        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];

        Random randomType = new Random();
        type=Token.values()[randomType.nextInt(Token.values().length)];

        for(int i = 0; i < SHELF_ROWS -1; i++){
            for(int j = 0; j < SHELF_COLUMNS -1; j++) {
                Random random = new Random();
                shelf[i][j]=Token.values()[random.nextInt(Token.values().length)];
            }
        }
        shelf[0][0] = type;
        shelf[0][SHELF_COLUMNS -1] = type;
        shelf[SHELF_ROWS -1][0] = type;
        shelf[SHELF_ROWS -1][SHELF_COLUMNS -1] = type;

        assertEquals(true,firstCorner.isSatisfied(shelf));
    }
    @Test
    void WrongScenario(){
        Configurations.loadConfiguration("src/main/resources/configuration.json");
        var firstCorner = new Corners();
        Token[][] shelf;


        shelf = new Token[SHELF_ROWS][SHELF_COLUMNS];


        for(int i = 0; i < SHELF_ROWS; i++){
            for(int j = 0; j < SHELF_COLUMNS; j++) {
                Random random = new Random();
                shelf[i][j]=Token.values()[random.nextInt(Token.values().length)];
            }
        }
        shelf[0][0]=Token.CAT;
        shelf[5][0]=Token.BOOK;
        assertEquals(false,firstCorner.isSatisfied(shelf));
    }


}