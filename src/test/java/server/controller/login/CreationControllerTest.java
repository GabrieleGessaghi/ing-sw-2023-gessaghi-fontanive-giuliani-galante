package server.controller.login;

import org.junit.jupiter.api.BeforeEach;
import server.controller.observer.Event;
import server.controller.utilities.ConfigLoader;

import static org.junit.jupiter.api.Assertions.*;

class CreationControllerTest {
    CreationController creationController;

    @BeforeEach
    public void init(){
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        creationController = new CreationController();
        String player1Name = "Player1";
        String player2Name = "Player2";
        String eventString = "eventString";
        Event event = new Event(eventString);
        creationController.update(event);

    }


}