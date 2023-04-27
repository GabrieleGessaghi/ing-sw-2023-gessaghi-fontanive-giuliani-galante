package controller;

import controller.observer.Event;
import controller.observer.Observer;
import model.Game;
import view.socket.ClientHandlerSocket;

import java.util.ArrayList;
import java.util.Map;

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
    private Map<String, ClientHandlerSocket> clientHandlersNicknames;

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

        //PARSE JSON

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
