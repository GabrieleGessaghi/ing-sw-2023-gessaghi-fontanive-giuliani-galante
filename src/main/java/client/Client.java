package client;

import client.network.NetworkHandler;
import server.controller.Prompt;

/**
 * Allows the network handler to relay information from and to the server regardless of client type.
 * @author Giorgio Massimo Fontanive
 */
public interface Client {

    /**
     * Asks the player for specific information.
     * @param prompt The type of information the server needs.
     */
    void requestInput(Prompt prompt);

    /**
     * Updates or shows on screen the information received in the json message.
     * @param jsonMessage The json message received from the server.
     */
    void showOutput(String jsonMessage);

    /**
     * Setter for the network handler
     */
    void setNetworkHandler(NetworkHandler networkHandler);
}
