package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ClientGUI.fxml"));
        Scene base = new Scene(root);
        base.getStylesheets().add(getClass().getResource("/Application.css").toExternalForm());
        primaryStage.setScene(base);
        primaryStage.setTitle("MyShelfie");
        primaryStage.show();
    }
}
