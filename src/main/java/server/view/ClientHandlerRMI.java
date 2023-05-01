package server.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Event;
import server.controller.observer.Observer;

import java.util.ArrayList;

public class ClientHandlerRMI extends ClientHandler implements ClientHandlerRMIInterface{
    private Prompt lastRequest;
    private boolean isThereRequest;
    private final ArrayList<Observer> observers;

    public ClientHandlerRMI(int index){
        this.observers = null;
        this.isThereRequest = true;
        this.index = index;
        this.lastRequest = Prompt.NICKNAME;
    }
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
    public void update(Event event) {

    }

    @Override
    public void registerObserver(Observer observer) {

    }

    @Override
    public void requestInput(Prompt prompt) {

    }

    @Override
    public void run() {

    }

    @Override
    public void showOutput(String jsonMessage) {

    }
}
