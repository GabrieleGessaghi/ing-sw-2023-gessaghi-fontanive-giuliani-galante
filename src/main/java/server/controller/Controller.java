package server.controller;

import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
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
        int i = 0;
        while(true) {

            //Waits for the game to be running
            while (!isGameRunning)
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("Error while waiting for the game to start.");
                }

            //Instantiates the chat
            ChatController chatController = new ChatController(clientHandlers);
            for (ClientHandler c : clientHandlers)
                c.registerObserver(chatController);

            //Manages turns
            while (!game.gameOver()) {
                if (i == clientHandlers.size())
                    i = 0;
                ClientHandler currentClient = clientHandlers.get(i);
                turnController = new TurnController(game, currentClient);
                currentClient.registerObserver(turnController);
                turnController.newTurn();
                i++;
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
     *
     * @param clientHandler
     */
    public synchronized void addClient(ClientHandler clientHandler) {
        if (!isGameRunning && creationController.isSpotAvailable()) {
            clientHandlers.add(clientHandler);
            clientHandler.registerObserver(creationController);
            clientHandler.registerObserver(this);
            clientHandler.requestInput(Prompt.NICKNAME);

            //Requests the players number if it's the first player added
            if (clientHandlers.size() == 1) {
                while (clientHandlers.get(0).getNickname() == null)
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
}
