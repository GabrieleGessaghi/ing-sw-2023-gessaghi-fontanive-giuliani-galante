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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import server.controller.Prompt;
import server.controller.utilities.ConfigLoader;
import server.controller.utilities.JsonTools;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

public class MainSceneController implements Client, Initializable {
    public ImageView common_goal1;
    public ImageView common_goal2;
    @FXML
    private GridPane board;
    @FXML
    private GridPane shelf;
    @FXML
    private Label points;
    @FXML
    private Label messages;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;
    @FXML
    private ImageView Personal_goal;
    NetworkHandler networkHandler;
    int[][] tokenSelection;
    int tokensSelected;
    int columnSelection;
    boolean selectingTokens;
    boolean selectingColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Connects to the server
        if (GUI.connectionType == 0)
            setNetworkHandler(new NetworkHandlerTCP());
        if (GUI.connectionType == 1)
            setNetworkHandler(new NetworkHandlerRMI());
        networkHandler.setHost(GUI.host);
        networkHandler.setClient(this);
        new Thread(networkHandler).start();

        //TODO: Go back if there's an error

        tokenSelection = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        for (int[] i : tokenSelection)
            Arrays.fill(i, -1);
        tokensSelected = 0;
        columnSelection = -1;

        //Add buttons functionalities
        cancel.setOnMouseClicked(e -> {
            tokenSelection = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
            tokensSelected = 0;
        });
        confirm.setOnMouseClicked(e -> {
            if (selectingTokens) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("selectedTiles", JsonTools.createJsonMatrix(tokenSelection));
                networkHandler.sendInput(jsonObject.toString());
                for (int[] i : tokenSelection)
                    Arrays.fill(i, -1);
                tokensSelected = 0;
                selectingTokens = false;
                System.out.println(jsonObject);
            }
            if (selectingColumn) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("selectedColumn", columnSelection);
                networkHandler.sendInput(jsonObject.toString());
                columnSelection = -1;
                selectingColumn = false;
            }
            confirm.setDisable(true);
            confirm.setVisible(false);
            cancel.setVisible(false);
            messages.setText("");
        });
        for (Node node : board.getChildren())
            node.setOnMouseClicked(e -> {
                int row = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
                tokenSelection[row][column] = tokensSelected;
                tokensSelected++;
            });
        for (Node node : shelf.getChildren())
            node.setOnMouseClicked(e -> {
                columnSelection = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
            });
    }

    @Override
    public void requestInput(Prompt prompt) {
        switch (prompt) {
            case NICKNAME -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("nickname", GUI.playerNickname);
                networkHandler.sendInput(jsonObject.toString());
            }
            case PLAYERSNUMBER -> requestPlayersNumber();
            case TOKENS -> requestTiles();
            case COLUMN -> requestColumn();
        }
    }

    @Override
    public void showOutput(String jsonMessage) {
        String tempNickname = "";
        int[][] tempTiles = null;
        int tempPersonalCard = -1;
        int tempTotalPoints = -1;
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
                    case "totalPoints" -> tempTotalPoints = jsonReader.nextInt();
                    //case "isFirstPlayer" ->
                    //case "playerIndex" ->
                    //case "currentPlayerNickname" ->
                    //case "commonCard1" ->{
                     /*   jsonReader.beginObject();
                        while(jsonReader.hasNext()){
                            field = jsonReader.nextName();
                            if(field.equals("cardIndex"))
                                setCommonCard(jsonReader.nextInt(),false);
                            else
                                jsonReader.skipValue();
                        }
                        jsonReader.endObject();
                    }*/
                    //case "commonCard2" ->{
                        /*jsonReader.beginObject();
                        while(jsonReader.hasNext()){
                            field = jsonReader.nextName();
                            if(field.equals("cardIndex"))
                                setCommonCard(jsonReader.nextInt(),true);
                            else
                                jsonReader.skipValue();
                        }
                       jsonReader.endObject();

                    }*/
                    //case "numberOfTokensLeft" ->
                    //case "nextPointsAvailable" ->
                    case "message" -> {
                        String nextString = jsonReader.nextString();
                        Platform.runLater(() -> messages.setText(nextString));
                    }
                    case "personalCard" -> {
                        jsonReader.beginObject();
                        while(jsonReader.hasNext()) {
                            field = jsonReader.nextName();
                            if (field.equals("cardIndex"))
                                tempPersonalCard = jsonReader.nextInt();
                            else
                                jsonReader.skipValue();
                        }
                        jsonReader.endObject();
                    }
                    case "tiles" -> updateTokens(JsonTools.readMatrix(jsonReader), false);
                    case "shelf" -> {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext())
                            if (jsonReader.nextName().equals("shelfTiles"))
                                tempTiles = JsonTools.readMatrix(jsonReader);
                        jsonReader.endObject();
                    }
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            //Updates some information only if it regards this player
            if(tempNickname.equals(GUI.playerNickname)) {
                if (tempTiles != null)
                    updateTokens(tempTiles, true);
                if (tempPersonalCard != -1)
                    setPersonalCard(tempPersonalCard);
                if (tempTotalPoints != -1)
                    points.setText(String.valueOf(tempTotalPoints));
            }
            System.out.print(toPrint);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCommonCard(int i, boolean b) {
        ImageView comm;
        if(b)
            comm = common_goal2;
        else
            comm =  common_goal1;
        comm.setImage((new Image("assets/common_goal_cards/"+i+".jpg")));
    }

    @Override
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
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

    /**
     *
     */
    private void requestTiles() {
        Platform.runLater(() -> {
            for (int[] i : tokenSelection)
                Arrays.fill(i, -1);
            tokensSelected = 0;
            selectingTokens = true;
            messages.setText("It's your turn!");
            cancel.setVisible(true);
            confirm.setVisible(true);
            confirm.setDisable(false);
        });
    }

    /**
     *
     */
    private void requestColumn() {
        Platform.runLater(() -> {
            columnSelection = -1;
            selectingColumn = true;
            messages.setText("Pick a column!");
            cancel.setVisible(true);
            confirm.setVisible(true);
            confirm.setDisable(false);
        });
    }

    /**
     *
     * @param i
     * @author Niccolò Giuliani
     */
    private void setPersonalCard(int i) {
        switch(i){
            case 1 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals.png"));
            case 2 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals2.png"));
            case 3 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals3.png"));
            case 4 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals4.png"));
            case 5 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals5.png"));
            case 6 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals6.png"));
            case 7 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals7.png"));
            case 8 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals8.png"));
            case 9 -> Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals9.png"));
            case 10 ->  Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals10.png"));
            case 11 ->  Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals11.png"));
            case 12 ->  Personal_goal.setImage(new Image("assets/personal_goal_cards/Personal_Goals12.png"));
        }
    }

    /**
     *
     * @param tokens
     * @param choice
     * @author Niccolò Giuliani
     */
    private void updateTokens(int[][] tokens, boolean choice) {
        Node node;
        for(int i = 0; i < tokens.length; i++){
            for(int j = 0; j < tokens[i].length; j++) {
                node = getNodeByRowColumnIndex(i, j, choice);
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                if(node != null) {
                    switch (tokens[i][j]) {
                        case 0 -> node.setOpacity(0);
                        case 1 -> {
                            node.getStyleClass().add("cat" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 2 -> {
                            node.getStyleClass().add("book" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 3 -> {
                            node.getStyleClass().add("toy" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 4 -> {
                            node.getStyleClass().add("trophy" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 5 -> {
                            node.getStyleClass().add("frame" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 6 -> {
                            node.getStyleClass().add("plant" + pictureNumber);
                            node.setOpacity(1);
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

