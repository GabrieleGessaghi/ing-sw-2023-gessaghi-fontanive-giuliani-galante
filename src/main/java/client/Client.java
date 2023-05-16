package client;

import client.network.NetworkHandler;
import server.controller.Prompt;

//TODO: Document this class

public interface Client {

    void requestInput(Prompt prompt);

    void showOutput(String jsonMessage);

    void setNetworkHandler(NetworkHandler networkHandler);
}
