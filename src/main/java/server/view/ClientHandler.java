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
    protected List<Observer> observers;

    /**
     * Class constructor. Immediately asks the client for its nickname.
     */
    public ClientHandler() {
        nickname = null;
        observers = new ArrayList<>();
    }

    @Override
    public void updateObservers(Event event) {
        String jsonMessage = event.jsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();

        //Finds the client's nickname
        if (jsonObject.has("nickname"))
            nickname = jsonObject.get("nickname").getAsString();

        for(Observer o : observers)
            o.update(event);
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

    public String getNickname() {return nickname;}
}
