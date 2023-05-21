package server.controller;

import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.chat.Chat;
import server.view.ClientHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 *
 * @author Giorgio Massimo Fontanive
 */
public class ChatController implements Observer {
    Chat chat;
    List<ClientHandler> clientHandlers;

    public ChatController(List<ClientHandler> clientHandlers) {
        chat = new Chat();
        this.clientHandlers = clientHandlers;
    }

    private ClientHandler findClientHandler(String nickname) {
        for (ClientHandler c : clientHandlers)
            if (c.nickname.equals(nickname))
                return c;
        return null;
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
                    case "senderNickname" -> currentMessageSender = jsonReader.nextString();
                    case "message" -> message = jsonReader.nextString();
                    case "receiverNickname" -> currentMessageReceiver = jsonReader.nextString();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            if (message != null && currentMessageSender != null)
                if (currentMessageReceiver != null)
                    if (findClientHandler(currentMessageReceiver) != null)
                        chat.addPrivateMessage(currentMessageSender, currentMessageReceiver, message);
                    else
                        findClientHandler(currentMessageSender).sendOutput(JsonTools.createMessage("No player with this nickname found!"));
                else
                    chat.addPublicMessage(currentMessageSender, message);

        } catch (IOException e) {
            System.out.println("Error in reading JSON file.");
        }
    }
}
