package server.controller;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import server.controller.observer.Event;
import server.controller.observer.Observer;
import server.controller.utilities.JsonTools;
import server.model.chat.Chat;
import server.view.ClientHandler;

import java.io.IOException;
import java.io.StringReader;

/**
 * Handles messaging in the game
 * @author Giorgio Massimo Fontanive
 */
public class ChatController implements Observer {
    Chat chat;

    /**
     * Class constructor.
     */
    public ChatController(Chat chat) {
        this.chat = chat;
    }

    /**
     * Receives messages regarding the chat.
     * @param event The message to be sent to this observer.
     */
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
                    case "receiverNickname" -> currentMessageReceiver = jsonReader.nextString();
                    case "message" -> message = jsonReader.nextString();
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            if (message != null && currentMessageSender != null)
                if (currentMessageReceiver != null) {
                    ClientHandler currentMessageReceiverHandler = Controller.findClientHandlerByName(currentMessageReceiver);
                    if (currentMessageReceiverHandler != null) {
                        JsonObject privateMessage = chat.addPrivateMessage(currentMessageSender, currentMessageReceiver, message);
                        currentMessageReceiverHandler.sendOutput(privateMessage.toString());
                    } else {
                        ClientHandler sender = Controller.findClientHandlerByName(currentMessageSender);
                        if (sender != null)
                            sender.sendOutput(JsonTools.createMessage("No player with this nickname found!", true));
                    }
                }
                else
                    chat.addPublicMessage(currentMessageSender, message);

        } catch (IOException e) {
            System.out.println("Error in reading JSON file.");
        }
    }
}
