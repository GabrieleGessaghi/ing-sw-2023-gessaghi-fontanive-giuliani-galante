package server.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.model.View;
import server.view.ClientHandler;

import java.io.FileNotFoundException;
import java.util.*;

//TODO: Handle only one client remaining

/**
 * Handles the game flow on a different thread.
 * @author Giorgio Massimo Fontanive
 */
public class Controller implements Observer, Runnable {
    public static List<ClientHandler> clientHandlers;
    public static Map<String, Integer> disconnectedClients;
    private boolean isGameRunning;
    private CreationController creationController;
    private TurnController turnController;
    private Game game;

    //TODO: Make initial information be requested

    public Controller() {
        reset();
    }

    /**
     * Manages turns by sending requests and updates to client handlers.
     */
    @Override
    public void run() {
        disconnectedClients = new HashMap<>();
        while(true) {
            synchronized (this) {

                //Waits for the game to be running
                while (!isGameRunning)
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Error while waiting for the game to start.");
                    }
            }

            //Instantiates the chat and the request controller
            ChatController chatController = new ChatController();
            RequestController requestController = new RequestController(game);
            for (ClientHandler c : clientHandlers) {
                c.registerObserver(chatController);
                c.registerObserver(requestController);
            }

            //Show initial information
            game.sendState(View.PLAYER_NICKNAMES);
            game.sendState(View.COMMON_CARDS);
            game.sendState(View.CURRENT_PLAYER);
            game.sendState(View.BOARD);

            //Manages turns
            while (!game.gameOver()) {

                //Checks if a client has reconnected
                for (ClientHandler clientHandler : clientHandlers)
                    if (!game.getPlayerConnection(clientHandler.nickname)) {
                        game.registerObserver(clientHandler);
                        game.setPlayerConnection(clientHandler.nickname, true);
                        clientHandler.registerObserver(chatController);
                        clientHandler.registerObserver(requestController);
                    }

                //TODO: If there's only one client left

                ClientHandler currentClient = findClientHandlerByName(game.getCurrentPlayer());
                if (currentClient != null) {
                    turnController = new TurnController(game, currentClient);
                    currentClient.registerObserver(turnController);
                    turnController.newTurn();
                }
            }

            //Makes everyone disconnect after the game
            for (ClientHandler ch : clientHandlers) {
                ch.sendOutput(JsonTools.createMessage("Closing the game!"));
                ch.disconnect();
            }
            reset();
        }
    }

    @Override
    public void update(Event event) {

        //Finds if a client has disconnected
        int index;
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        if (jsonObject.has("clientDisconnected")) {
            index = jsonObject.get("clientDisconnected").getAsInt();
            ClientHandler clientHandler = findClientHandler(index);
            if (clientHandler != null) {
                System.out.println("Detected player disconnection!");
                disconnectedClients.put(clientHandler.nickname, index);
                game.setPlayerConnection(clientHandler.nickname, false);
                clientHandlers.remove(clientHandler);
            }

            //Reset the game if someone disconnects during game creation
            if (!isGameRunning) {
                for (ClientHandler ch : clientHandlers) {
                    ch.sendOutput(JsonTools.createMessage("One player disconnected, closing game."));
                    ch.disconnect();
                }
                reset();
            }
        }

        if (!isGameRunning) {
            synchronized (this) {

                //Checks whether it can start a new game
                if (creationController.isGameReady()) {
                    game = creationController.createGame();

                    //Checks whether there was a previously saved game
                    if (Game.isThereGameSaved()) {
                        try {
                            game.loadGame();
                        } catch (FileNotFoundException e) {
                            System.out.println("Error while loading game!");
                        }
                    }
                    isGameRunning = true;
                    for (ClientHandler clientHandler : clientHandlers) {
                        game.registerObserver(clientHandler);
                        clientHandler.sendOutput(JsonTools.createMessage("The game is starting!"));
                    }
                }
                this.notifyAll();
            }
        }
    }

    /**
     * Adds a new client and starts their own ClientHandler thread
     * @param clientHandler The client handler that just connected to the server.
     * @author Giorgio Massimo Fontanive
     */
    public synchronized void addClient(ClientHandler clientHandler) {
        if (creationController.isSpotAvailable() || !disconnectedClients.isEmpty()) {
            clientHandlers.add(clientHandler);
            clientHandler.registerObserver(creationController);
            clientHandler.registerObserver(this);
            clientHandler.requestInput(Prompt.NICKNAME);

            //Requests the players number if it's the first player added
            if (clientHandlers.size() == 1) {
                while (clientHandlers.get(0).nickname == null)
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Error when waiting for player's number.");
                    }
                clientHandler.requestInput(Prompt.PLAYERSNUMBER);
            }
        } else
            clientHandler.sendOutput(JsonTools.createMessage("The game is full, please exit!"));
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
        disconnectedClients = new HashMap<>();
    }

    /**
     * Finds a client handler by its index
     * @param index The wanted client handler's index.
     * @return A reference to the requested client handler or null.
     * @author Giorgio Massimo Fontanive
     */
    public static ClientHandler findClientHandler(int index) {
        for (ClientHandler clientHandler : clientHandlers)
            if (clientHandler.index == index)
                return clientHandler;
        return null;
    }

    /**
     * Finds the client handler with this nickname.
     * @param nickname The nickname of the wanted client handler.
     * @return The client handler with the given nickname.
     */
    public static ClientHandler findClientHandlerByName(String nickname) {
        for (ClientHandler clientHandler : clientHandlers)
            if (clientHandler.nickname.equals(nickname))
                return clientHandler;
        return null;
    }
}
