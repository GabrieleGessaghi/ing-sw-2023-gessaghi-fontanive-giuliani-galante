package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.model.chat.Chat;

import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author Giorgio Massimo Fontanive
 */
public class ChatController implements Observer {
    Chat chat;

    public ChatController() {
        chat = new Chat();
    }

    @Override
    public void update(Event event) {
        String currentMessageSender = null;
        String currentMessageReceiver = null;
        String message = null;

        String jsonMessage = event.jsonMessage();
        String field;
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new StringReader(jsonMessage));
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "nickname" -> currentMessageSender = jsonReader.nextString();
                    case "message" -> message = jsonReader.nextString();
                    case "receiverNickname" -> currentMessageReceiver = jsonReader.nextString();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            if (message != null && currentMessageSender != null)
                if (currentMessageReceiver != null)
                    chat.addPrivateMessage(currentMessageSender, currentMessageReceiver, message);
                else
                    chat.addPublicMessage(currentMessageSender, message);

        } catch (IOException e) {
            System.out.println("Error in reading JSON file.");
        }
    }
}
