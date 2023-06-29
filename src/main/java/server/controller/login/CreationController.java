package server.controller.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Controller;
import server.controller.observer.Event;
import server.controller.utilities.JsonTools;
import server.view.ClientHandler;

import java.util.ArrayList;

/**
 * Controller of the creation of the game.
 * @author Giorgio Massimo Fontanive
 */
public class CreationController extends LoginController {

    /**
     * Class constructor
     * @author Niccolò Giuliani
     */
    public CreationController(){
        playersNicknames = new ArrayList<>();
        playersNumber = -1;
    }

    /**
     * Receives information for the game creation.
     * @param event The message to be sent to this observer.
     * @author Giorgio Massimo Fontanive
     */
    @Override
    public void update(Event event) {
        int index;
        String nickname = null;
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        index = jsonObject.get("index").getAsInt();
        if (jsonObject.has("nickname"))
            nickname = jsonObject.get("nickname").getAsString();
        if (jsonObject.has("playersNumber"))
            playersNumber = jsonObject.get("playersNumber").getAsInt();
        if (nickname != null) {
            updateNickname(nickname, index);
            checkDisconnection(nickname);
        }
    }

    /**
     * Checks if there's duplicate nicknames.
     * @param nickname The last player's nickname.
     * @param index The last player's index.
     * @author Giorgio Massimo Fontanive
     */
    private void updateNickname(String nickname, int index) {
        ClientHandler clientHandler = Controller.findClientHandler(index);
        if (clientHandler != null) {
            clientHandler.nickname = addPlayer(nickname);
            if (!nickname.equals(clientHandler.nickname)) {
                clientHandler.sendOutput(JsonTools.createMessage("Duplicate name, yours is now " + clientHandler.nickname, true));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("newNickname", clientHandler.nickname);
                clientHandler.sendOutput(jsonObject.toString());
            }
        }
    }

    /**
     * Getter of the state of the game (ready or not ready).
     * @return True if game is ready.
     * @author Niccolò Giuliani
     */
    public boolean isGameReady(){
         return playersNicknames.size() == playersNumber;
    }

    /**
     * Method to add a player and avoid name duplicates.
     * @param nickname Nickname of the player to add.
     * @author Giorgio Massimo Fontanive
     */
    private String addPlayer(String nickname) {
        String newNickname;
        if (!playersNicknames.contains(nickname) || Controller.disconnectedClients.containsKey(nickname))
            newNickname = nickname;
        else {
            int i = 0;
            for (String s : playersNicknames)
                if (s.equals(nickname))
                    i++;
            newNickname = nickname + i;
        }
        playersNicknames.add(newNickname);
        return newNickname;
    }
}
