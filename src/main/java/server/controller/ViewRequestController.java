package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.model.Game;
import server.model.View;
import server.view.ClientHandler;

import java.io.IOException;
import java.io.StringReader;

//TODO: Document this
//TODO: Instantiate in controller

/**
 *
 */
public class ViewRequestController implements Observer {
    Game game;

    public ViewRequestController(Game game) {
        this.game = game;
    }

    @Override
    public void update(Event event) {
        ClientHandler currentClientHandler;
        int tempIndex = -1;
        View request = View.CHAT;
        String jsonMessage = event.jsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "requestBoard" -> request = View.BOARD;
                    case "requestShelf" -> request = View.SHELF;
                    case "requestPersonalCard" -> request = View.PERSONAL_CARD;
                    case "index" -> tempIndex = jsonReader.nextInt();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            if (tempIndex != -1) {
                currentClientHandler = Controller.findClientHandler(tempIndex);
                if (currentClientHandler != null)
                    currentClientHandler.update(new Event(game.getView(request).toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
