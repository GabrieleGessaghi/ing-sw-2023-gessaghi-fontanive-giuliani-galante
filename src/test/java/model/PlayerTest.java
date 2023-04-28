package model;

import controller.utilities.ConfigLoader;
import model.cards.*;
import model.cards.concreteobjectives.*;
import model.exceptions.FullColumnException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class PlayerTest {
    @Test
    public void insertTokensTestOne() throws FullColumnException {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        PersonalCard pc = new PersonalCard(1);

        CommonObjective obj1 = new Corners();
        CommonObjective obj2 = new Corners();
        List<CommonCard> commonCards = new ArrayList<>(2);

        CommonCard common1 = new CommonCard(obj1, 2);
        CommonCard common2 = new CommonCard(obj2, 2);

        commonCards.add(0, common1);
        commonCards.add(1, common2);

        Player testPlayer = new Player("nick", false, pc, commonCards);

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
        PersonalCard pc = new PersonalCard(1);

        CommonObjective obj1 = new Corners();
        CommonObjective obj2 = new Diagonal();
        List<CommonCard> commonCards = new ArrayList<>(2);

        CommonCard common1 = new CommonCard(obj1, 2);
        CommonCard common2 = new CommonCard(obj2, 2);

        commonCards.add(0, common1);
        commonCards.add(1, common2);

        Player testPlayer = new Player("nick", false, pc, commonCards);

        Token[] testFill = new Token[6];

        for(int i=0; i<6; i++)
            testFill[i] = Token.CAT;

        testPlayer.insertTokens(testFill, 0);
        testPlayer.insertTokens(testFill, 4);
        System.out.println("Player points:" + testPlayer.getPoints());

        assertEquals(testPlayer.getPoints(), 25);
    }
}
