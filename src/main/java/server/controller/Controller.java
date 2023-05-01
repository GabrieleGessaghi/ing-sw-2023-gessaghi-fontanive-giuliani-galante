package server.controller;

import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.view.ClientHandlerSocket;
import server.view.ClientHandler;

import java.util.*;

/**
 * Handles the game flow on a different thread.
 * @author Giorgio Massimo Fontanive
 */
public class Controller extends Thread implements Observer {
    private boolean isGameRunning;
    private CreationController creationController;
    private TurnController turnController;
    private List<ClientHandler> clientHandlers;
    private ClientHandler currentClient;
    private Game game;

    public Controller() {
        reset();
    }

    @Override
    public void run() {
        isGameRunning = false;
        creationController = new CreationController();
        ListIterator<ClientHandler> listIterator = clientHandlers.listIterator();
        while(true) {
            if (isGameRunning) {
                if (!listIterator.hasNext())
                    listIterator = clientHandlers.listIterator();
                currentClient = listIterator.next();
                turnController = new TurnController(game, currentClient);
                currentClient.registerObserver(turnController);
                if (turnController.isGameOver())
                    reset();
            }
        }
    }

    @Override
    public void update(Event event) {
        if (!isGameRunning)
            if (creationController.isGameReady()) {
                game = creationController.createGame();
                isGameRunning = true;
                for (ClientHandler clientHandler : clientHandlers)
                    game.registerObserver(clientHandler);
            }
    }

    public void addClient(ClientHandler clientHandler) {
        if (!isGameRunning && creationController.isSpotAvailable()) {
            clientHandlers.add(clientHandler);
            clientHandler.registerObserver(this);
            clientHandler.registerObserver(creationController);
            if (clientHandlers.size() == 1)
                clientHandler.requestInput(Prompt.PLAYERSNUMBER);
        } else
            clientHandler.showOutput(JsonTools.createMessage("The game is full, please exit!"));
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
}
