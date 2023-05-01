package server.controller;

import server.controller.observer.Event;
import server.controller.observer.Observer;
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
        //TODO: Add wait/notify
        //TODO: Add observers
        isGameRunning = false;
        creationController = new CreationController();
        ListIterator<ClientHandler> listIterator = clientHandlers.listIterator();
        while(true) {
            if (isGameRunning) {
                if (listIterator.hasNext())
                    currentClient = listIterator.next();
                turnController = new TurnController(game, currentClient);
            }
        }
    }

    @Override
    public void update(Event event) {
        if (!isGameRunning)
            if (creationController.isGameReady()) {
                game = creationController.createGame();
                isGameRunning = true;
            }
    }

    public void addClient(int index, ClientHandler clientHandler) {
        if (!isGameRunning && creationController.isSpotAvailable()) {
            clientHandlers.add(clientHandler);
            if (clientHandlers.size() == 1)
                clientHandler.requestInput(Prompt.PLAYERSNUMBER);
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
    }
}
