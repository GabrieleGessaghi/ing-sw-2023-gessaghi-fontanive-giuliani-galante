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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import server.controller.Prompt;
import server.model.Board;
import server.model.Token;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

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
                Platform.runLater(()->{
                    String [] arrayData = {"Two (2)", "Three (3)", "Four (4)"};
                    List<String> dialogData = Arrays.asList(arrayData);
                    ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0),dialogData);
                    dialog.setTitle("Game settings");
                    //dialog.setGraphic(new ImageView(this.getClass().getResource("/assets/Publisher_material/Icon50x50px.png").toString()));
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
                });
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
        Node node;
        for(int row = 0; row < BOARD_SIZE; row++) {
            for(int col = 0; col < BOARD_SIZE; col++) {
                node = getNodeByRowColumnIndex(row,col);
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                if(node!=null){
                    switch(tiles[row][col]) {
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

    private Node getNodeByRowColumnIndex (int row,int column) {
        Node result = null;
        int gridRow;
        int gridColumn;
        ObservableList<Node> children = board.getChildren();
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

