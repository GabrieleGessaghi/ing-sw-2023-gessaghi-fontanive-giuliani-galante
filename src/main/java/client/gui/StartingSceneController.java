package client.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartingSceneController {

    @FXML
    private Button connectBtn;

    @FXML
    private TextField hostTextField;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private RadioButton rmiSelection;

    @FXML
    private RadioButton socketSelection;

    @FXML
    private Label title;

    @FXML
    void connectBtnClicked(ActionEvent event) throws Exception{
        GUI.playerNickname = nicknameTextField.getText();
        if (socketSelection.isSelected())
            GUI.connectionType = 0;
        if (rmiSelection.isSelected())
            GUI.connectionType = 1;
        GUI.host = hostTextField.getText();

        Parent root = FXMLLoader.load(getClass().getResource("/javafx/MainScene.fxml"));
        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(mainScene);

        currentWindow.show();

        currentWindow.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
