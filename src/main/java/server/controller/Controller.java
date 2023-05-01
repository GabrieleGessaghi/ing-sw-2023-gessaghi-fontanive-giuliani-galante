package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.model.Game;
import server.view.ClientHandlerSocket;
import server.view.ClientHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the game flow on a different thread.
 * @author Giorgio Massimo Fontanive
 */
public class Controller extends Thread implements Observer {
    private boolean isGameRunning;
    private boolean waitingForTiles;
    private boolean waitingForColumn;
    private CreationController creationController;
    private TurnController turnController;
    private List<ClientHandlerSocket> clientHandlers;
    private Map<ClientHandlerSocket, Integer> clientHandlerIndexes;
    private ClientHandlerSocket currentClient;

    public Controller() {
        reset();
    }

    @Override
    public void run() {
        //TODO: Add wait/notify
        //TODO: Add observers

    }

    @Override
    public void update(Event event) {
        boolean correctClient = false;
        boolean receivedTiles = false;
        boolean receivedColumn = false;
        String jsonMessage = event.getJsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "tilesSelection" -> receivedTiles = true;
                    case "column" -> receivedColumn = true;
                    case "clientIndex" -> {
                        if(jsonReader.nextInt() == clientHandlerIndexes.get(currentClient)) correctClient = true;
                    }
                }
            }
            jsonReader.endObject();
            waitingForTiles = !(receivedTiles && correctClient);
            waitingForColumn = !(receivedColumn && correctClient);
            //NOTIFY ALL
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        startGame();
    }

    public void addClient(int index, ClientHandlerSocket clientHandler) {
        clientHandlerIndexes.put(clientHandler, index);
        clientHandlers.add(clientHandler);
        if (clientHandlers.size() == 1)
            clientHandler.requestInput(Prompt.PLAYERSNUMBER);
    }

    private void startGame() {
        if (!isGameRunning && creationController.isGameReady()) {
            Game game = creationController.createGame();
            turnController = new TurnController(game);
            isGameRunning = true;
        }
    }

    /**
     * Resets the object to initial state.
     * @author Giorgio Massimo Fontanive
     */
    private void reset() {
        isGameRunning = false;
        turnController = null;
        creationController = new CreationController();
        clientHandlers = new ArrayList<>();
        clientHandlerIndexes = new HashMap<>();
        waitingForTiles = false;
        waitingForColumn = false;
    }
}
