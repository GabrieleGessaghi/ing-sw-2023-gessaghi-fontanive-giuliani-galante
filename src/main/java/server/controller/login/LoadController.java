package server.controller.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Controller;
import server.controller.observer.Event;

import java.util.ArrayList;

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
    public void update(Event event) {
        String nickname;
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        if (jsonObject.has("nickname")) {
            nickname = jsonObject.get("nickname").getAsString();
            checkDisconnection(nickname);
        }
    }
}
