package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlURL = new File("src/main/resources/ClientGUI.fxml").toURI().toURL();
        //Parent root = FXMLLoader.load(getClass().getResource("src/main/resources/ClientGUI.fxml"));
        Parent root = FXMLLoader.load(fxmlURL);
        Scene base = new Scene(root);
        //base.getStylesheets().add(getClass().getResource("src/main/resources/Apllicaion.css").toExternalForm());
        primaryStage.setScene(base);
        primaryStage.show();
    }
}
