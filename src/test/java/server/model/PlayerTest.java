package server.model;

import server.controller.utilities.ConfigLoader;
import server.model.Player;
import server.model.Token;
import server.model.cards.CommonCard;
import server.model.cards.CommonObjective;
import server.model.cards.CommonType;
import server.model.cards.PersonalCard;
import server.model.cards.concreteobjectives.Corners;
import server.model.cards.concreteobjectives.Diagonal;
import server.model.exceptions.FullColumnException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class PlayerTest {
    @Test
    public void insertTokensTestOne() throws FullColumnException {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");

        List<CommonCard> commonCards = new ArrayList<>(2);

        CommonCard common1 = new CommonCard(CommonType.CORNERS, 2);
        CommonCard common2 = new CommonCard(CommonType.CORNERS, 2);

        commonCards.add(0, common1);
        commonCards.add(1, common2);

        Player testPlayer = new Player("nick", false, 1, commonCards);

        Token[] testFill = new Token[6];

        for(int i=0; i<6; i++)
            testFill[i] = Token.CAT;

        testPlayer.insertTokens(testFill, 0);
        System.out.println("Player points:" + testPlayer.getPoints());

        assertEquals(testPlayer.getPoints(), 8);
    }


    @Test
    public void insertTokensTestTwo() throws FullColumnException {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");

        List<CommonCard> commonCards = new ArrayList<>(2);

        CommonCard common1 = new CommonCard(CommonType.CORNERS, 2);
        CommonCard common2 = new CommonCard(CommonType.DIAGONAL, 2);

        commonCards.add(0, common1);
        commonCards.add(1, common2);

        Player testPlayer = new Player("nick", false, 1, commonCards);

        Token[] testFill = new Token[6];

        for(int i=0; i<6; i++)
            testFill[i] = Token.CAT;

        testPlayer.insertTokens(testFill, 0);
        testPlayer.insertTokens(testFill, 4);
        System.out.println("Player points:" + testPlayer.getPoints());

        assertEquals(testPlayer.getPoints(), 25);
    }
}
