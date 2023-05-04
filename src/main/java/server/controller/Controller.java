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
    private boolean isGameRunning;
    private CreationController creationController;
    private TurnController turnController;
    private List<ClientHandler> clientHandlers;
    private Game game;

    public Controller() {
        reset();
    }

    @Override
    public synchronized void run() {
        isGameRunning = false;
        creationController = new CreationController();
        int i = 0;
        while(true) {

            //Waits for the game to be running
            while (!isGameRunning)
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            //Moves to the next player and begins a new turn
            if (i == clientHandlers.size())
                i = 0;
            ClientHandler currentClient = clientHandlers.get(i);
            turnController = new TurnController(game, currentClient);
            currentClient.registerObserver(turnController);
            i++;

            //Waits for the turn to be finished
            while (!turnController.getIsTurnOver())
                try {
                    turnController.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            if (game.gameOver())
                reset();
        }
    }

    @Override
    public synchronized void update(Event event) {
        if (!isGameRunning)
            if (creationController.isGameReady()) {
                game = creationController.createGame();
                isGameRunning = true;
                for (ClientHandler clientHandler : clientHandlers) {
                    game.registerObserver(clientHandler);
                    clientHandler.sendOutput(JsonTools.createMessage("The game is starting!"));
                }
                game.sendState(View.COMMON_CARDS);
            }
        this.notifyAll();
    }

    /**
     *
     * @param clientHandler
     */
    public synchronized void addClient(ClientHandler clientHandler) {
        if (!isGameRunning && creationController.isSpotAvailable()) {
            clientHandlers.add(clientHandler);
            clientHandler.registerObserver(creationController);
            clientHandler.registerObserver(this);
            clientHandler.requestInput(Prompt.NICKNAME);
            if (clientHandlers.size() == 1) {
                while (clientHandlers.get(0).getNickname() == null)
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
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
    private synchronized void reset() {
        isGameRunning = false;
        turnController = null;
        creationController = new CreationController();
        clientHandlers = new ArrayList<>();
    }
}
