package client.gui;

import client.network.NetworkHandler;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Random;

/**
 * Controls the window displaying the other players' shelves.
 */
public class PlayerShelfDialogController {

    private NetworkHandler clientHandler;
    private String nickname;

    public Boolean requestedReload;

    @FXML
    private GridPane shelfGrid;

    /**
     * Initialize the shelf when the dialog is triggered form the main scene.
     * Set the class variable with given params.
     * @param tokens is the requested player shelf from the server.
     * @param handler is the handler of the client who opened the dialog.
     * @param nickname is the player nickname which shelf is requested.
     * @author Gabriele Gessaghi
     */
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
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.setOpacity(0);
                    }
                    case 1 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("cat_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 2 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("book_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 3 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("toy_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 4 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("trophy_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 5 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("frame_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                    case 6 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("plant_image" + pictureNumber);
                        //imageView.setOpacity(1);
                    }
                }
            }
        }
    }

    /**
     * Is the function called when the reload button in the dialog is pressed.
     * Send the request for the shelf through the client handler to the server.
     * @param event is the event triggered by the button.
     * @author Gabriele Gessaghi
     */
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

    /**
     * Update the shelf with the new values.
     * @param tokens is the updated shelf values from the server.
     * @author Gabriele Gessaghi
     */
    public void updateShelf(int [][] tokens){
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
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.setOpacity(0);
                    }
                    case 1 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("cat_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 2 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("book_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 3 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("toy_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 4 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("trophy_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 5 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("frame_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                    case 6 -> {
                        if(imageView.getStyleClass().size() > 1)
                            imageView.getStyleClass().remove(1);
                        imageView.getStyleClass().add("plant_image" + pictureNumber);
                        imageView.setOpacity(1);
                    }
                }
            }
        }
    }

    /**
     * return a node of a gridpane by row and column
     * @param row row of the node
     * @param column column of the node
     * @return Node by the row and the column
     * @author Niccolò Giuliani
     */
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

