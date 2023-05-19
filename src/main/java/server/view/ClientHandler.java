package server.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.observer.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles communication with clients.
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler implements Observer, Observable, Runnable {

    public String nickname;
    public int index;
    protected List<Observer> observers;
    protected boolean isConnected;

    /**
     * Class constructor. Immediately asks the client for its nickname.
     */
    public ClientHandler() {
        nickname = null;
        observers = new ArrayList<>();
        isConnected = false;
    }

    /**
     * Runs indefinitely, used to receive information from the client.
     */
    public abstract void run();

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

    public boolean isConnected() {
        return isConnected;
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
    public abstract void update(Event event);
}
