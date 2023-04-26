package controller;

import controller.observer.Event;
import controller.observer.Observer;
import model.Game;
import view.ClientHandler;
import view.socket.ClientHandlerSocket;

import java.util.ArrayList;

/**
 * Handles the game flow on a different thread.
 *
 */
public class Controller extends Thread implements Observer {
    private boolean isGameRunning;
    private CreationController creationController;
    private TurnController turnController;
    private ArrayList<ClientHandlerSocket> clientHandlers;

    public Controller() {
        creationController = new CreationController();
        clientHandlers = new ArrayList<>();
        isGameRunning = false;
    }

    public void addClient(ClientHandlerSocket clientHandler) {
        clientHandlers.add(clientHandler);
    }

    @Override
    public void run() {
        while (true) {

        }
    }

    @Override
    public void update(Event event) {
        if (creationController.getIsGameReady()) {
            Game game = creationController.createGame();
            turnController = new TurnController(game);
            isGameRunning = true;
        }
    }
}
