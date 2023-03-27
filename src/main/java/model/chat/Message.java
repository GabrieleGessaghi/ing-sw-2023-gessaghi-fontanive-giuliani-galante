package model.chat;

/**
 *
 * @author Giorgio Massimo Fontanive
 */
public class Message {
   private String senderNickname;
   private String receiverNickname;
   private String message;

   public Message(String senderNickname, String receiverNickname, String message) {
       this.senderNickname = senderNickname;
       this.receiverNickname = receiverNickname;
       this.message = message;
   }
}
