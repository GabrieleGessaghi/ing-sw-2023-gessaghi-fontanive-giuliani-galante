package client.gui;

import client.Client;
import client.network.NetworkHandler;
import server.controller.Prompt;

public class MainSceneController implements Client {
    NetworkHandler networkHandler;



    @Override
    public void requestInput(Prompt prompt) {

    }

    @Override
    public void showOutput(String jsonMessage) {

    }

    @Override
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
}
