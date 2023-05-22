package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Random;

public class PlayerShelfDialogController {

    @FXML
    private GridPane shelfGrid;
    @FXML
    private Button shelfReloadBtn;

    public void initialize(){}

    @FXML
    public void initShelf(int [][] tokens){
        for(int i = 0; i < shelfGrid.getRowCount(); i++){
            for(int j = 0; j < shelfGrid.getColumnCount(); j++) {
                StackPane stackPane = new StackPane();
                stackPane.setPrefWidth(49);
                stackPane.setPrefHeight(46);
                ImageView imageView = new ImageView();
                imageView.setFitWidth(49);
                imageView.setFitHeight(46);
                imageView.setImage(new Image("assets/item_tiles/Gatti1.1.png"));
                Random rng = new Random();
                int pictureNumber = rng.nextInt(3) + 1;
                stackPane.getChildren().add(imageView);
                shelfGrid.add(stackPane, i, j);
//                switch (tokens[i][j]) {
//                    case 0 -> {
//                        imageView.setImage(new Image("assets/item_tiles/Gatti1.1.png"));
//                    }
//                    case 1 -> {
//                        imageView.getStyleClass().add("cat_image" + pictureNumber);
//                    }
//                    case 2 -> {
//                        imageView.getStyleClass().add("book_image" + pictureNumber);
//                    }
//                    case 3 -> {
//                        imageView.getStyleClass().add("toy_image" + pictureNumber);
//                    }
//                    case 4 -> {
//                        imageView.getStyleClass().add("trophy_image" + pictureNumber);
//                    }
//                    case 5 -> {
//                        imageView.getStyleClass().add("frame_image" + pictureNumber);
//                    }
//                    case 6 -> {
//                        imageView.getStyleClass().add("plant_image" + pictureNumber);
//                        imageView.setOpacity(1);
//                    }
//                }
            }
        }
    }
    @FXML
    void reloadShelf(ActionEvent event) {

    }

}

