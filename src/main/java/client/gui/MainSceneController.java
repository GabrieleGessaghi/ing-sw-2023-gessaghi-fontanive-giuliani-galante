package client.gui;

import client.Client;
import client.network.NetworkHandler;
import client.network.NetworkHandlerRMI;
import client.network.NetworkHandlerTCP;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.controller.Prompt;
import server.controller.utilities.ConfigLoader;
import server.controller.utilities.JsonTools;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

/**
 * Controls the main scene where the game is played.
 */
public class MainSceneController implements Client, Initializable {
    @FXML
    public ImageView common_goal1;
    @FXML
    public ImageView common_goal2;
    @FXML
    public ImageView twoComm1;
    @FXML
    public ImageView fourComm1;
    @FXML
    public ImageView sixComm1;
    @FXML
    public ImageView eightComm1;
    @FXML
    public ImageView twoComm2;
    @FXML
    public ImageView fourComm2;
    @FXML
    public ImageView sixComm2;
    @FXML
    public ImageView eightComm2;
    @FXML
    public ImageView chair;
    @FXML
    public ImageView chair2;
    @FXML
    private GridPane board;
    @FXML
    private GridPane shelf;
    @FXML
    private Label points;
    @FXML
    private Label messages;
    @FXML
    private Label personalGoalLabel;
    @FXML
    private Label commonGoalLabel;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;
    @FXML
    private ImageView personalGoal;
    @FXML
    private Button player1Btn;
    @FXML
    private Button player2Btn;
    @FXML
    private Button player3Btn;

    @FXML
    private Button chatButton;

    NetworkHandler networkHandler;
    int[][] tokenSelection;
    int tokensSelected;
    int columnSelection;
    boolean selectingTokens;
    boolean selectingColumn;
    boolean isPlayerWindowOpen;
    boolean isWinnerWindowOpen;
    client.gui.ChatController chatController;
    int[][] tempPlayerShelf;
    PlayerShelfDialogController playerShelfController;
    List<String> nicknames = new ArrayList<>();

