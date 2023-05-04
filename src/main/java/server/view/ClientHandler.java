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
 * Handler of the ClientTUI
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler implements Observer, Observable, Runnable {
    protected String nickname;
    protected int index;
    protected List<Observer> observers;

    /**
     * Class constructor. Immediately asks the client for its nickname.
     * @param index This client's unique identifier.
     */
    public ClientHandler(int index) {
        nickname = null;
        this.index = index;
        observers = new ArrayList<>();
    }

    @Override
    public void updateObservers(Event event) {
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();

        if (jsonObject.has("selectedTiles"))
            System.out.println("Received tiles");

        //Finds the client's nickname
        if (jsonObject.has("nickname"))
            nickname = jsonObject.get("nickname").getAsString();

        //Adds client's index and sends the message to observers
        jsonObject.addProperty("clientIndex", index);
        Event indexedEvent = new Event(jsonObject.toString());
        for(Observer o : observers)
            o.update(indexedEvent);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public abstract void update(Event event);

    /**
     * method to request an input to the ClientTUI
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
     * Runs indefinitely, used to receive information from the client.
     */
    public abstract void run();

    /**
     *
     * @author Giorgio massimo Fontanive
     */
    public void ping() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ping", true);
        sendOutput(jsonObject.toString());
    }

    /**
     * The index represents a unique identifier to identify the client.
     * @return This client's index.
     */
    public int getIndex() {
        return index;
    }

    public String getNickname() {return nickname;}
}
