package client.gui;

import client.Client;
import client.network.NetworkHandler;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class PlayerShelfDialogController {

    private NetworkHandler clientHandler;
    private String nickname;

    public Boolean requestedReload;

    @FXML
    private GridPane shelfGrid;
    @FXML
    private Button shelfReloadBtn;

    public void initialize(){}

    @FXML
    public void initShelf(int [][] tokens, NetworkHandler handler, String nickname) {
        clientHandler = handler;
        this.nickname = nickname;
        requestedReload = false;
        for (int i = 0; i < shelfGrid.getRowCount(); i++) {
            for(int j = 0; j < shelfGrid.getColumnCount(); j++) {
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                ImageView imageView = (ImageView) getNodeByRowColumnIndex(i,j);
                switch (tokens[i][j]) {
                    case 0 -> {
                        imageView.setOpacity(0);
                    }
                    case 1 -> {
                        imageView.getStyleClass().add("cat_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 2 -> {
                        imageView.getStyleClass().add("book_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 3 -> {
                        imageView.getStyleClass().add("toy_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 4 -> {
                        imageView.getStyleClass().add("trophy_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 5 -> {
                        imageView.getStyleClass().add("frame_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 6 -> {
                        imageView.getStyleClass().add("plant_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                }
            }
        }
    }
    @FXML
    void reloadShelf(ActionEvent event) {
        requestedReload = true;
        Platform.runLater(() -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("requestShelf", true);
            jsonObject.addProperty("requestedPlayerNickname", nickname);
            clientHandler.sendInput(jsonObject.toString());
        });
    }

    void updateShelf(int [][] tokens){
        for (int i = 0; i < shelfGrid.getRowCount(); i++) {
            for(int j = 0; j < shelfGrid.getColumnCount(); j++) {
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                ImageView imageView = (ImageView) getNodeByRowColumnIndex(i,j);
                //if (imageView.getOpacity() != 0){
                //continue;
                //}
                switch (tokens[i][j]) {
                    case 0 -> {
                        imageView.setOpacity(0);
                    }
                    case 1 -> {
                        imageView.getStyleClass().add("cat_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 2 -> {
                        imageView.getStyleClass().add("book_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 3 -> {
                        imageView.getStyleClass().add("toy_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 4 -> {
                        imageView.getStyleClass().add("trophy_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 5 -> {
                        imageView.getStyleClass().add("frame_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 6 -> {
                        imageView.getStyleClass().add("plant_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                }
            }
        }
    }

    private Node getNodeByRowColumnIndex (int row,int column) {
        Node result = null;
        int gridRow;
        int gridColumn;
        ObservableList<Node> children = shelfGrid.getChildren();
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

