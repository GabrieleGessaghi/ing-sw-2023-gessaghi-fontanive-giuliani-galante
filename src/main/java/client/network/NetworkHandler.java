package client.network;

import client.Client;
import client.tui.ClientTUI;

public abstract class NetworkHandler implements Runnable {
    protected Client client;
    protected String host;

    public NetworkHandler(Client client, String host) {
        this.client = client;
        this.host = host;
    }

    @Override
    public abstract void run();

    public abstract void sendInput(String input);
}
