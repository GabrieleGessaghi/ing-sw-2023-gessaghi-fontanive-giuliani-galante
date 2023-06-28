package server.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.utilities.ConfigLoader;
import server.view.ClientHandler;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller controller;

    @BeforeEach
    public void init() {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        controller = new Controller();
    }


    @AfterEach
    public void teardown() {
        controller = null;
    }

    @Test
    public void addClientTest(){
        ClientHandler clientHandler = new ClientHandler() {
            @Override
            public void requestInput(Prompt prompt) {

            }

            @Override
            public void sendOutput(String jsonMessage) {

            }
        };
        controller.addClient(clientHandler);
    }

}