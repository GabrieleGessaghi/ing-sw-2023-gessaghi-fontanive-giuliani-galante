package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.controller.utilities.ConfigLoader;


public class GUI extends Application {
    private Stage stage;

    //Starting constants
    public static String playerNickname;
    public static int connectionType;
    public static String host;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConfigLoader.loadConfiguration("src/main/resources/json/configuration.json");
        Parent root = FXMLLoader.load(getClass().getResource("/javafx/GUI.fxml"));
        Scene base = new Scene(root);
        base.getStylesheets().add(getClass().getResource("/javafx/Application.css").toExternalForm());
        primaryStage.setScene(base);
        primaryStage.setTitle("MyShelfie");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
