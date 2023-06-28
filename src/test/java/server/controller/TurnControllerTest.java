package server.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.utilities.ConfigLoader;
import server.model.Game;
import server.view.ClientHandler;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TurnControllerTest {
    TurnController turnController;

    @BeforeEach
    public void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
    }


    @AfterEach
    public void teardown() {
        turnController = null;
    }

    @Test
    public void constructorTest(){
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Name1");
        nicknames.add("Name2");
        Game testGame = new Game(2, nicknames);
        ClientHandler clientHandler = new ClientHandler() {
            @Override
            public void requestInput(Prompt prompt) {

            }

            @Override
            public void sendOutput(String jsonMessage) {

            }
        };
        turnController = new TurnController(testGame, clientHandler);
        assertNotEquals(turnController, null);
    }
}