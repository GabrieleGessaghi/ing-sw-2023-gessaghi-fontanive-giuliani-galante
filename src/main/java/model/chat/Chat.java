package model.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * A chat players can communicate through.
 * @author Giorgio Massimo Fontanive
 */
public class Chat {
    private final List<String> messages;

    /**
     * Chat constructor.
     * @author Giorgio Massimo Fontanive
     */
    public Chat() {
        messages = new ArrayList<>();
    }

    /**
     * Getter for messages' list.
     * @author Giorgio Massimo Fontanive
     * @return the list of messages that have been sent.
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Sends a message to the chat.
     * @author Giorgio Massimo Fontanive
     * @param nickname the nickname of the sender.
     * @param message the content of the message.
     */
    public void addMessage(String nickname, String message) {
        String newMessage = nickname + ": " + message;
        messages.add(newMessage);
    }
}
