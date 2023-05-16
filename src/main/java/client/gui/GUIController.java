package client.gui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import client.tui.ClientTUI;
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
import server.controller.Prompt;

public class GUIController {

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
        String playerNickname = nicknameTextField.getText();
        NetworkHandler networkHandler ;
        String connectionType;
        if (rmiSelection.isSelected()){
            networkHandler = new NetworkHandlerRMI();
            connectionType = "RMI";
        }else {
            networkHandler = new NetworkHandlerTCP();
            connectionType = "SOCKET";
        }
        String host = hostTextField.getText();
        //Client client = new ;
        networkHandler.setHost(host);
        //networkHandler.setClient(client);

        Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"));
        Scene mainScene = new Scene(root);
        mainScene.getStylesheets().add(getClass().getResource("/Application.css").toExternalForm());
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(mainScene);
        currentWindow.show();
        try {
            new Thread(networkHandler).start();
            System.out.println(playerNickname+" is connected to the server using "+connectionType+" at: "+host);
            //client.setNetworkHandler(networkHandler);
        }catch (RuntimeException e){
            // TODO unable to connect
        }
    }

}
