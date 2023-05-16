package client.gui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import server.controller.Prompt;
import server.model.Token;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;
import static server.controller.utilities.ConfigLoader.*;
public class MainSceneController implements Client, Initializable {
    NetworkHandler networkHandler;
    @FXML
    private GridPane board;
    private int[][] intBoard;
    @Override
    public void requestInput(Prompt prompt) {
        switch (prompt) {
            case NICKNAME -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("nickname", GUI.playerNickname);
                networkHandler.sendInput(jsonObject.toString());
            }
            case PLAYERSNUMBER -> {
                //TODO: Show alert asking player for number
            }
            case TOKENS -> {
            }
            case COLUMN -> {
            }
            case CONNECTIONTYPE -> {
            }
        }
    }

    @Override
    public void showOutput(String jsonMessage) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonMessage));
        String field;
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n");
        try {
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    //case "nickname" ->
                    //case "totalPoints" ->
                    //case "isFirstPlayer" ->
                    //case "playerIndex" ->
                    //case "currentPlayerNickname" ->
                    //case "objectiveDescription" ->
                    //case "numberOfTokensLeft" ->
                    //case "nextPointsAvailable" ->
                    //case "message" ->
                    //case "tiles" ->
                    //case "shelf" ->
                    //case "personalCard" ->
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            System.out.print(toPrint);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TODO: Parse JSON and update each section of the screen
    }

    @Override
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (GUI.connectionType == 0)
            setNetworkHandler(new NetworkHandlerTCP());
        if (GUI.connectionType == 1)
            setNetworkHandler(new NetworkHandlerRMI());
        networkHandler.setHost(GUI.host);
        networkHandler.setClient(this);
        intBoard = new int [BOARD_SIZE][BOARD_SIZE];
        new Thread(networkHandler).start();
        GridPane
    }

}

