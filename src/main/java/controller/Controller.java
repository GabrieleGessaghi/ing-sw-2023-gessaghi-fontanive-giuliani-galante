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
import java.util.List;
import java.util.Map;

import static controller.Configurations.readMatrix;

/**
 * Handles the game flow on a different thread.
 * @author Giorgio Massimo Fontanive
 */
public class Controller extends Thread implements Observer {
    private boolean isGameRunning;
    private boolean isWaitingForInput;
    private CreationController creationController;
    private TurnController turnController;
    private List<ClientHandlerSocket> clientHandlers;
    private Map<ClientHandlerSocket, Integer> clientHandlerIndexes;
    private ClientHandlerSocket currentClient;

    public Controller() {
        creationController = new CreationController();
        clientHandlers = new ArrayList<>();
        clientHandlerIndexes = new HashMap<>();
        isGameRunning = false;
        isWaitingForInput = false;
    }

    @Override
    public void run() {
        currentClient = clientHandlers.get(0);
        int clientsListIndex = 0;
        while (true) {
            if (isGameRunning) {

                //Gets input from the player
                currentClient = clientHandlers.get(clientsListIndex);
                try {
                    currentClient.requestInput(Prompt.TOKENS);
                    isWaitingForInput = true;
                    //WAIT INPUT
                    currentClient.requestInput(Prompt.COLUMN);
                    isWaitingForInput = true;
                    //WAIT INPUT
                    clientsListIndex++;
                } catch (Exception e) {
                    currentClient.showOutput("\"errorMessage\":\"Generic error!\"");
                }

                //CHECK IF GAME IS OVER
                    //RESET EVERYTHING
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

        startGame();
    }

    public void addClient(int index, ClientHandlerSocket clientHandler) {
        clientHandlerIndexes.put(clientHandler, index);
        clientHandlers.add(clientHandler);
    }

    private void startGame() {
        if (!isGameRunning && creationController.isGameReady()) {
            Game game = creationController.createGame();
            turnController = new TurnController(game);
            isGameRunning = true;
        }
    }

    private void reset() {
        isGameRunning = false;
        //TODO: Reset tutto
    }
}
