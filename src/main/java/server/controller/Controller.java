package server.controller;

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
    private boolean isGameRunning;
    private CreationController creationController;
    private TurnController turnController;
    private Game game;

    public Controller() {
        reset();
    }

    /**
     * Manages turns by sending requests and updates to client handlers.
     */
    @Override
    public synchronized void run() {
        int i = 0;
        while(true) {

            //Waits for the game to be running
            while (!isGameRunning)
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Error while waiting for the game to start.");
                }

            //Instantiates the chat and the request controller
            ChatController chatController = new ChatController(clientHandlers);
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
                if (i == clientHandlers.size())
                    i = 0;
                ClientHandler currentClient = clientHandlers.get(i);

                if (currentClient.isConnected()) {
                    turnController = new TurnController(game, currentClient);
                    currentClient.registerObserver(turnController);
                    turnController.newTurn();
                    i++;
                }
            }
            reset();
        }
    }

    @Override
    public void update(Event event) {
        if (!isGameRunning) {
            synchronized (this) {
                if (creationController.isGameReady()) {
                    game = creationController.createGame();
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
     * @authro Giorgio Massimo Fontanive
     */
    public synchronized void addClient(ClientHandler clientHandler) {
        if (!isGameRunning && creationController.isSpotAvailable()) {
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
}
