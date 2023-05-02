package server.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Observable;
import server.controller.observer.Observer;
import server.controller.observer.Event;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler of the Client
 * @author Niccolò Giuliani
 */
public abstract class ClientHandler implements Observer, Observable, Runnable {
    protected int index;
    protected List<Observer> observers;
    protected Prompt lastRequest;
    protected boolean isThereRequest;

    public ClientHandler(int index) {
        this.index = index;
        observers = new ArrayList<>();
        lastRequest = Prompt.NICKNAME;
        isThereRequest = true;
    }

    /**
     *
     * @param event
     * @author Giorgio Massimo Fontanive
     */
    @Override
    public void updateObservers(Event event) {
        String jsonMessage = event.getJsonMessage();
        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
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
     * method to request an input to the Client
     * @author Niccolò Giuliani
     * @param prompt type of request
     */
    public void requestInput(Prompt prompt) {
        this.lastRequest = prompt;
        this.isThereRequest = true;
    }

    /**
     * method to show the output
     * @author Niccolò Giuliani
     * @param jsonMessage String to show
     */
    public abstract void showOutput(String jsonMessage);

    public abstract void run();

    public int getIndex() {
        return index;
    }
}
