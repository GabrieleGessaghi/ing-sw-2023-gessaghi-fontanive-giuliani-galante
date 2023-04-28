package server.model.chat;

/**
 * Holds information for a single message in a private chat.
 * @author Giorgio Massimo Fontanive
 */
public class Message {
   private String senderNickname;
   private String receiverNickname;
   private String message;

   public Message(String senderNickname, String receiverNickname, String message) {
       this.senderNickname = senderNickname;
       this.receiverNickname = receiverNickname;
       this.message = senderNickname + ": " + message;
   }

    public String getSenderNickname() {
        return senderNickname;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public String getMessage() {
       return message;
   }
}
