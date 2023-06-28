package server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.utilities.ConfigLoader;
import server.model.cards.CommonCard;
import server.model.exceptions.IllegalColumnException;
import server.model.exceptions.IllegalMoveException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static server.controller.utilities.ConfigLoader.BOARD_SIZE;
import static server.model.Game.isThereGameSaved;

public class GameTest {
    Game testGame;
    @BeforeEach
    public void init() throws IllegalColumnException, IllegalMoveException {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        ArrayList<String> nicknames = new ArrayList<>();
        int[][] selection = new int[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++)
            for(int j = 0; j < BOARD_SIZE; j++)
                selection[i][j] = -1;

        selection[1][3] = 1;
        selection[1][4] = 1;

        nicknames.add("Player1");
        nicknames.add("Player2");
        testGame = new Game(2, nicknames);
        testGame.playerTurn(selection, 1);
    }

    @AfterEach
    public void teardown(){ testGame = null; };

    @Test
    public void generateCommonCardsTest(){
        CommonCard[] commonCardsTest = new CommonCard[testGame.getCommonCards().length];
        assert(commonCardsTest.length != 0);
    }

    @Test
    public void isThereGameSavedTest(){
        boolean test = isThereGameSaved();
        assertTrue(test);
    }
}
