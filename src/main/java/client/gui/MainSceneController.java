package client.gui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import server.controller.Prompt;
import server.model.Token;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;
import static server.controller.utilities.ConfigLoader.*;
import static server.controller.utilities.JsonTools.readMatrix;

public class MainSceneController implements Client, Initializable {
    NetworkHandler networkHandler;
    @FXML
    private GridPane board;
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
                    case "tiles" -> updateBoard(readMatrix(jsonReader));
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
        new Thread(networkHandler).start();
    }

    private void updateBoard(int [][]tiles){
        for(int row = 0; row < board.getHeight(); row++) {
            for(int col = 0; row < board.getMaxWidth(); col++) {
                Node node;
                node = getNodeByRowColumnIndex(row,col,board);
                switch(tiles[row][col]){
                    case 1 -> node.setStyle("cat1");
                    case 2 -> node.setStyle("book1");
                    case 3 -> node.setStyle("toy1");
                    case 4 -> node.setStyle("trophy1");
                    case 5 -> node.setStyle("frame1");
                    case 6 -> node.setStyle("plant1");

                }
            }
        }
    }

    private Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}

