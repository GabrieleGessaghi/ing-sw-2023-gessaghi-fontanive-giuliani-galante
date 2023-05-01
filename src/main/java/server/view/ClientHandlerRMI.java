package server.view;

import client.NetworkHandlerRMI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.controller.Prompt;
import server.controller.observer.Event;
import server.controller.observer.Observer;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class ClientHandlerRMI extends ClientHandler implements ClientHandlerRMIInterface{

    private boolean available;

    private Prompt lastRequest;
    private boolean isThereRequest;
    private final ArrayList<Observer> observers;
    NetworkHandlerRMI client;
    private String clientName;
    public ClientHandlerRMI(int index){
        this.isThereRequest = true;
        this.index = index;
        this.lastRequest = Prompt.NICKNAME;
        this.available = true;
        this.client = null;
        this.observers = new ArrayList<>();
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
        client.showOutput(event.getJsonMessage());
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void requestInput(Prompt prompt) {
        this.lastRequest = prompt;
        this.isThereRequest = true;
    }

    @Override
    public void run() {

        try {
            Registry registry = LocateRegistry.getRegistry();
            client = (NetworkHandlerRMI) registry.lookup(clientName);
        }catch(Exception e){
            System.out.println("[System] Client failed: " + e);
        }
        while(true){
            if(isThereRequest) {
                client.receiveInput(lastRequest);
                isThereRequest = false;
            }


        }
    }

    public void showOutput(String jsonMessage) {
        client.showOutput(jsonMessage);
    }


    @Override
    public boolean isAvailable(){
        return this.available;
    }

    @Override
    public void setAvailable(String clientName) {
        this.clientName = clientName;
        available = false;
    }

    public void sendInput(String input){
        updateObservers(new Event(input));
    }

}
