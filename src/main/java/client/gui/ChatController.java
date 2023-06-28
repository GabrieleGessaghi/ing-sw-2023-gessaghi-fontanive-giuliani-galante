package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
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
    private VBox ChatField;
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

    @FXML
    void sendButtonClicked(){
        String message = MessageField.getText();
        if(!message.isEmpty())
            sendMessage(radioButtonSelected(), message);
    }

    @FXML
    String radioButtonSelected(){
        //REQUEST MESSAGES TO BE DISPLAYED
        String selectedPlayerNickname = "";
        if(Player1Button.isSelected())
            selectedPlayerNickname = Player1Button.getText();
        else if(Player2Button.isSelected())
            selectedPlayerNickname = Player2Button.getText();
        else if(Player3Button.isSelected())
            selectedPlayerNickname = Player3Button.getText();
        else if(PublicButton.isSelected())
            selectedPlayerNickname = "public";
        return selectedPlayerNickname;
    }

    void sendMessage(String chatSelection, String message){
        //SEND MESSAGE THROUGH NETWORK HANDLER
    }

    void setMessages(List<String> messages) {
        //DISPLAY MESSAGES
    }

    @FXML
    void setRadioButtons(List<String> nicknames){
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


