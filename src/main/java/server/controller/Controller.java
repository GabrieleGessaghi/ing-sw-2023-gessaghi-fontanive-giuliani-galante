package server.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.login.CreationController;
import server.controller.login.LoadController;
import server.controller.login.LoginController;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.ConfigLoader;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.model.View;
import server.model.chat.Chat;
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
    private boolean isPreviousGameSaved;
    private LoginController loginController;
    private TurnController turnController;
    private ChatController chatController;
    private RequestController requestController;
    private Game game;
    private Chat chat;

    public Controller() {
        reset();
    }

    /**
     * Manages turns by sending requests and updates to client handlers.
     * @author Giorgio Massimo Fontanive
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
                ClientHandler currentClient = findClientHandlerByName(game.getCurrentPlayer());
                game.sendState(View.BOARD);
                game.sendState(View.CURRENT_PLAYER);
                if (currentClient != null) {
                    currentClient.sendOutput(game.getView(View.PERSONAL_CARD, currentClient.nickname).toString());
                    currentClient.sendOutput(game.getView(View.SHELF, currentClient.nickname).toString());
                    turnController = new TurnController(game, currentClient);
                    currentClient.registerObserver(turnController);
                    turnController.newTurn();
                    if (!isGameRunning)
                        break;
                    currentClient.sendOutput(game.getView(View.SHELF, currentClient.nickname).toString());
                    currentClient.sendOutput(game.getView(View.POINTS, currentClient.nickname).toString());
                }
            }

            //Makes everyone disconnect after the game finishes
            if (isGameRunning) {
                for (ClientHandler ch : clientHandlers) {
                    ch.sendOutput(JsonTools.createMessage("Log back in if you want to play again.", false));
                    ch.disconnect();
                }
                reset();
            }
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
                if (isGameRunning && !isPreviousGameSaved) {
                    game.registerObserver(clientHandler);
                    game.setPlayerConnection(clientHandler.nickname, true);
                    chat.registerObserver(clientHandler);
                    clientHandler.registerObserver(chatController);
                    clientHandler.registerObserver(requestController);
                }
            }

        //Checks whether it can start a new game
        if (!isGameRunning)
            synchronized (this) {
                if (loginController.isGameReady()) {
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
        if (loginController.isSpotAvailable() || !disconnectedClients.isEmpty()) {
            clientHandlers.add(clientHandler);
            clientHandler.registerObserver(loginController);
            clientHandler.registerObserver(this);
            clientHandler.requestInput(Prompt.NICKNAME);

            //Requests the players number if it's the first player added
            if (clientHandlers.size() == 1 && !isPreviousGameSaved)
                clientHandler.requestInput(Prompt.PLAYERSNUMBER);
        } else {
            clientHandler.sendOutput(JsonTools.createMessage("The game is full, please exit!", true));
            clientHandler.disconnect();
        }
    }

    /**
     * Starts the game.
     * @author Giorgio Massimo Fontanive
     */
    private void startGame() {
        game = loginController.createGame();
        chat = new Chat();
        chatController = new ChatController(chat);
        requestController = new RequestController(game, chat);
        for (ClientHandler clientHandler : clientHandlers) {
            game.registerObserver(clientHandler);
            chat.registerObserver(clientHandler);
            clientHandler.sendOutput(JsonTools.createMessage("The game is starting!", false));
            clientHandler.registerObserver(chatController);
            clientHandler.registerObserver(requestController);
        }
    }

    /**
     * Handles a player's disconnection.
     * @param clientHandler The client handler that has disconnected.
     * @author Giorgio Massimo Fontanive
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
        else if (clientHandlers.size() == 0) {
            turnController.skipTurn();
            reset();
        }

        //Checks if there's only one client left
        else if (clientHandlers.size() == 1)
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if (clientHandlers.size() == 1) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("winnerNickname", clientHandlers.get(0).nickname);
                                clientHandlers.get(0).sendOutput(jsonObject.toString());
                                clientHandlers.get(0).sendOutput(JsonTools.createMessage("You are the only player left. You win!", false));
                                clientHandlers.get(0).sendOutput(JsonTools.createMessage("Log back in if you want to play again.", false));
                                clientHandlers.get(0).disconnect();
                                turnController.skipTurn();
                                Game.deleteSave();
                                reset();
                            }
                        }
                    },
                    ConfigLoader.LONE_PLAYER_WAIT
            );


        //Checks if it was their turn
        else if (game.getCurrentPlayer().equals(clientHandler.nickname))
            turnController.skipTurn();

        if (game != null)
            game.setPlayerConnection(clientHandler.nickname, false);
    }

    /**
     * Resets the object to initial state.
     * @author Giorgio Massimo Fontanive
     */
    private void reset() {
        isGameRunning = false;
        isPreviousGameSaved = Game.isThereGameSaved();
        turnController = null;
        clientHandlers = new ArrayList<>();
        disconnectedClients = new HashMap<>();
        game = null;
        chat = null;
        if (isPreviousGameSaved)
            loginController = new LoadController(Game.loadNicknames());
        else
            loginController = new CreationController();
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
     * @author Giorgio Massimo Fontanive
     */
    public static ClientHandler findClientHandlerByName(String nickname) {
        for (ClientHandler clientHandler : clientHandlers)
            if (clientHandler.nickname.equals(nickname))
                return clientHandler;
        return null;
    }
}
