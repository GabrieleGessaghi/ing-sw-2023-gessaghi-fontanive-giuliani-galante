package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.controller.utilities.ConfigLoader;


public class GUI extends Application {

    private Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConfigLoader.loadConfiguration("src/main/resources/configuration.json");
        Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));
        Scene base = new Scene(root);
        base.getStylesheets().add(getClass().getResource("/Application.css").toExternalForm());
        primaryStage.setScene(base);
        primaryStage.setTitle("MyShelfie");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
