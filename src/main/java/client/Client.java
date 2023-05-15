package client;

import client.network.NetworkHandler;
import server.controller.Prompt;

//TODO: Document this class

public abstract class Client {
    protected NetworkHandler networkHandler;

    public abstract void requestInput(Prompt prompt);

    public abstract void showOutput(String jsonMessage);

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
}
