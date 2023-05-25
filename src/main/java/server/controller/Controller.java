package server.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.model.View;
import server.view.ClientHandler;

import java.util.*;

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
    private ChatController chatController;
    private RequestController requestController;
    private Game game;

    //TODO: Handle game loading if save found
    //TODO: Lonely player timer
    //TODO: Check if client disconnected during own turn
    //TODO: Handle all clients disconnecting

    public Controller() {
        reset();
    }

    /**
     * Manages turns by sending requests and updates to client handlers.
     */
    @Override
    public void run() {
        while(true) {

            //Waits for the game to be running
            synchronized (this) {
                while (!isGameRunning)
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Error while waiting for the game to start.");
                    }
            }
            startGame();

            //Start managing turns
            game.sendState(View.PLAYER_NICKNAMES);
            game.sendState(View.COMMON_CARDS);
            while (!game.gameOver()) {
                game.sendState(View.BOARD);
                game.sendState(View.CURRENT_PLAYER);
                ClientHandler currentClient = findClientHandlerByName(game.getCurrentPlayer());
                if (currentClient != null) {
                    currentClient.sendOutput(game.getView(View.PERSONAL_CARD, currentClient.nickname).toString());
                    currentClient.sendOutput(game.getView(View.SHELF, currentClient.nickname).toString());
                    turnController = new TurnController(game, currentClient);
                    currentClient.registerObserver(turnController);
                    turnController.newTurn();
                    currentClient.sendOutput(game.getView(View.SHELF, currentClient.nickname).toString());
                    currentClient.sendOutput(game.getView(View.POINTS, currentClient.nickname).toString());
                }
            }

            //Makes everyone disconnect after the game
            for (ClientHandler ch : clientHandlers) {
                ch.sendOutput(JsonTools.createMessage("Log back in if you want to play again.", false));
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
        index = jsonObject.get("index").getAsInt();
        ClientHandler clientHandler = findClientHandler(index);
        if (clientHandler != null)
            if (jsonObject.has("clientDisconnected")) {
                new Thread(() -> handleDisconnection(clientHandler)).start();
            } else if (jsonObject.has("clientReconnected")) {
                disconnectedClients.remove(clientHandler.nickname);
                game.registerObserver(clientHandler);
                game.setPlayerConnection(clientHandler.nickname, true);
                clientHandler.registerObserver(chatController);
                clientHandler.registerObserver(requestController);
            }

        //Checks whether it can start a new game
        if (!isGameRunning)
            synchronized (this) {
                if (creationController.isGameReady()) {
                    isGameRunning = true;
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
            if (clientHandlers.size() == 1)
                clientHandler.requestInput(Prompt.PLAYERSNUMBER);
        } else {
            clientHandler.sendOutput(JsonTools.createMessage("The game is full, please exit!", true));
            clientHandler.disconnect();
        }
    }

    /**
     *
     */
    private void startGame() {
        game = creationController.createGame();
        chatController = new ChatController();
        requestController = new RequestController(game);
        for (ClientHandler clientHandler : clientHandlers) {
            game.registerObserver(clientHandler);
            clientHandler.sendOutput(JsonTools.createMessage("The game is starting!", false));
            clientHandler.registerObserver(chatController);
            clientHandler.registerObserver(requestController);
        }
    }

    /**
     *
     */
    private void handleDisconnection(ClientHandler clientHandler) {
        disconnectedClients.put(clientHandler.nickname, clientHandler.index);
        clientHandlers.remove(clientHandler);

        //Resets the game if someone disconnects during game creation
        if (!isGameRunning) {
            for (ClientHandler clientHandler1 : clientHandlers) {
                clientHandler1.sendOutput(JsonTools.createMessage("One player disconnected, restarting the login process.", true));
                clientHandler1.disconnect();
            }
            reset();
        }

        //Checks if there's no client left
        else if (clientHandlers.size() == 0)
            reset();

            //Checks if there's only one client left
        else if (clientHandlers.size() == 1)
        {}

        //Checks if it was their turn
        else if (game.getCurrentPlayer().equals(clientHandler.nickname))
        {}

        game.setPlayerConnection(clientHandler.nickname, false);
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
