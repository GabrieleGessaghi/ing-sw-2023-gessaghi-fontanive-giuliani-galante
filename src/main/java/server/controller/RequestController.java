package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.model.Game;
import server.model.View;
import server.view.ClientHandler;

import java.io.IOException;
import java.io.StringReader;

//TODO: Handle chat requests

/**
 * Handles specific requests from clients.
 * @author Giorgio Massimo Fontanive
 */
public class RequestController implements Observer {
    Game game;

    /**
     * Class constructor.
     * @param game The game to which this object asks for the information requested.
     */
    public RequestController(Game game) {
        this.game = game;
    }

    @Override
    public void update(Event event) {
        ClientHandler currentClientHandler;
        int tempIndex = -1;
        View request = null;
        String requestedPlayerNickname = null;
        String jsonMessage = event.jsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "requestBoard" -> {
                        request = View.BOARD;
                        jsonReader.skipValue();
                    }
                    case "requestShelf" -> {
                        request = View.SHELF;
                        jsonReader.skipValue();
                    }
                    case "requestPersonalCard" -> {
                        request = View.PERSONAL_CARD;
                        jsonReader.skipValue();
                    }
                    case "requestCommonCards" -> {
                        request = View.COMMON_CARDS;
                        jsonReader.skipValue();
                    }
                    case "requestPlayer" -> {
                        request = View.SPECIFIC_PLAYER;
                        jsonReader.skipValue();
                    }
                    case "requestedPlayerNickname" -> requestedPlayerNickname = jsonReader.nextString();
                    case "index" -> tempIndex = jsonReader.nextInt();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            //Sends the requested information
            if (tempIndex != -1 && request != null) {
                currentClientHandler = Controller.findClientHandler(tempIndex);
                if (currentClientHandler != null) {
                    if (requestedPlayerNickname == null)
                        requestedPlayerNickname = currentClientHandler.nickname;
                    currentClientHandler.update(new Event(game.getView(request, requestedPlayerNickname).toString()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
