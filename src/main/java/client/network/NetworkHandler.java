package client.network;

import client.tui.ClientTUI;

public abstract class NetworkHandler implements Runnable {
    protected ClientTUI client;
    protected String host;

    public NetworkHandler(ClientTUI client, String host) {
        this.client = client;
        this.host = host;
    }

    @Override
    public abstract void run();

    public abstract void sendInput(String input);
}
