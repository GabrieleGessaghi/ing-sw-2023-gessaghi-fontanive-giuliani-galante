package client.network;

import client.Client;

public abstract class NetworkHandler implements Runnable {
    protected Client client;
    protected String host;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public abstract void run();

    public abstract void sendInput(String input);
}
