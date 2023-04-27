package controller;

import com.google.gson.stream.JsonReader;
import controller.observer.Event;
import controller.observer.Observer;
import model.Game;
import view.socket.ClientHandlerSocket;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    private Map<Integer, ClientHandlerSocket> clientHandlerIndexes;

    public Controller() {
        creationController = new CreationController();
        clientHandlers = new ArrayList<>();
        clientHandlerIndexes = new HashMap<>();
        isGameRunning = false;
    }

    @Override
    public void run() {
        while (true) {
            if (isGameRunning) {

            }
        }
    }

    @Override
    public void update(Event event) {
//        String jsonMessage = event.getJsonMessage();
//        String field;
//        JsonReader jsonReader;
//        try {
//            jsonReader = new JsonReader(new StringReader(jsonMessage));
//            jsonReader.beginObject();
//            while(jsonReader.hasNext()) {
//                field = jsonReader.nextName();
//                switch (field) {
//                    //PARSE JSON
//                }
//            }
//            jsonReader.endObject();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        if (!isGameRunning && creationController.isGameReady()) {
            Game game = creationController.createGame();
            turnController = new TurnController(game);
            isGameRunning = true;
        }
    }

    public void addClient(int index, ClientHandlerSocket clientHandler) {
        clientHandlers.add(clientHandler);
        clientHandlerIndexes.put(index, clientHandler);
    }

    private void reset() {
        //TODO: Reset tutto
    }
}
