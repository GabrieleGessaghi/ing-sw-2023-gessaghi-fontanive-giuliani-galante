package server.controller.login;

import server.controller.Controller;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.Game;
import server.view.ClientHandler;

import java.util.ArrayList;

/**
 * Controls the creation of a game.
 * @author Giorgio Massimo Fontanive
 */
public abstract class LoginController implements Observer {
    protected int playersNumber;
    protected ArrayList<String> playersNicknames;

    /**
     * @return True if the game is ready to start.
     */
    public abstract boolean isGameReady();

    /**
     * Method to create the Game.
     * @return The Game created.
     * @author Niccolò Giuliani
     */
    public Game createGame(){
        return new Game(playersNumber, playersNicknames);
    }

    /**
     * Checks whether there's a spot available.
     * @return True if there's space for more players.
     * @author Giorgio Massimo Fontanive
     */
    public boolean isSpotAvailable() {
        return playersNumber == -1 || playersNicknames.size() < playersNumber;
    }

    /**
     * Checks whether the given player had previously disconnected.
     * @param nickname The player's nickname.
     */
    protected void checkDisconnection(String nickname) {
        ClientHandler clientHandler = Controller.findClientHandlerByName(nickname);
        if (clientHandler != null && !isSpotAvailable() && !Controller.disconnectedClients.isEmpty())
            if (Controller.disconnectedClients.containsKey(nickname)) {
                int index = Controller.disconnectedClients.get(nickname);
                if (index != -1)
                    clientHandler.index = index;
                clientHandler.sendOutput(JsonTools.createMessage("Welcome back!", false));
                clientHandler.reconnect();
            } else
                Controller.clientHandlers.remove(clientHandler);
    }
}
