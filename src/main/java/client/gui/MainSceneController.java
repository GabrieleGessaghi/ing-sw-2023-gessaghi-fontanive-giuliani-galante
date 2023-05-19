package client.gui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import server.controller.Prompt;
import server.controller.utilities.JsonTools;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

import static server.controller.utilities.ConfigLoader.*;
import static server.controller.utilities.JsonTools.readMatrix;


public class MainSceneController implements Client, Initializable {

    public GridPane shelf;
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
            case PLAYERSNUMBER -> requestPlayersNumber();
            case TOKENS -> {
            }
            case COLUMN -> {
            }
        }
    }

    @Override
    public void showOutput(String jsonMessage) {
        String tempNickname = "";
        int[][] tempTiles = null;
        JsonReader jsonReader = new JsonReader(new StringReader(jsonMessage));
        String field;
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("\n");
        try {
            jsonReader.beginObject();
            while(jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "nickname" -> tempNickname = jsonReader.nextString();
                    //case "totalPoints" ->
                    //case "isFirstPlayer" ->
                    //case "playerIndex" ->
                    //case "currentPlayerNickname" ->
                    //case "objectiveDescription" ->
                    //case "numberOfTokensLeft" ->
                    //case "nextPointsAvailable" ->
                    //case "message" ->
                    case "tiles" -> updateTokens(readMatrix(jsonReader), false);
                    case "shelf" -> tempTiles = JsonTools.readMatrix(jsonReader);
                    //case "personalCard" ->
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            if(tempNickname.equals(GUI.playerNickname))
                updateTokens(tempTiles, true);
            System.out.print(toPrint);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Shows a popup screen asking user for player's number.
     * @author Gabriele Gessaghi
     */
    private void requestPlayersNumber() {
        Platform.runLater(()->{
            String [] arrayData = {"Two (2)", "Three (3)", "Four (4)"};
            List<String> dialogData = Arrays.asList(arrayData);
            ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0),dialogData);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
            dialog.setTitle("Game settings");
            dialog.setGraphic(null);
            dialog.setHeaderText("Select the number of players: ");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/Application.css").toExternalForm());
            dialogPane.getStyleClass().add("playerDialog");
            Optional<String> result = dialog.showAndWait();
            int numberOfPlayers = 0;
            if (result.isPresent()) {
                switch (result.get()){
                    case "Two (2)" -> numberOfPlayers = 2;
                    case "Three (3)" -> numberOfPlayers = 3;
                    case "Four (4)" -> numberOfPlayers = 4;
                }
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("playersNumber", numberOfPlayers);
            networkHandler.sendInput(jsonObject.toString());
        });
    }

    private void updateTokens(int[][] tokens, boolean choice) {
        Node node;
        for(int i = 0; i < tokens.length; i++){
            for(int j = 0; j < tokens[i].length; j++) {
                node = getNodeByRowColumnIndex(i, j, choice);
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                if(node != null) {
                    switch (tokens[i][j]) {
                        case 0 -> node.setVisible(false);
                        case 1 -> {
                            node.getStyleClass().add("cat" + pictureNumber);
                            node.setVisible(true);
                        }
                        case 2 -> {
                            node.getStyleClass().add("book" + pictureNumber);
                            node.setVisible(true);
                        }
                        case 3 -> {
                            node.getStyleClass().add("toy" + pictureNumber);
                            node.setVisible(true);
                        }
                        case 4 -> {
                            node.getStyleClass().add("trophy" + pictureNumber);
                            node.setVisible(true);
                        }
                        case 5 -> {
                            node.getStyleClass().add("frame" + pictureNumber);
                            node.setVisible(true);
                        }
                        case 6 -> {
                            node.getStyleClass().add("plant" + pictureNumber);
                            node.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    //TODO: Document this
    /**
     *
     * @param row
     * @param column
     * @param choice
     * @return
     * @author Niccolò Giuliani
     */
    private Node getNodeByRowColumnIndex (int row,int column, boolean choice) {
        Node result = null;
        int gridRow;
        int gridColumn;
        ObservableList<Node> children;
        if(choice)
            children = shelf.getChildren();
        else
            children = board.getChildren();
        for (Node node : children) {
            try {
                if(GridPane.getRowIndex(node)  == null)
                    gridRow = 0;
                else
                    gridRow = GridPane.getRowIndex(node);

                if(GridPane.getColumnIndex(node)  == null)
                    gridColumn = 0;
                else
                    gridColumn = GridPane.getColumnIndex(node);

                if (gridRow == row && gridColumn == column) {
                    result = node;
                    break;
                }
            } catch(NullPointerException ex){
                throw new RuntimeException(ex);
            }
        }

        return result;
    }
}