    /**
     * Initialized the variable and some event handlers when this stage starts.
     * @author Giorgio Massimo Fontanive
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Node> selectedNodes = new ArrayList<>();
        tokenSelection = new int[ConfigLoader.BOARD_SIZE][ConfigLoader.BOARD_SIZE];
        for (int[] i : tokenSelection)
            Arrays.fill(i, -1);
        tokensSelected = 0;
        columnSelection = -1;
        isPlayerWindowOpen = false;
        chatController = null;

        //Add buttons functionalities
        cancel.setOnMouseClicked(e -> {
            for (int[] row : tokenSelection)
                Arrays.fill(row, -1);
            tokensSelected = 0;
            for (Node i : selectedNodes)
                i.setOpacity(1);
            selectedNodes.clear();
        });
        confirm.setOnMouseClicked(e -> {
            if (selectingTokens) {
                // handle token order here
                if (tokensSelected >1){
                    manageTilesOrder();
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("selectedTiles", JsonTools.createJsonMatrix(tokenSelection));
                networkHandler.sendInput(jsonObject.toString());
                for (int[] i : tokenSelection)
                    Arrays.fill(i, -1);
                tokensSelected = 0;
                selectingTokens = false;
                for (Node i : selectedNodes)
                    i.setOpacity(1);
                selectedNodes.clear();
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
                if (node.getOpacity() == 1) {
                    if(confirm.isVisible())
                        node.setOpacity(0.5);
                    selectedNodes.add(node);
                    selectedNodes.add(node);
                    tokenSelection[row][column] = tokensSelected;
                    tokensSelected++;
                }

            });
        for (Node node : shelf.getChildren())
            node.setOnMouseClicked(e -> {
                columnSelection = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
                if (selectingColumn) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("selectedColumn", columnSelection);
                    networkHandler.sendInput(jsonObject.toString());
                    columnSelection = -1;
                    selectingColumn = false;
                    messages.setText("");
                }
            });

        //Connects to the server
        if (GUI.connectionType == 0)
            setNetworkHandler(new NetworkHandlerTCP());
        if (GUI.connectionType == 1)
            setNetworkHandler(new NetworkHandlerRMI());
        networkHandler.setHost(GUI.host);
        networkHandler.setClient(this);
        new Thread(networkHandler).start();
    }

    /**
     * Request the information the server needs.
     * @param prompt The type of information the server needs.
     * @author Giorgio Massimo Fontanive
     */
    @Override
    public void requestInput(Prompt prompt) {
        switch (prompt) {
            case NICKNAME -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("nickname", GUI.playerNickname);
                networkHandler.sendInput(jsonObject.toString());
                Platform.runLater(() -> {
                    Stage currentWindow = (Stage) chatButton.getScene().getWindow();
                    currentWindow.setOnCloseRequest(e -> {
                        Platform.exit();
                        System.exit(0);
                    });
                });
            }
            case PLAYERSNUMBER -> {
                requestPlayersNumber();
                chair.setVisible(false);
                chair2.setVisible(true);
            }
            case TOKENS -> requestTiles();
            case COLUMN -> requestColumn();
        }
    }

    /**
     * Updates the information visible on screen based on the received message from the server.
     * @param jsonMessage The json message received from the server.
     */
    @Override
    public void showOutput(String jsonMessage) {
        String tempNickname = "";
        int[][] tempTiles = null;
        int tempPersonalCard = -1;
        int tempTotalPoints = -1;
        JsonReader jsonReader = new JsonReader(new StringReader(jsonMessage));
        String field;
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                field = jsonReader.nextName();
                switch (field) {
                    case "nickname" -> tempNickname = jsonReader.nextString();
                    case "newNickname" -> GUI.playerNickname = jsonReader.nextString();
                    case "nicknames" -> {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext())
                            nicknames.add(jsonReader.nextString());
                        jsonReader.endArray();
                        if (nicknames.size() > 1) {
                            Platform.runLater(() -> updateOpponents(nicknames));
                        }
                        startGame();
                    }
                    case "points" -> {
                        tempTotalPoints = jsonReader.nextInt();
                        int finalTempTotalPoints = tempTotalPoints;
                        Platform.runLater(() -> points.setText(String.valueOf(finalTempTotalPoints)));
                    }
                    case "commonCard0" -> {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            field = jsonReader.nextName();
                            if (field.equals("cardIndex"))
                                setCommonCard(jsonReader.nextInt(), false);
                            else if (field.equals("nextPointsAvailable"))
                                updatePointsCommonCards(false, jsonReader.nextInt());
                            else
                                jsonReader.skipValue();
                        }
                        jsonReader.endObject();
                    }
                    case "commonCard1" -> {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            field = jsonReader.nextName();
                            if (field.equals("cardIndex"))
                                setCommonCard(jsonReader.nextInt(), true);
                            else if (field.equals("nextPointsAvailable"))
                                updatePointsCommonCards(true, jsonReader.nextInt());
                            else
                                jsonReader.skipValue();
                        }
                        jsonReader.endObject();
                    }
                    case "message" -> {
                        String nextString = jsonReader.nextString();
                        Platform.runLater(() -> messages.setText(nextString));
                    }
                    case "messages" -> {
                        if (chatController != null) {
                            List<String> messages = new ArrayList<>();
                            jsonReader.beginArray();
                            while (jsonReader.hasNext())
                                messages.add(jsonReader.nextString());
                            jsonReader.endArray();
                            chatController.setMessages(messages);
                        }
                    }
                    case "personalCard" -> {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
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
                        tempPlayerShelf = tempTiles;
                        if (playerShelfController != null) {
                            if (tempNickname != GUI.playerNickname){
                                if (playerShelfController.requestedReload){
                                    playerShelfController.updateShelf(tempTiles);
                                    playerShelfController.requestedReload = false;
                                }
                            }
                        }
                        jsonReader.endObject();
                    }
                    case "connectionError" -> {
                        if (isWinnerWindowOpen){
                            jsonReader.skipValue();
                        }else {
                            Platform.runLater(() -> {
                                Parent root = null;
                                try {
                                    root = FXMLLoader.load(getClass().getResource("/javafx/GUI.fxml"));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                Scene base = new Scene(root);
                                base.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
                                Stage currentWindow = (Stage) chatButton.getScene().getWindow();
                                currentWindow.setScene(base);
                                currentWindow.setResizable(true);
                                currentWindow.show();
                            });
                            jsonReader.skipValue();
                        }
                    }
                    case "winnerNickname" -> {
                        isWinnerWindowOpen = true;
                        showWinner(jsonReader.nextString());
                    }
                    default -> jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

            //Updates some information only if it regards this player
            if (tempNickname.equals(GUI.playerNickname)) {
                if (tempTiles != null)
                    updateTokens(tempTiles, true);
                if (tempPersonalCard != -1)
                    setPersonalCard(tempPersonalCard);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes the visible parts of the screen when the game starts.
     * @author Giorgio Massimo Fontanive
     */
    private void startGame() {
        Platform.runLater(() -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("requestPlayer", true);
            networkHandler.sendInput(jsonObject.toString());

            //Sets UI elements to visible
            personalGoalLabel.setVisible(true);
            commonGoalLabel.setVisible(true);
            common_goal1.setVisible(true);
            common_goal2.setVisible(true);
            eightComm1.setVisible(true);
            eightComm2.setVisible(true);
            sixComm1.setVisible(true);
            sixComm2.setVisible(true);
            fourComm1.setVisible(true);
            fourComm2.setVisible(true);
            twoComm1.setVisible(true);
            twoComm2.setVisible(true);
        });
    }

    /**
     * change graphic number of points available of the common cards
     * @param commonCardSwitch indicate the common card (0 = common_goal2, 1 = commonGoal1)
     * @param nextPointsAvailable next points available for the card
     * @author Niccolò Giuliani
     */
    private void updatePointsCommonCards(boolean commonCardSwitch, int nextPointsAvailable) {
        if (!commonCardSwitch) {
            twoComm1.setVisible(false);
            fourComm1.setVisible(false);
            sixComm1.setVisible(false);
            eightComm1.setVisible(false);
            switch (nextPointsAvailable) {
                case 2 -> twoComm1.setVisible(true);
                case 4 -> fourComm1.setVisible(true);
                case 6 -> sixComm1.setVisible(true);
                case 8 -> eightComm1.setVisible(true);
            }
        } else {
            twoComm2.setVisible(false);
            fourComm2.setVisible(false);
            sixComm2.setVisible(false);
            eightComm2.setVisible(false);
            switch (nextPointsAvailable) {
                case 2 -> twoComm2.setVisible(true);
                case 4 -> fourComm2.setVisible(true);
                case 6 -> sixComm2.setVisible(true);
                case 8 -> eightComm2.setVisible(true);
            }
        }
    }

    @Override
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    /**
     * set the right image of the common cards
     * @param commonCardIndex index fot the name of the image
     * @param commonCardSwitch indicate the common card (0 = common_goal2, 1 = commonGoal1)
     * @author Niccolò Giuliani
     */
    private void setCommonCard(int commonCardIndex, boolean commonCardSwitch) {
        ImageView comm;
        if (commonCardSwitch)
            comm = common_goal2;
        else
            comm = common_goal1;
        commonCardIndex = commonCardIndex + 1;
        comm.setImage((new Image("assets/common_goal_cards/" + commonCardIndex + ".jpg")));
    }

    /**
     * set the buttons of the nicknames with the right Nickname
     * @param nicknames List of nicknames
     * @author Niccolò Giuliani
     */
    private void updateOpponents(List<String> nicknames) {
        switch (nicknames.size()) {
            case 2 -> {
                for (String nick : nicknames)
                    if (!nick.equals(GUI.playerNickname)) {
                        player1Btn.setText(nick);
                        player1Btn.setVisible(true);
                    }
            }
            case 3 -> {
                int i = 0;
                for (String nick : nicknames)
                    if (!nick.equals(GUI.playerNickname)) {
                        if (i == 0) {
                            player1Btn.setText(nick);
                            player1Btn.setVisible(true);
                            i++;
                        } else if (i == 1) {
                            player2Btn.setText(nick);
                            player2Btn.setVisible(true);
                        }
                    }
            }
            case 4 -> {
                int i = 0;
                for (String nick : nicknames)
                    if (!nick.equals(GUI.playerNickname)) {
                        if (i == 0) {
                            player1Btn.setText(nick);
                            player1Btn.setVisible(true);
                            i++;
                        } else if (i == 1) {
                            player2Btn.setText(nick);
                            player2Btn.setVisible(true);
                            i++;
                        } else if (i == 2) {
                            player3Btn.setText(nick);
                            player3Btn.setVisible(true);
                        }
                    }
            }
        }
    }

    /**
     * Shows a popup screen asking user for player's number and send the information to the server.
     * @author Gabriele Gessaghi
     */
    private void requestPlayersNumber() {
        Platform.runLater(() -> {
            String[] arrayData = {"Two (2)", "Three (3)", "Four (4)"};
            List<String> dialogData = Arrays.asList(arrayData);
            ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
            dialog.setTitle("Game settings");
            dialog.setGraphic(null);
            dialog.setHeaderText("Select the number of players: ");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
            dialogPane.getStyleClass().add("playerDialog");
            Optional<String> result = dialog.showAndWait();
            int numberOfPlayers = 0;
            if (result.isPresent()) {
                switch (result.get()) {
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
     * Starts the tile selection process.
     * @author Giorgio Massimo Fontanive
     */
    private void requestTiles() {
        Platform.runLater(() -> {
            for (int[] i : tokenSelection)
                Arrays.fill(i, -1);
            tokensSelected = 0;
            selectingTokens = true;
            cancel.setVisible(true);
            confirm.setVisible(true);
            confirm.setDisable(false);
            messages.setText("It's your turn!");
        });
    }

    /**
     * Starts the column selection process.
     * @author Giorgio Massimo Fontanive
     */
    private void requestColumn() {
        Platform.runLater(() -> {
            columnSelection = -1;
            selectingColumn = true;
            messages.setText("Pick a column!");
        });
    }

    /**
     * set the right image for the personal goal card
     * @param i index of the name of the image of the personal card
     * @author Niccolò Giuliani
     */
    private void setPersonalCard(int i) {
        switch (i) {
            case 1 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals.png"));
            case 2 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals2.png"));
            case 3 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals3.png"));
            case 4 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals4.png"));
            case 5 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals5.png"));
            case 6 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals6.png"));
            case 7 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals7.png"));
            case 8 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals8.png"));
            case 9 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals9.png"));
            case 10 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals10.png"));
            case 11 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals11.png"));
            case 12 -> personalGoal.setImage(new Image("assets/personal_goal_cards/Personal_Goals12.png"));
        }
    }

    /**
     * set images of the tokens of the board and of the personal shelf
     * @param tokens tokens of the grid pane
     * @param choice choice of what to modify (0 board, 1 shelf)
     * @author Niccolò Giuliani
     */
    private void updateTokens(int[][] tokens, boolean choice) {
        Node node;
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[i].length; j++) {
                node = getNodeByRowColumnIndex(i, j, choice);
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                if (node != null) {

                    switch (tokens[i][j]) {
                        case 0 -> {
                            if(node.getStyleClass().size() > 1)
                             node.getStyleClass().remove(1);
                            node.setOpacity(0);
                        }
                        case 1 -> {
                            if (node.getOpacity() != 0){
                                continue;
                            }

                            if(node.getStyleClass().size() > 1)
                                node.getStyleClass().remove(1);
                            node.getStyleClass().add("cat" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 2 -> {
                            if (node.getOpacity() != 0){
                                continue;
                            }
                            if(node.getStyleClass().size() > 1)
                                node.getStyleClass().remove(1);
                            node.getStyleClass().add("book" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 3 -> {
                            if (node.getOpacity() != 0){
                                continue;
                            }
                            if(node.getStyleClass().size() > 1)
                                node.getStyleClass().remove(1);
                            node.getStyleClass().add("toy" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 4 -> {
                            if (node.getOpacity() != 0){
                                continue;
                            }
                            if(node.getStyleClass().size() > 1)
                                node.getStyleClass().remove(1);
                            node.getStyleClass().add("trophy" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 5 -> {
                            if (node.getOpacity() != 0){
                                continue;
                            }
                            if(node.getStyleClass().size() > 1)
                                node.getStyleClass().remove(1);
                            node.getStyleClass().add("frame" + pictureNumber);
                            node.setOpacity(1);
                        }
                        case 6 -> {
                            if (node.getOpacity() != 0){
                                continue;
                            }
                            if(node.getStyleClass().size() > 1)
                                node.getStyleClass().remove(1);
                            node.getStyleClass().add("plant" + pictureNumber);
                            node.setOpacity(1);
                        }
                    }
                }
            }
        }
    }

    /**
     * return a node of a gridpane by row and column
     * @param row row of the node
     * @param column column of the node
     * @param choice choice of what to modify (0 board, 1 shelf)
     * @return Node by the row and the column
     * @author Niccolò Giuliani
     */
    private Node getNodeByRowColumnIndex(int row, int column, boolean choice) {
        Node result = null;
        int gridRow;
        int gridColumn;
        ObservableList<Node> children;
        if (choice)
            children = shelf.getChildren();
        else
            children = board.getChildren();
        for (Node node : children) {
            try {
                if (GridPane.getRowIndex(node) == null)
                    gridRow = 0;
                else
                    gridRow = GridPane.getRowIndex(node);

                if (GridPane.getColumnIndex(node) == null)
                    gridColumn = 0;
                else
                    gridColumn = GridPane.getColumnIndex(node);

                if (gridRow == row && gridColumn == column) {
                    result = node;
                    break;
                }
            } catch (NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        }
        return result;
    }

    /**
     * Create the shelf dialog by loading the FXML files and send request for the shelf of chosed player.
     * Initialize the shelf with the values received from the server.
     * Set the class variable isPlayerWindowOpen.
     * @param event is the event triggered by the button action.
     * @author Gabriele Gessaghi
     */
    @FXML
    private void playerBtnClicked(ActionEvent event) {
        Button playerBtn = (Button) event.getSource();
        String nickname = playerBtn.getText();
        Platform.runLater(() -> {
            isPlayerWindowOpen = true;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("requestShelf", true);
            jsonObject.addProperty("requestedPlayerNickname", nickname);
            networkHandler.sendInput(jsonObject.toString());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/PlayerShelfDialog.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playerShelfController = loader.getController();
            playerShelfController.initShelf(tempPlayerShelf, networkHandler, nickname);
            Stage playerShelf = new Stage();
            Scene base = new Scene(root);
            base.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
            playerShelf.setScene(base);
            playerShelf.setTitle(nickname + " shelf:");
            playerShelf.setResizable(false);
            playerShelf.show();
        });
        isPlayerWindowOpen = false;
    }

    /**
     * Opens the chat when its button is clicked.
     * @author Niccolò Galante
     */
    @FXML
    void chatButtonClicked() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/Chat.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            chatController = loader.getController();
            Stage chatStage = new Stage();
            Scene chatScene = new Scene(root);
            chatScene.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
            chatStage.setScene(chatScene);
            chatStage.setTitle("Chat");
            chatStage.setResizable(false);
            chatController.setRadioButtons(nicknames);
            chatController.setNetworkHandler(networkHandler);
            chatStage.show();
        });
        chatController = null;
    }

    /**
     * Create and show the dialog with the game results.
     * @param winnerNickname is the nickname of the player who win the game
     * @author Gabriele Gessaghi
     */
    private void showWinner(String winnerNickname){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            String imagePath = getClass().getResource("/assets/award.png").toExternalForm();
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(400);
            imageView.setFitHeight(300);
            Text text;
            if (GUI.playerNickname.equals(winnerNickname)) {
                text = new Text("Congratulazioni " + winnerNickname + ", hai vinto !");
            } else {
                text = new Text("Hai perso, il vincitore è " + winnerNickname + " !");
            }

            text.setFont(Font.font(18));
            text.setFill(Color.WHITE);

            Button backButton = new Button("Torna alla home");
            backButton.setOnAction(event -> {
                alert.setResult(ButtonType.CLOSE);
                alert.close();
                isWinnerWindowOpen = false;
                Platform.runLater(() -> {
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/javafx/GUI.fxml"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene base = new Scene(root);
                    base.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
                    Stage currentWindow = (Stage) chatButton.getScene().getWindow();
                    currentWindow.setScene(base);
                    currentWindow.setResizable(true);
                    currentWindow.show();
                });
            });
            VBox vbox = new VBox(10);
            vbox.getChildren().addAll(imageView, text, backButton);
            vbox.setAlignment(Pos.CENTER);
            BorderPane borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-image: url('/assets/misc/base_pagina2.jpg'); -fx-background-size: cover; -fx-padding: 0;");
            borderPane.setCenter(vbox);
            alert.getDialogPane().setContent(borderPane);
            alert.getDialogPane().setStyle("-fx-background-image: url('/assets/misc/base_pagina2.jpg'); -fx-background-size: cover; -fx-padding: 0;");
            alert.showAndWait();
        });
    }

    /**
     * Create and show the dialog for the tiles order selection during the player turn.
     * Set the class variable tokenSelection with the ordered tiles.
     * @author Gabriele Gessaghi
     */
    private void manageTilesOrder(){
        // fill arraylist with token images
        ArrayList<ImageView> tokens = new ArrayList<>();
        Map<Integer, ImageView> tokenIndexMap = new HashMap<>();
        for (int i=0; i<tokenSelection.length; i++){
            for (int j=0; j<tokenSelection[0].length; j++){
                if (tokenSelection[i][j]!=-1){
                    ImageView token = new ImageView();
                    token.setFitWidth(50);
                    token.setFitHeight(50);
                    ObservableList<String> cssClasses = getNodeByRowColumnIndex(i, j, false).getStyleClass();
                    String className = cssClasses.get(1);
                    String number = className.substring(className.length() - 1);
                    String imageClassName = className.substring(0, className.length() - 1) + "_image" + number;
                    token.getStyleClass().removeAll();
                    token.getStyleClass().addAll(imageClassName);
                    tokens.add(token);
                    tokenIndexMap.put(tokenSelection[i][j],token);
                }
            }
        }
        int rowHeight = 50;
        int visibleRows = 4;
        double preferredHeight = visibleRows * rowHeight;
        ListView<ImageView> listView = new ListView<>();
        ObservableList<ImageView> observableTokens = FXCollections.observableArrayList(tokens);
        listView.setItems(observableTokens);
        listView.setPrefHeight(preferredHeight);
        Button moveUpButton = new Button("UP");
        Button moveDownButton = new Button("DOWN");
        moveUpButton.setOnAction(event -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex > 0) {
                Collections.swap(observableTokens, selectedIndex, selectedIndex - 1);
                listView.getSelectionModel().select(selectedIndex - 1);
            }
        });

        moveDownButton.setOnAction(event -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex < tokens.size() - 1) {
                Collections.swap(observableTokens, selectedIndex, selectedIndex + 1);
                listView.getSelectionModel().select(selectedIndex + 1);
            }
        });

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().add(listView);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        hbox.getChildren().addAll(moveUpButton, moveDownButton);

        vbox.getChildren().add(hbox);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Set tiles order");
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        //dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
        dialog.getDialogPane().setStyle("-fx-background-image: url('/assets/misc/base_pagina2.jpg'); -fx-background-size: cover; -fx-padding: 0;");
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                return buttonType;
            }
            return null;
        });
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            for (int i=0; i<tokenSelection.length; i++){
                for (int j=0; j<tokenSelection[0].length; j++){
                    if (tokenSelection[i][j]!=-1){
                        int newIndex = observableTokens.indexOf(tokenIndexMap.get(tokenSelection[i][j]));
                        tokenSelection[i][j] = newIndex;
                    }
                }
            }
        }
    }
}


