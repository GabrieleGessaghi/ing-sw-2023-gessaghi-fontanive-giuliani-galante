package controller;

import com.google.gson.stream.JsonReader;
import controller.observer.Event;
import controller.observer.Observer;
import model.Game;
import view.socket.ClientHandlerSocket;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;

import static controller.Configurations.readMatrix;

/**
 * Handles the game flow on a different thread.
 *
 */
public class Controller extends Thread implements Observer {
    private boolean isGameRunning;
    private int playersNumber;
    private CreationController creationController;
    private TurnController turnController;
    private ArrayList<ClientHandlerSocket> clientHandlers;

    public Controller() {
        creationController = new CreationController();
        clientHandlers = new ArrayList<>();
        isGameRunning = false;
    }

    @Override
    public void run() {
        while (true) {

            //HANDLE TURNS

        }
    }

    @Override
    public void update(Event event) {
        String jsonMessage = event.getJsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    //PARSE JSON
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!isGameRunning && creationController.isGameReady()) {
            Game game = creationController.createGame();
            turnController = new TurnController(game);
            isGameRunning = true;
        }
    }

    public void addClient(ClientHandlerSocket clientHandler) {
        clientHandlers.add(clientHandler);
    }
}
