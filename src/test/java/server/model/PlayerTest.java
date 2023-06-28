package server.model;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.controller.utilities.ConfigLoader;
import server.model.cards.CommonCard;
import server.model.cards.CommonType;
import server.model.exceptions.IllegalColumnException;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    Player testPlayer;
    Player testPlayer2;

    @BeforeEach
    public void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");

        List<CommonCard> commonCards = new ArrayList<>(2);

        CommonCard common1 = new CommonCard(CommonType.CORNERS, 2, 0);
        CommonCard common2 = new CommonCard(CommonType.DIAGONAL, 2, 1);

        commonCards.add(0, common1);
        commonCards.add(1, common2);

        testPlayer = new Player("nick", false, 1, commonCards);
        testPlayer2 = new Player(testPlayer.getState().toString(), commonCards);
    }

    @AfterEach
    public void teardown() {
        testPlayer = null;
    }

    @Test
    public void insertTokensTestOne() throws IllegalColumnException {
        Token[] testFill = new Token[6];

        for(int i=0; i<6; i++)
            testFill[i] = Token.CAT;

        testPlayer.insertTokens(testFill, 0);
        assertEquals(testPlayer.getPoints(), 8);
    }


    @Test
    public void insertTokensTestTwo() throws IllegalColumnException {
        Token[] testFill = new Token[6];

        for(int i=0; i<6; i++)
            testFill[i] = Token.CAT;

        testPlayer.insertTokens(testFill, 4);
        testPlayer.insertTokens(testFill, 0);

        assertEquals(testPlayer.getPoints(), 25);
    }

    @Test
    public void getStateTest(){
        JsonObject jsonObject = null;
        jsonObject = testPlayer.getState();
        assertNotEquals(jsonObject, null);
    }

}
