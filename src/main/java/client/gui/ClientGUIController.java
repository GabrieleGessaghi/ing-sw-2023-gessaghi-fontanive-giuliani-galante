package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ClientGUIController {

    @FXML
    private TextField nicknameTextField;

    @FXML
    void connectBTNClicked(ActionEvent event) {
        String nickname = nicknameTextField.getText();
        System.out.println(nickname + " si sta connettendo al server ...");
    }

}