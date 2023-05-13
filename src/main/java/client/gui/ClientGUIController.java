package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ClientGUIController {

    @FXML
    private Button createBtn;

    @FXML
    private Button joinBtn;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private ChoiceBox<?> numberOfPlayersSelector;

    @FXML
    private RadioButton rmiSelection;

    @FXML
    private RadioButton socketSelection;

    @FXML
    private Label title;

    @FXML
    void createBtnClicked(ActionEvent event) {

    }

    @FXML
    void joinBtnClicked(ActionEvent event) {

    }

}
