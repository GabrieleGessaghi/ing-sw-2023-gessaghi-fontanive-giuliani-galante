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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles communication with clients.
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler implements Observer, Observable, Runnable {

    public String nickname;
    public int index;
    protected List<Observer> observers;
    private Timer timer;

    /**
     * Class constructor. Immediately asks the client for its nickname.
     */
    public ClientHandler() {
        nickname = null;
        observers = new ArrayList<>();
    }

    /**
     * Runs indefinitely, used to receive information from the client.
     */
    @Override
    public void run() {

        //Continuously pings the client
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, ConfigLoader.PING_PERIOD * 100L, ConfigLoader.PING_PERIOD);
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
        //Finds the client's nickname
//        String jsonMessage = event.jsonMessage();
//        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
//        if (jsonObject.has("nickname"))
//            nickname = jsonObject.get("nickname").getAsString();

        //Adds this clientHandler's index to the message
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
        jsonObject.addProperty("index", index);

        for(Observer o : observers)
            o.update(new Event(jsonObject.toString()));
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void update(Event event) {

        //Skips this event if it does not involve the client
        JsonObject jsonObject = JsonParser.parseString(event.jsonMessage()).getAsJsonObject();
        if (jsonObject.has("privateMessageSender") || jsonObject.has("privateMessageReceiver"))
            if (!jsonObject.get("privateMessageSender").getAsString().equals(nickname) &&
                    !jsonObject.get("privateMessageReceiver").getAsString().equals(nickname))
                return;

        sendOutput(event.jsonMessage());
    }

    /**
     * Adds itself to the list of disconnected clients.
     */
    public void disconnect() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientDisconnected", index);
        updateObservers(new Event(jsonObject.toString()));
        timer.cancel();
        timer.purge();
    }
}
