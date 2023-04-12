package model.cards;

import controller.Configurations;
import model.Token;
import model.cards.concreteobjectives.Corners;
import org.junit.jupiter.api.Test;

import static controller.Configurations.SHELF_ROWS;
import static controller.Configurations.SHELF_COLUMNS;
import static org.junit.jupiter.api.Assertions.*;

class CommonCardTest {
    @Test
    public void twoPlayers(){
        int numberOfPlayers = 2;
        int one,two,three;
        CommonObjective objective = new Corners();
        Card card = new CommonCard(objective,numberOfPlayers );
        Token[][] firstShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] secondShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] thirdShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++) {
                firstShelf[i][j] = Token.NOTHING;
                secondShelf[i][j] = Token.NOTHING;
                thirdShelf[i][j] = Token.NOTHING;
            }
        firstShelf[0][0] = Token.CAT;
        firstShelf[5][0] = Token.CAT;
        firstShelf[0][4] = Token.CAT;
        firstShelf[5][4] = Token.CAT;
        secondShelf[0][0] = Token.CAT;
        secondShelf[5][0] = Token.CAT;
        secondShelf[0][4] = Token.CAT;
        secondShelf[5][4] = Token.BOOK;
        thirdShelf[0][0] = Token.CAT;
        thirdShelf[5][0] = Token.CAT;
        thirdShelf[0][4] = Token.CAT;
        thirdShelf[5][4] = Token.CAT;

        Configurations conf= new Configurations();
        one = card.getPoints(firstShelf);
        two = card.getPoints(secondShelf);
        three = card.getPoints(thirdShelf);
        assertEquals(8,one);
        assertEquals(0,two);
        assertEquals(4,three);



    }
    @Test
    public void threePlayers(){
        int numberOfPlayers = 3;
        int one,two,three;
        CommonObjective objective = new Corners();
        Card card = new CommonCard(objective,numberOfPlayers );
        Token[][] firstShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] secondShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] thirdShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++) {
                firstShelf[i][j] = Token.NOTHING;
                secondShelf[i][j] = Token.NOTHING;
                thirdShelf[i][j] = Token.NOTHING;
            }
        firstShelf[0][0] = Token.CAT;
        firstShelf[5][0] = Token.CAT;
        firstShelf[0][4] = Token.CAT;
        firstShelf[5][4] = Token.CAT;
        secondShelf[0][0] = Token.CAT;
        secondShelf[5][0] = Token.CAT;
        secondShelf[0][4] = Token.CAT;
        secondShelf[5][4] = Token.CAT;
        thirdShelf[0][0] = Token.CAT;
        thirdShelf[5][0] = Token.CAT;
        thirdShelf[0][4] = Token.CAT;
        thirdShelf[5][4] = Token.CAT;

        Configurations conf= new Configurations();
        one = card.getPoints(firstShelf);
        two = card.getPoints(secondShelf);
        three = card.getPoints(thirdShelf);
        assertEquals(8,one);
        assertEquals(6,two);
        assertEquals(4,three);



    }

    @Test
    public void fourPlayers(){
        int numberOfPlayers = 4;
        int one,two,three,four;
        CommonObjective objective = new Corners();
        Card card = new CommonCard(objective,numberOfPlayers );
        Token[][] firstShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] secondShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] thirdShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        Token[][] fourthShelf = new Token[SHELF_ROWS][SHELF_COLUMNS];
        for(int i = 0; i < SHELF_ROWS; i++)
            for(int j = 0; j < SHELF_COLUMNS; j++) {
                firstShelf[i][j] = Token.NOTHING;
                secondShelf[i][j] = Token.NOTHING;
                thirdShelf[i][j] = Token.NOTHING;
            }
        firstShelf[0][0] = Token.CAT;
        firstShelf[5][0] = Token.CAT;
        firstShelf[0][4] = Token.CAT;
        firstShelf[5][4] = Token.CAT;
        secondShelf[0][0] = Token.CAT;
        secondShelf[5][0] = Token.CAT;
        secondShelf[0][4] = Token.CAT;
        secondShelf[5][4] = Token.CAT;
        thirdShelf[0][0] = Token.CAT;
        thirdShelf[5][0] = Token.CAT;
        thirdShelf[0][4] = Token.CAT;
        thirdShelf[5][4] = Token.CAT;
        fourthShelf[0][0] = Token.CAT;
        fourthShelf[5][0] = Token.CAT;
        fourthShelf[0][4] = Token.CAT;
        fourthShelf[5][4] = Token.CAT;

        Configurations conf= new Configurations();
        one = card.getPoints(firstShelf);
        two = card.getPoints(secondShelf);
        three = card.getPoints(thirdShelf);
        four = card.getPoints(thirdShelf);
        assertEquals(8,one);
        assertEquals(6,two);
        assertEquals(4,three);
        assertEquals(2,four);



    }

}