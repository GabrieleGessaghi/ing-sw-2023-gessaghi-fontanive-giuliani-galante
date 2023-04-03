package model.cards;

import model.Configurations;
import model.Token;
import model.cards.concreteobjectives.Corners;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.Configuration;

import static model.Configurations.ROWS_SHELF;
import static model.Configurations.COLUMNS_SHELF;
import static org.junit.jupiter.api.Assertions.*;

class CommonCardTest {
    @Test
    public void twoPlayers(){
        int numberOfPlayers = 2;
        int one,two,three;
        CommonObjective objective = new Corners();
        Card card = new CommonCard(objective,numberOfPlayers );
        Token[][] firstShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] secondShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] thirdShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++) {
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
        Token[][] firstShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] secondShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] thirdShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++) {
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
        Token[][] firstShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] secondShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] thirdShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        Token[][] fourthShelf = new Token[ROWS_SHELF][COLUMNS_SHELF];
        for(int i = 0; i < ROWS_SHELF; i++)
            for(int j = 0; j < COLUMNS_SHELF; j++) {
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