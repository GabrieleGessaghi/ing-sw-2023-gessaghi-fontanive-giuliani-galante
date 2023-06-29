package client.network;

import client.Client;
import com.google.gson.JsonObject;
import server.controller.utilities.ConfigLoader;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Defines a module for the client to communicate with the server.
 * @author Giorgio Massimo Fontanive
 */
public abstract class NetworkHandler implements Runnable {

    protected Client client;
    protected String host;
    protected boolean isConnected;
    private Timer timer;

    public NetworkHandler() {
        isConnected = true;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Sets a heartbeat to detect disconnections.
     */
    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, 0, ConfigLoader.PING_PERIOD);
    }

    /**
     * Sends a ping message to the server.
     * @author Giorgio massimo Fontanive
     */
    public void ping() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ping", true);
        sendInput(jsonObject.toString());
    }

    /**
     * Sends a message to server in json format.
     * @param input The json message to be sent to the server.
     */
    public abstract void sendInput(String input);

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Stops all networking for this client.
     */
    public void disconnect() {
        isConnected = false;
        timer.cancel();
        timer.purge();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("connectionError", true);
        client.showOutput(jsonObject.toString());
    }
}
