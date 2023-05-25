package server.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.Server;
import server.controller.Controller;
import server.controller.Prompt;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.observer.Event;
import server.controller.utilities.ConfigLoader;

import java.util.*;

/**
 * Handles communication with clients.
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler implements Observer, Observable, Runnable {

    public String nickname;
    public int index;
    protected boolean isConnected;
    protected List<Observer> observers;
    private Timer timer;

    /**
     * Class constructor. Immediately asks the client for its nickname.
     */
    public ClientHandler() {
        nickname = null;
        isConnected = true;
        observers = new ArrayList<>();
    }

    /**
     * Runs indefinitely, used to receive information from the client.
     * Also pings the client to detect disconnections
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
     * method to request an input to the TUI
     * @author Niccolò Giuliani
     * @param prompt type of request
     */
    public abstract void requestInput(Prompt prompt);

    /**
     * method to show the output
     * @author Niccolò Giuliani
     * @param jsonMessage String to show
     */
    public abstract void sendOutput(String jsonMessage);

    /**
     *
     * @author Giorgio massimo Fontanive
     */
    public void ping() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ping", true);
        sendOutput(jsonObject.toString());
    }

    @Override
    public void updateObservers(Event event) {

        //Adds this clientHandler's index to the message
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        jsonObject.addProperty("index", index);

        try {
            for (Observer observer : observers)
                observer.update(new Event(jsonObject.toString()));
        } catch (ConcurrentModificationException ignored) {}
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void update(Event event) {

        //Skips this event if it does not involve the client
//        JsonObject jsonObject = JsonParser.parseString(event.jsonMessage()).getAsJsonObject();
//        if (jsonObject.has("privateMessageSender") || jsonObject.has("privateMessageReceiver"))
//            if (!jsonObject.get("privateMessageSender").getAsString().equals(nickname) &&
//                    !jsonObject.get("privateMessageReceiver").getAsString().equals(nickname))
//                return;

        sendOutput(event.jsonMessage());
    }

    /**
     * Adds itself to the list of disconnected clients.
     */
    public void disconnect() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientDisconnected", index);
        updateObservers(new Event(jsonObject.toString()));
        isConnected = false;
        timer.cancel();
        timer.purge();
    }

    public void reconnect() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientReconnected", index);
        updateObservers(new Event(jsonObject.toString()));
    }
}
