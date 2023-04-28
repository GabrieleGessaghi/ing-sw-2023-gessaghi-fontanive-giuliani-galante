package server.model.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * A chat players can communicate through.
 * @author Giorgio Massimo Fontanive
 */
public class Chat {
    private final List<String> publicMessages;
    private final List<Message> privateMessages;

    /**
     * Chat constructor.
     * @author Giorgio Massimo Fontanive
     */
    public Chat() {
        publicMessages = new ArrayList<>();
        privateMessages = new ArrayList<>();
    }

    /**
     * Getter for messages' list.
     * @author Giorgio Massimo Fontanive
     * @return the list of messages that have been sent.
     */
    public List<String> getPublicMessages() {
        return publicMessages;
    }

    /**
     * Creates a list of messages for all private messages exchanged by two players.
     * @author Giorgio Massimo Fontanive
     * @param firstNickname the nickname of the player in the conversation.
     * @param secondNickname the nickname of the player in the conversation.
     * @return a list of all private messages exchanged by the two players.
     */
    public List<String> getPrivateConversation(String firstNickname, String secondNickname) {
        List<String> messages = new ArrayList<>();
        for (Message message: privateMessages)
            if (firstNickname.equals(message.getSenderNickname()) && secondNickname.equals(message.getReceiverNickname()) ||
                    secondNickname.equals(message.getSenderNickname()) && firstNickname.equals(message.getReceiverNickname()))
                messages.add(message.getMessage());
        return messages;
    }

    /**
     * Sends a message to the public chat.
     * @author Giorgio Massimo Fontanive
     * @param nickname the nickname of the player sending the message.
     * @param message the nickname of the player sending the message.
     */
    public void addPublicMessage(String nickname, String message) {
        String newMessage = nickname + ": " + message;
        publicMessages.add(newMessage);
    }

    /**
     * Creates a message for a private chat.
     * @author Giorgio Massimo Fontanive
     * @param senderNickname the nickname of the player sending the message.
     * @param receiverNickname the nickname of the player receiving the message.
     * @param message the content of the message.
     */
    public void addPrivateMessage(String senderNickname, String receiverNickname, String message) {
        privateMessages.add(new Message(senderNickname, receiverNickname, message));
    }
}
