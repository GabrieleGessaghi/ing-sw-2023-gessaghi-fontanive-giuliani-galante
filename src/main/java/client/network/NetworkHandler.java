package client.network;

import client.Client;
import com.google.gson.JsonObject;
import server.model.View;

//TODO: Documents this

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
}
