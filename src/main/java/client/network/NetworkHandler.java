package client.network;

import client.Client;
import com.google.gson.JsonObject;
import server.controller.utilities.ConfigLoader;
import server.model.View;

import java.util.Timer;
import java.util.TimerTask;

//TODO: Documents this

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
     *
     * @author Giorgio massimo Fontanive
     */
    public void ping() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ping", true);
        sendInput(jsonObject.toString());
    }

    public abstract void sendInput(String input);

    public void requestView(View view, String playerNickname) {
        JsonObject jsonObject = new JsonObject();
        String request = null;
        switch (view) {
            case BOARD -> request = "requestBoard";
            case CURRENT_PLAYER -> request = "currentPlayer";
            case SPECIFIC_PLAYER -> request = "requestPlayer";
            case COMMON_CARDS -> request = "requestCommonCards";
            case PERSONAL_CARD -> request = "requestPersonalCard";
            case CHAT -> request = "requestChat";
            case SHELF -> request = "requestShelf";
        }
        if (request != null)
            jsonObject.addProperty(request, true);
        if (playerNickname != null)
            jsonObject.addProperty("requestedPlayerNickname", playerNickname);
        sendInput(jsonObject.toString());
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     *
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
