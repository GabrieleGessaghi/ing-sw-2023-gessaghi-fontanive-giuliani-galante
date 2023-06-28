package client.gui;

import client.network.NetworkHandler;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static client.gui.GUI.playerNickname;

public class ChatController {
    @FXML
    private ImageView ChatBackground;
    @FXML
    private ToggleGroup ChatButtons;
    @FXML
    private TextArea ChatField;
    @FXML
    private TextField MessageField;
    @FXML
    public RadioButton Player1Button;
    @FXML
    private RadioButton Player2Button;
    @FXML
    private RadioButton Player3Button;
    @FXML
    private RadioButton PublicButton;
    @FXML
    private Button SendButton;
    private NetworkHandler networkHandler;
    private String receiverNickname;

    @FXML
    public void sendButtonClicked(){
        String message = MessageField.getText();
        if(!message.isEmpty()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("senderNickname", playerNickname);
            if (receiverNickname != null)
                jsonObject.addProperty("receiverNickname", receiverNickname);
            networkHandler.sendInput(jsonObject.toString());
        }
        MessageField.setText("");
    }

    @FXML
    public void radioButtonSelected(){
        if(Player1Button.isSelected())
            receiverNickname = Player1Button.getText();
        else if(Player2Button.isSelected())
            receiverNickname = Player2Button.getText();
        else if(Player3Button.isSelected())
            receiverNickname = Player3Button.getText();
        else if(PublicButton.isSelected())
            receiverNickname = null;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestChat", true);
        if (receiverNickname != null)
            jsonObject.addProperty("requestedPlayerNickname", receiverNickname);
        networkHandler.sendInput(jsonObject.toString());
    }

    public void setMessages(List<String> messages) {
        StringBuilder chat = new StringBuilder();
        for (String message : messages)
            chat.append(message).append("\n");
        ChatField.setText(chat.toString());
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestChat", true);
        networkHandler.sendInput(jsonObject.toString());
    }

    public void setRadioButtons(List<String> nicknames){
        receiverNickname = null;
        switch (nicknames.size()){
            case 2 -> {
                for(String nick : nicknames)
                    if (!nick.equals(playerNickname)) {
                        Player1Button.setText(nick);
                        Player1Button.setVisible(true);
                    }
            }
            case 3 -> {
                int i = 0;
                for(String nick : nicknames)
                    if(!nick.equals(GUI.playerNickname)){
                        if(i == 0) {
                            Player1Button.setText(nick);
                            Player1Button.setVisible(true);
                            i++;
                        }else if (i == 1){
                            Player2Button.setText(nick);
                            Player2Button.setVisible(true);
                        }
                    }
            }
            case 4 -> {
                int i = 0;
                for (String nick : nicknames)
                    if (!nick.equals(GUI.playerNickname)){
                        if (i == 0) {
                            Player1Button.setText(nick);
                            Player1Button.setVisible(true);
                            i++;
                        } else if (i == 1){
                            Player2Button.setText(nick);
                            Player2Button.setVisible(true);
                            i++;
                        } else if (i == 2){
                            Player3Button.setText(nick);
                            Player3Button.setVisible(true);
                        }
                    }
            }
        }
    }
}


