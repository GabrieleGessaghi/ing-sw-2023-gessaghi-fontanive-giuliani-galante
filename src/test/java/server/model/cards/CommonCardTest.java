package server.model.cards;

import server.controller.utilities.ConfigLoader;
import server.model.Token;
import server.model.cards.Card;
import server.model.cards.CommonCard;
import server.model.cards.CommonObjective;
import server.model.cards.concreteobjectives.Corners;
import org.junit.jupiter.api.Test;

import static server.controller.utilities.ConfigLoader.SHELF_ROWS;
import static server.controller.utilities.ConfigLoader.SHELF_COLUMNS;
import static org.junit.jupiter.api.Assertions.*;

class CommonCardTest {
    @Test
    public void twoPlayers(){
        int numberOfPlayers = 2;
        int one,two,three;
        Card card = new CommonCard(CommonType.CORNERS, numberOfPlayers);
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

        ConfigLoader conf= new ConfigLoader();
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
        Card card = new CommonCard(CommonType.CORNERS,numberOfPlayers );
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

        ConfigLoader conf= new ConfigLoader();
        one = card.getPoints(firstShelf);
        two = card.getPoints(secondShelf);
        three = card.getPoints(thirdShelf);
        assertEquals(8,one);
        assertEquals(6,two);
        assertEquals(4,three);



    }

    @Test
    public void fourPlayers(){
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");

        int numberOfPlayers = 4;
        int one,two,three,four;
        Card card = new CommonCard(CommonType.CORNERS,numberOfPlayers );
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


        one = card.getPoints(firstShelf);
        two = card.getPoints(secondShelf);
        three = card.getPoints(thirdShelf);
        four = card.getPoints(fourthShelf);
        assertEquals(8,one);
        assertEquals(6,two);
        assertEquals(4,three);
        assertEquals(2,four);



    }

}