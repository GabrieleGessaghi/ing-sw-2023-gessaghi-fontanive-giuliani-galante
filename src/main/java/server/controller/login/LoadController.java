package server.controller.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Controller;
import server.controller.observer.Event;
import server.model.Game;
import server.view.ClientHandler;

import java.util.ArrayList;

/**
 * Controls loading a game from a save file.
 * @author Giorgio Massimo Fontanive
 */
public class LoadController extends LoginController{

    public LoadController(ArrayList<String> nicknames) {
        playersNicknames = nicknames;
        playersNumber = nicknames.size();
        for (String nickname : nicknames)
            Controller.disconnectedClients.put(nickname, -1);
    }

    @Override
    public boolean isGameReady() {
        return Controller.disconnectedClients.size() == 0;
    }

    @Override
    public Game createGame(){
        Game game = new Game(playersNumber, playersNicknames);
        game.loadGame();
        return game;
    }

    @Override
    public void update(Event event) {
        int index;
        String nickname;
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        index = jsonObject.get("index").getAsInt();
        if (jsonObject.has("nickname")) {
            nickname = jsonObject.get("nickname").getAsString();
            ClientHandler clientHandler = Controller.findClientHandler(index);
            if (clientHandler != null)
                clientHandler.nickname = nickname;
            checkDisconnection(nickname);
        }
    }
}
